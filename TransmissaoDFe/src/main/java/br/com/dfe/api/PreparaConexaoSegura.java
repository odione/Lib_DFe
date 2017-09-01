package br.com.dfe.api;

import java.net.MalformedURLException;

public interface PreparaConexaoSegura {

	void preparaConexaoSegura() throws Exception;
	void setUrl(String url) throws MalformedURLException;
	void setPathCacerts(String pathCacerts);
}