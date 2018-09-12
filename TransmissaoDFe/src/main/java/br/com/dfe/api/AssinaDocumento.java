package br.com.dfe.api;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public interface AssinaDocumento {

	public String assinarEnvNFe(String strXML, X509Certificate cert, PrivateKey key) throws Exception;
	
	public String assinarEvento(String strXML, X509Certificate cert, PrivateKey key) throws Exception;

	public String assinarInutilizada(String strXML, X509Certificate cert, PrivateKey key) throws Exception;
}