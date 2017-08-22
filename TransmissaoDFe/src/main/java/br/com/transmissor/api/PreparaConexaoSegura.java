package br.com.transmissor.api;

import java.net.MalformedURLException;

public interface PreparaConexaoSegura {

	void preparaConexaoSegura() throws Exception;
	void setUrl(String url) throws MalformedURLException;
	void setPathCacerts(String pathCacerts);
}