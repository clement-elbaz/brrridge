package io.brrridge.transpiler;

import io.brrridge.transpiler.exception.SQLTranspilationFailedException;

import java.util.List;
import java.util.Map;

/**
 * SQL Transpiler interface.
 * @author Clément
 *
 */
public interface SQLTranspiler {
	
	/**
	 * Transpile a SQL request from brrridge format to JDBC format.
	 * Example of brrridge format : 'SELECT * from foo WHERE foo.bar IN (:someParam) AND foo.bidoo = :someOtherParam {{AND foo.shui = :anOptionalParam}}'
	 * And a Map containing 'someParam' -> List {'yoo', 'pi'}
	 * and 'someOtherParam' -> List {'woo'}
	 * 
	 * Example of JDBC transpilation result : 'SELECT * from foo WHERE foo.bar IN (?, ?) AND foo.bidoo = ?'
	 * With a parameter list { 'yoo', 'pi', 'woo' }.
	 * @param sqlRequest
	 * @param parameters
	 * @return
	 * @throws SQLTranspilationFailedException if the transpilation failed
	 */
	TranspilationResult transpile(String sqlRequest, Map<String, List<String>> parameters) throws SQLTranspilationFailedException;

}
