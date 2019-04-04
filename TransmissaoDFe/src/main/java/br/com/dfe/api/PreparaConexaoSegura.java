package br.com.dfe.api;

public interface PreparaConexaoSegura {

	void preparaConexaoSegura(String url) throws Exception;
	void setPathCacerts(String pathCacerts);
}