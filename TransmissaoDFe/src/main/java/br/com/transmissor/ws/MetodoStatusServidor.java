package br.com.transmissor.ws;

import org.apache.axiom.om.OMElement;

import br.com.transmissor.api.MetodoWS;
import br.inf.portalfiscal.www.nfe.wsdl.nfestatusservico4.NfeStatusServico4Stub;

public class MetodoStatusServidor implements MetodoWS {

	@Override
	public String call(OMElement elemento, String endPoint) throws Exception {
		NfeStatusServico4Stub.NfeDadosMsg dados = new NfeStatusServico4Stub.NfeDadosMsg();
		dados.setExtraElement(elemento);
		
		NfeStatusServico4Stub stub = new NfeStatusServico4Stub(endPoint);
		return stub.nfeStatusServicoNF(dados).getExtraElement().getText();
	}
}