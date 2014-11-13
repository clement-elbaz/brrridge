package io.brrridge.transpiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import io.brrridge.transpiler.exception.InvalidInputToUnitException;
import io.brrridge.transpiler.exception.UnitCompilationFailedException;

/**
 * A parameter name compilation unit.
 * 
 * The effect of this one is multiple : when parsing ":someParam" it will :
 * - Get the value of "someParam" in the input parameters and push them in the output parameters
 * - Push some JDBC parameter in the transpiled SQL (aka ?, ?, ? for example)
 * 
 * @author Clément
 *
 */
public class ParamNameCompilationUnit extends BaseCompilationUnit {
	private final StringBuffer parameterNameBuffer;
	private final Map<String, List<String>> parametersInput;
	private final List<String> parametersOutput;
	private boolean began;

	/*package*/ ParamNameCompilationUnit(StringBuffer request, final Map<String, List<String>> parametersInput, final List<String> parametersOutput) {
		super(request);
		this.parameterNameBuffer = new StringBuffer();
		this.began = true;
		this.parametersInput = parametersInput;
		this.parametersOutput = parametersOutput;
	}

	/*
	 * (non-Javadoc)
	 * @see io.brrridge.transpiler.BaseCompilationUnit#checkInput()
	 */
	@Override
	protected void checkInput() throws InvalidInputToUnitException {
		if(this.getRequestInput().charAt(0) != ':') {
			throw new InvalidInputToUnitException();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see io.brrridge.transpiler.BaseCompilationUnit#parseCurrentChar()
	 */
	@Override
	protected void parseCurrentChar() throws InvalidInputToUnitException,
	UnitCompilationFailedException {
		String currentChar = String.valueOf(this.getRequestInput().charAt(0));

		if(began) {
			// We do not process first character (':')
			this.moveCompilationCursorUp();
		} else if(StringUtils.isAlphanumeric(currentChar)) {
			this.parameterNameBuffer.append(currentChar);
			this.moveCompilationCursorUp();
		} else {
			this.endParameterCompilation();
		}
		

		this.began = false;
	}

	/*
	 * (non-Javadoc)
	 * @see io.brrridge.transpiler.BaseCompilationUnit#parseEndOfBuffer()
	 */
	@Override
	protected void parseEndOfBuffer() throws InvalidInputToUnitException {
		// It is possible for a parameter name to end a buffer. We just declare the end of the compîlation.
		this.endParameterCompilation();
	}

	/**
	 * End the parameter compilation.
	 * @throws InvalidInputToUnitException
	 */
	private void endParameterCompilation() throws InvalidInputToUnitException {
		String parameterName = this.parameterNameBuffer.toString();

		if(!this.parametersInput.containsKey(parameterName)) {
			throw new InvalidInputToUnitException();
		}

		if(this.parametersInput.get(parameterName).isEmpty()) {
			throw new InvalidInputToUnitException();
		}

		for(String valueParameter : this.parametersInput.get(parameterName)) {
			this.parametersOutput.add(valueParameter);
		}

		List<String> paramSlots = new ArrayList<String>();
		for(int i =0 ; i < this.parametersInput.get(parameterName).size(); i++) {
			paramSlots.add("?");
		}
		
		this.pushToOutput(StringUtils.join(paramSlots, ", "));
		this.itsDone();
	}

}
