package io.brrridge.transpiler.exception;

public class UnitCompilationFailedException extends ExplainableException {

	public UnitCompilationFailedException(InvalidInputToUnitException e) {
		super(e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1723769129998132069L;

}
