package io.brrridge.transpiler;

import io.brrridge.transpiler.exception.InvalidInputToUnitException;
import io.brrridge.transpiler.exception.UnitCompilationFailedException;


public abstract class BaseCompilationUnit {
	private final StringBuffer requestInput;
	private final StringBuffer requestOutput;
	private boolean done;
	
	protected BaseCompilationUnit(StringBuffer request) {
		this.requestInput = request;
		this.requestOutput = new StringBuffer();
	}
	
	/**
	 * Compile the unit.
	 * 
	 * @return the unit as a compiled string.
	 * @throws UnitCompilationFailedException 
	 */
	public String compileUnit() throws UnitCompilationFailedException {
		try {
		this.checkBaseInput();
		while(!this.done) {
			if(requestInput.length() == 0) {
				this.parseEndOfBuffer();
				this.itsDone();
				break;
			}
			
			this.parseCurrentChar();
			
		}
		
		return requestOutput.toString();
		} catch (InvalidInputToUnitException e) {
			throw new UnitCompilationFailedException(e);
		}
	}
	
	
	
	
	private void checkBaseInput() throws InvalidInputToUnitException {
		// We ensure every input statement is at least two characters long
		if(this.requestInput == null || this.requestInput.length() < 2) {
			throw new InvalidInputToUnitException();
		}
		this.checkInput();
	}
	
	/**
	 * Check the input of the compilation unit. The compilation unit should notably check the first characters.
	 * @throws InvalidInputToUnitException
	 */
	protected abstract void checkInput() throws InvalidInputToUnitException;
	
	protected void itsDone() {
		this.done = true;
	}
	
	/**
	 * Parse the unit at the current character position.
	 * This method can use getRequestInput() and has the responsibility to call moveCompilationCursorUp() and pushToOutput();
	 * If some invalid character is encountered, it should throw an InvalidInputToUnitException.
	 * @return parsed result
	 * @throws InvalidInputToUnitException when the current char fails.
	 * @throws UnitCompilationFailedException when a sub compilation unit fails.
	 */
	protected abstract void parseCurrentChar() throws InvalidInputToUnitException, UnitCompilationFailedException;
	
	/**
	 * When the end of buffer is reaching during the compilation, this method is called.
	 * Compilation units should throw an exception if they were not supposed to meet an EOB.
	 * @throws InvalidInputToUnitException
	 */
	protected abstract void parseEndOfBuffer() throws InvalidInputToUnitException;

	/**
	 * Get the work in progress request.
	 * @return request
	 */
	protected StringBuffer getRequestInput() {
		return requestInput;
	}
	
	/**
	 * Move the compilation cursor one character away.
	 */
	protected void moveCompilationCursorUp() {
		requestInput.deleteCharAt(0);
	}
	
	/**
	 * Push some string to the output.
	 * @param toOutput
	 */
	protected void pushToOutput(String toOutput) {
		requestOutput.append(toOutput);
	}

}
