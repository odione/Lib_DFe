package br.com.dfe.api;

public interface PreTransmissao {
	void verificaEnvioNF(String xml) throws Exception;
	void verificaEvento(String xml, Class<?> clazzEnvEvento) throws Exception;
	void verificaInutilizacao(String xmlInutNFe) throws Exception;
}