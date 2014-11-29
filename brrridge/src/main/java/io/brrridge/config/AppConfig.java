package io.brrridge.config;

import io.brrridge.servlet.BrrridgeServlet;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;


public class AppConfig extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new ServletModule(){

			/*
			 * (non-Javadoc)
			 * @see com.google.inject.servlet.ServletModule#configureServlets()
			 */
			@Override
			protected void configureServlets() {
				serve("/*").with(BrrridgeServlet.class);
			}
			
		}, new AbstractModule(){

			@Override
			protected void configure() {
				bind(BrrridgeServlet.class).asEagerSingleton();
				
			}});
	}

}
