package io.brrridge.transpiler;

import java.util.List;
import java.util.Map;

import io.brrridge.transpiler.exception.InvalidInputToUnitException;
import io.brrridge.transpiler.exception.UnitCompilationFailedException;

/**
 * Parse an optional statement, encapsulated in {{ statement }}.
 * If the inner statement fails, the optional statement is skipped.
 * @author Clément
 *
 */
public class OptionalCompilationUnit extends BaseCompilationUnit {
	/** Optional request output. Will be merged to the output if child compilation do not fail. */
	private final StringBuffer optionalRequestOutput;
	private int openedStatements;
	private final Map<String, List<String>> parametersInput;
	private final List<String> parametersOutput;

	/*package*/ OptionalCompilationUnit(StringBuffer request, final Map<String, List<String>> parametersInput, final List<String> parametersOutput) {
		super(request);
		this.optionalRequestOutput = new StringBuffer();
		this.openedStatements = 0;
		this.parametersInput = parametersInput;
		this.parametersOutput = parametersOutput;
	}

	@Override
	protected void parseCurrentChar() throws InvalidInputToUnitException {
		String nextTwoChars = this.getRequestInput().substring(0, 2);

		switch(nextTwoChars) {
		case "{{":
			openedStatements++;
			// We skip two characters
			this.moveCompilationCursorUp();
			this.moveCompilationCursorUp();
			break;
		case "}}":
			openedStatements--;
			// We skip two characters
			this.moveCompilationCursorUp();
			this.moveCompilationCursorUp();
			if(openedStatements == 0) {
				this.compileOptionalStatement();
				this.itsDone();
			}
			break;
		default:
			this.optionalRequestOutput.append(this.getRequestInput().charAt(0));
			// We skip one characters
			this.moveCompilationCursorUp();
			break;
		}	

	}

	private void compileOptionalStatement() {
		try {
			String optionalStatement = new SQLCompilationUnit(this.optionalRequestOutput, this.parametersInput, this.parametersOutput).compileUnit();
			this.pushToOutput(optionalStatement);
		} catch (UnitCompilationFailedException e) {
			// We fail silently - the statement is optional !
		}
		
	}

	@Override
	protected void parseEndOfBuffer() throws InvalidInputToUnitException {
		// End of buffer is invalid for an optional compilation.
		throw new InvalidInputToUnitException();
	}

	@Override
	protected void checkInput() throws InvalidInputToUnitException {
		String firstTwoChars = this.getRequestInput().substring(0, 2);
		
		// We ensure the first two chars are {{
		if(!"{{".equals(firstTwoChars)) {
			throw new InvalidInputToUnitException();
		}
		
	}

}
