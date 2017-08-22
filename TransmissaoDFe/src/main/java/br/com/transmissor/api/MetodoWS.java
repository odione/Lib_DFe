package br.com.transmissor.api;

import org.apache.axiom.om.OMElement;

public interface MetodoWS {

	String call(OMElement elemento, String endPoint) throws Exception;
}