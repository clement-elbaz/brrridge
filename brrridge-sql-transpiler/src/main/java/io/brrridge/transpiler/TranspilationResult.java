package io.brrridge.transpiler;

import java.util.List;

/**
 * A SQL transpilation result.
 * 
 * @author Clément
 *
 */
public class TranspilationResult {
	/** A transpiled, JDBC ready request. */
	private final String transpiledRequest;
	/** An ordered list of parameters for JDBC. */
	private final List<String> parameterList;
	
	/**
	 * Constructor. 
	 * @param transpiledRequest transpiled request
	 * @param parameterList parameter list
	 */
	TranspilationResult(final String transpiledRequest, final List<String> parameterList) {
		this.transpiledRequest = transpiledRequest;
		this.parameterList = parameterList;
	}

	/**
	 * Getter.
	 * 
	 * @return transpiled request
	 */
	public String getTranspiledRequest() {
		return transpiledRequest;
	}

	/**
	 * Getter.
	 * 
	 * @return parameter list
	 */
	public List<String> getParameterList() {
		return parameterList;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TranspilerResult [transpiledRequest=" + transpiledRequest
				+ ", parameterList=" + parameterList + "]";
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((parameterList == null) ? 0 : parameterList.hashCode());
		result = prime
				* result
				+ ((transpiledRequest == null) ? 0 : transpiledRequest
						.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TranspilationResult other = (TranspilationResult) obj;
		if (parameterList == null) {
			if (other.parameterList != null)
				return false;
		} else if (!parameterList.equals(other.parameterList))
			return false;
		if (transpiledRequest == null) {
			if (other.transpiledRequest != null)
				return false;
		} else if (!transpiledRequest.equals(other.transpiledRequest))
			return false;
		return true;
	}

}
