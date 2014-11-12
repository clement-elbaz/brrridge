package io.brrridge.transpiler;

import io.brrridge.transpiler.exception.InvalidInputToUnitException;

/**
 * A text compilation unit begin by " or ' and end with the same character.
 * Every character in between is processed as text directly from input to ouput without any special case.
 * Optional statements and name parameters are not processed inside a text statement.
 * @author Clément
 *
 */
public class TextCompilationUnit extends BaseCompilationUnit {
	private Character beginningComma;
	private boolean began;

	/* package */TextCompilationUnit(StringBuffer request) {
		super(request);
		this.began = true;
	}

	/*
	 * (non-Javadoc)
	 * @see io.brrridge.transpiler.BaseCompilationUnit#parseCurrentChar()
	 */
	@Override
	protected void parseCurrentChar() throws InvalidInputToUnitException {
		char currentChar = this.getRequestInput().charAt(0);

		if(this.beginningComma == currentChar && !began) {
			// If we encounter the same comma again, this is the end of this compilation
			this.itsDone();
		}

		// In every case we move the cursor up and we push to the output
		this.moveCompilationCursorUp();
		this.pushToOutput(String.valueOf(currentChar));
		
		this.began = false;
	}

	/*
	 * (non-Javadoc)
	 * @see io.brrridge.transpiler.BaseCompilationUnit#parseEndOfBuffer()
	 */
	@Override
	protected void parseEndOfBuffer() throws InvalidInputToUnitException {
		// Text unit must end with a comma, not and end of buffer. Therefore we throw an exception if it is reached.
		throw new InvalidInputToUnitException();
	}

	@Override
	protected void checkInput() throws InvalidInputToUnitException {
		char firstChar = this.getRequestInput().charAt(0);
	
		// We ensure the input start with either ' or "
		this.beginningComma = Character.valueOf(firstChar);
		if(this.beginningComma != '"' && this.beginningComma != '\'') {
			throw new InvalidInputToUnitException();
		}
	}

}
