package io.brrridge.transpiler;

import java.util.List;
import java.util.Map;

import io.brrridge.transpiler.exception.InvalidInputToUnitException;
import io.brrridge.transpiler.exception.UnitCompilationFailedException;

/**
 * A SQL compilation unit.
 * 
 * Everything goes through from input to output unless we encounter a special case (text, optional unit, parameter name).
 * 
 * @author Clément
 *
 */
public class SQLCompilationUnit extends BaseCompilationUnit {
	private final Map<String, List<String>> parametersInput;
	private final List<String> parametersOutput;

	/*package*/ SQLCompilationUnit(StringBuffer request, final Map<String, List<String>> parametersInput, final List<String> parametersOutput) {
		super(request);
		this.parametersInput = parametersInput;
		this.parametersOutput = parametersOutput;
	}

	/*
	 * (non-Javadoc)
	 * @see io.brrridge.transpiler.BaseCompilationUnit#checkInput()
	 */
	@Override
	protected void checkInput() throws InvalidInputToUnitException {
		// No particular check for a SQL statement.
		return;

	}

	/*
	 * (non-Javadoc)
	 * @see io.brrridge.transpiler.BaseCompilationUnit#parseCurrentChar()
	 */
	@Override
	protected void parseCurrentChar() throws InvalidInputToUnitException, UnitCompilationFailedException {
		char currentChar = this.getRequestInput().charAt(0);
		
		switch(currentChar) {
		case '\'':
		case '"':
			new TextCompilationUnit(this.getRequestInput()).compileUnit();
			break;
		case '{':
			if("{{".equals(this.getRequestInput().substring(0, 2))) {
				new OptionalCompilationUnit(this.getRequestInput(), this.parametersInput, this.parametersOutput).compileUnit();
				break;
			}
		case ':':
			new ParamNameCompilationUnit(this.getRequestInput(), this.parametersInput, this.parametersOutput).compileUnit();
			break;
		default:
			this.pushToOutput(String.valueOf(currentChar));
			this.moveCompilationCursorUp();
			break;
		}
		

	}

	/*
	 * (non-Javadoc)
	 * @see io.brrridge.transpiler.BaseCompilationUnit#parseEndOfBuffer()
	 */
	@Override
	protected void parseEndOfBuffer() throws InvalidInputToUnitException {
		// A SQL statement can be the end of the buffer.
		return;

	}

}
