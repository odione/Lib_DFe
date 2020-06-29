package br.com.dfe.api;

public interface AssinaDocumento {
	String assinarTNFe(String strXML) throws Exception;
	String assinarEvento(String strXML) throws Exception;
	String assinarInutilizada(String strXML) throws Exception;
}