package io.brrridge.transpiler;

import io.brrridge.transpiler.exception.SQLTranspilationFailedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class SQLTranspilerTest { 

	@Test
	public void test() {
		Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(SQLTranspiler.class).to(SQLTranspilerImpl.class);
			}

			});
		
		SQLTranspiler transpiler = injector.getInstance(SQLTranspiler.class);
		
		List<TranspilationTestCase> testCases = this.provideTestCases();
		
		for(TranspilationTestCase testCase : testCases) {
			try {
				Assert.assertEquals(testCase.result, transpiler.transpile(testCase.request, testCase.parameters));
				if(testCase.shouldFail) {
					Assert.fail(testCase.toString() + " should have failed and should have not.");
				}
			} catch (SQLTranspilationFailedException e) {
				if(!testCase.shouldFail) {
					e.printStackTrace();
					Assert.fail(testCase.toString() + " have failed and should have not : "+e.toString());
				}
			}
		}
	}
	
	private List<TranspilationTestCase> provideTestCases() {
		List<TranspilationTestCase> result = new ArrayList<TranspilationTestCase>();
		
		TranspilationTestCase testCase1 = new TranspilationTestCase();
		testCase1.request = "SELECT * from foo WHERE foo.bar IN (:someParam) AND foo.bidoo = :someOtherParam{{ AND foo.shui = :anOptionalParam}}";
		testCase1.parameters.put("someParam", Arrays.asList("yoo", "pi"));
		testCase1.parameters.put("someOtherParam", Arrays.asList("woo"));
		testCase1.result = new TranspilationResult("SELECT * from foo WHERE foo.bar IN (?, ?) AND foo.bidoo = ?", Arrays.asList("yoo", "pi", "woo"));
		testCase1.shouldFail = false;
		result.add(testCase1);
		
		
		
		TranspilationTestCase testCase2 = new TranspilationTestCase();
		testCase2.request = "SELECT * from foo WHERE foo.bar IN (:someParam) AND foo.bidoo = :someOtherParam {{AND foo.shui = :anOptionalParam}}";
		testCase2.parameters.put("someParam", Arrays.asList("yoo", "pi"));
		testCase2.parameters.put("someOtherParam", Arrays.asList("woo"));
		testCase2.parameters.put("anOptionalParam", Arrays.asList("yahou"));
		testCase2.result = new TranspilationResult("SELECT * from foo WHERE foo.bar IN (?, ?) AND foo.bidoo = ? AND foo.shui = ?", Arrays.asList("yoo", "pi", "woo", "yahou"));
		testCase2.shouldFail = false;
		result.add(testCase2);
		
		return result;
	}

	private static class TranspilationTestCase {
		public String request;
		public Map<String, List<String>> parameters = new HashMap<String, List<String>>();
		public TranspilationResult result;
		public boolean shouldFail;
	}

}
