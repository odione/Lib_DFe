package br.com.dfe.ws;

import org.apache.axiom.om.OMElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dfe.service.URLService;
import br.inf.portalfiscal.www.nfe.wsdl.nfeconsultaprotocolo4.NfeConsulta4Stub;
import br.com.dfe.api.MetodoWS;

@Component("consultaWS")
public class ConsultaNFWS implements MetodoWS {

	@Autowired private URLService urlService;
	
	@Override
	public String call(OMElement elemento) throws Exception {
		NfeConsulta4Stub.NfeDadosMsg dados = new NfeConsulta4Stub.NfeDadosMsg();
		dados.setExtraElement(elemento);
		
		NfeConsulta4Stub stub = new NfeConsulta4Stub(getUrl());
		return stub.nfeConsultaNF(dados).getExtraElement().toString();
	}

	@Override
	public String getUrl() {
		return urlService.getUrlConsultaNF();
	}
}