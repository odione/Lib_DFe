package br.com.transmissor.ws;

import org.apache.axiom.om.OMElement;

import br.com.transmissor.api.MetodoWS;
import br.inf.portalfiscal.www.nfe.wsdl.nfeautorizacao4.NfeAutorizacao4Stub;

public class EnviaNFWS implements MetodoWS {

	@Override
	public String call(OMElement elemento, String endPoint) throws Exception {
		NfeAutorizacao4Stub.NfeDadosMsg dados = new NfeAutorizacao4Stub.NfeDadosMsg();
		dados.setExtraElement(elemento);
		
		NfeAutorizacao4Stub stup = new NfeAutorizacao4Stub(endPoint);
		return stup.nfeAutorizacaoLote(dados).getExtraElement().toString();
	}

}
