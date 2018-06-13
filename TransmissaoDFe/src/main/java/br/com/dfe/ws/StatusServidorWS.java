package br.com.dfe.ws;

import org.apache.axiom.om.OMElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dfe.service.URLService;
import br.inf.portalfiscal.www.nfe.wsdl.nfestatusservico4.NfeStatusServico4Stub;
import br.com.dfe.api.MetodoWS;

@Component("statusWS")
public class StatusServidorWS implements MetodoWS {
	
	@Autowired private URLService urlService;

	@Override
	public String call(OMElement elemento) throws Exception {
		NfeStatusServico4Stub.NfeDadosMsg dados = new NfeStatusServico4Stub.NfeDadosMsg();
		dados.setExtraElement(elemento);
		
		NfeStatusServico4Stub stub = new NfeStatusServico4Stub(getUrl());
		return stub.nfeStatusServicoNF(dados).getExtraElement().toString();
	}

	@Override
	public String getUrl() {
		return urlService.getUrlStatusServico();
	}
}