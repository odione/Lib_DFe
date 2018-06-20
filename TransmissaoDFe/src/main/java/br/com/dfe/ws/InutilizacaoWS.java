package br.com.dfe.ws;

import java.io.IOException;

import org.apache.axiom.om.OMElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dfe.service.URLService;
import br.inf.portalfiscal.www.nfe.wsdl.nfeinutilizacao4.NfeInutilizacao4Stub;
import br.com.dfe.api.MetodoWS;

@Component("inutilizacaoWS")
public class InutilizacaoWS implements MetodoWS {
	
	@Autowired private URLService urlService;

	@Override
	public String call(OMElement elemento) throws Exception {
		NfeInutilizacao4Stub.NfeDadosMsg dados = new NfeInutilizacao4Stub.NfeDadosMsg();
		dados.setExtraElement(elemento);
		
		NfeInutilizacao4Stub stub = new NfeInutilizacao4Stub(getUrl());
		return stub.nfeInutilizacaoNF(dados).getExtraElement().toString();
	}

	@Override
	public String getUrl() throws IOException {
		return urlService.getUrlInutilizacao();
	}
}