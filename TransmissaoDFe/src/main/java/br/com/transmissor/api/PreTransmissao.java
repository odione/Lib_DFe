package br.com.transmissor.api;

public interface PreTransmissao {

	void verificaEnvioNF(String xml);
	void verificaEvento(String xml, Class<?> clazzEnvEvento);
	void verificaInutilizacao(String xmlInutNFe);
}