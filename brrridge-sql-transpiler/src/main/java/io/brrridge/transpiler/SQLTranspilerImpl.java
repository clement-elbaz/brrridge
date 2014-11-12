package io.brrridge.transpiler;

import io.brrridge.transpiler.exception.SQLTranspilationFailedException;
import io.brrridge.transpiler.exception.UnitCompilationFailedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SQLTranspilerImpl implements SQLTranspiler {

	/*
	 * (non-Javadoc)
	 * @see io.brrridge.transpiler.SQLTranspiler#transpile(java.lang.String, java.util.Map)
	 */
	@Override
	public TranspilationResult transpile(String sqlRequest,
			Map<String, List<String>> parameters) throws SQLTranspilationFailedException {
		List<String> parametersOutput = new ArrayList<String>();
		try {
			String transpiledRequest = new SQLCompilationUnit(new StringBuffer(sqlRequest), parameters, parametersOutput).compileUnit();
			
			return new TranspilationResult(transpiledRequest, parametersOutput);
		} catch (UnitCompilationFailedException e) {
			throw new SQLTranspilationFailedException(e);
		}
	}

}
