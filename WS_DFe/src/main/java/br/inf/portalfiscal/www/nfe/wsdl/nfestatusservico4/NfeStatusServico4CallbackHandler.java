/**
 * NfeStatusServico4CallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.8  Built on : May 19, 2018 (07:06:11 BST)
 */
package br.inf.portalfiscal.www.nfe.wsdl.nfestatusservico4;


/**
 *  NfeStatusServico4CallbackHandler Callback class, Users can extend this class and implement
 *  their own receiveResult and receiveError methods.
 */
public abstract class NfeStatusServico4CallbackHandler {
    protected Object clientData;

    /**
     * User can pass in any object that needs to be accessed once the NonBlocking
     * Web service call is finished and appropriate method of this CallBack is called.
     * @param clientData Object mechanism by which the user can pass in user data
     * that will be avilable at the time this callback is called.
     */
    public NfeStatusServico4CallbackHandler(Object clientData) {
        this.clientData = clientData;
    }

    /**
     * Please use this constructor if you don't want to set any clientData
     */
    public NfeStatusServico4CallbackHandler() {
        this.clientData = null;
    }

    /**
     * Get the client data
     */
    public Object getClientData() {
        return clientData;
    }

    /**
     * auto generated Axis2 call back method for nfeStatusServicoNF method
     * override this method for handling normal response from nfeStatusServicoNF operation
     */
    public void receiveResultnfeStatusServicoNF(
        br.inf.portalfiscal.www.nfe.wsdl.nfestatusservico4.NfeStatusServico4Stub.NfeResultMsg result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from nfeStatusServicoNF operation
     */
    public void receiveErrornfeStatusServicoNF(java.lang.Exception e) {
    }
}
