package io.brrridge.transpiler.exception;

public class SQLTranspilationFailedException extends ExplainableException {

	public SQLTranspilationFailedException(UnitCompilationFailedException e) {
		super(e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8709349692198189815L;

}
