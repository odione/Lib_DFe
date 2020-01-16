package br.com.dfe.ws;

import org.apache.axiom.om.OMElement;

import java.rmi.RemoteException;

public interface OperacaoWS {
    String getURL();
    String montaEnvio() throws Exception;
    String callWS(OMElement omElement) throws RemoteException;
}
