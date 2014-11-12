package io.brrridge.transpiler.exception;

public class ExplainableException extends Exception {

	public ExplainableException(Exception e) {
		super(e);
	}

	public ExplainableException() {
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2482248369782974326L;

}
