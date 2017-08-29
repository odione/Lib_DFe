package br.com.transmissor.ws;

import org.apache.axiom.om.OMElement;

import br.com.transmissor.api.MetodoWS;
import br.inf.portalfiscal.www.nfe.wsdl.nferecepcaoevento4.RecepcaoEvento4Stub;

public class EventoWS implements MetodoWS {

	@Override
	public String call(OMElement elemento, String endPoint) throws Exception {
		RecepcaoEvento4Stub.NfeDadosMsg dados = new RecepcaoEvento4Stub.NfeDadosMsg();
		dados.setExtraElement(elemento);
		
		RecepcaoEvento4Stub stub = new RecepcaoEvento4Stub(endPoint);
		return stub.nfeRecepcaoEvento(dados).getExtraElement().toString();
	}
}