package br.com.dfe.ws;

import org.apache.axiom.om.OMElement;

import br.com.dfe.api.MetodoWS;
import br.inf.portalfiscal.www.nfe.wsdl.nfeconsultaprotocolo4.NfeConsulta4Stub;

public class ConsultaNFWS implements MetodoWS {

	@Override
	public String call(OMElement elemento, String endPoint) throws Exception {
		NfeConsulta4Stub.NfeDadosMsg dados = new NfeConsulta4Stub.NfeDadosMsg();
		dados.setExtraElement(elemento);
		
		NfeConsulta4Stub stub = new NfeConsulta4Stub(endPoint);
		return stub.nfeConsultaNF(dados).getExtraElement().toString();
	}
}