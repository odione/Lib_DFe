package br.com.dfe.api;

import org.apache.axiom.om.OMElement;

public interface MetodoWS {

	String call(OMElement elemento) throws Exception;
	String getUrl();
}