package br.com.transmissor.ws;

import org.apache.axiom.om.OMElement;

import br.com.transmissor.api.MetodoWS;
import br.inf.portalfiscal.www.nfe.wsdl.nfeinutilizacao4.NfeInutilizacao4Stub;

public class InutilizacaoWS implements MetodoWS {

	@Override
	public String call(OMElement elemento, String endPoint) throws Exception {
		NfeInutilizacao4Stub.NfeDadosMsg dados = new NfeInutilizacao4Stub.NfeDadosMsg();
		dados.setExtraElement(elemento);
		
		NfeInutilizacao4Stub stub = new NfeInutilizacao4Stub(endPoint);
		return stub.nfeInutilizacaoNF(dados).getExtraElement().toString();
	}
}