package br.com.dfe.api;

public interface Servico {

	MetodoWS getMetodo();
	String getDados() throws Exception;
}