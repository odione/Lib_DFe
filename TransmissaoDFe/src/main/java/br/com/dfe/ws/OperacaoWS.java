package br.com.dfe.ws;

import org.apache.axiom.om.OMElement;

public interface OperacaoWS {
    String getModelo();

    String getURL();

    String montaEnvio() throws Exception;

    String callWS(OMElement omElement) throws Exception;

    String enviaParaIntegrador(OMElement omElement) throws Exception;
}
