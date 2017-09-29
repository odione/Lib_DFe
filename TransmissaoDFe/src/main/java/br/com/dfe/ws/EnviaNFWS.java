package br.com.dfe.ws;

import org.apache.axiom.om.OMElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dfe.api.MetodoWS;
import br.com.dfe.service.URLService;
import br.inf.portalfiscal.www.nfe.wsdl.nfeautorizacao4.NfeAutorizacao4Stub;

@Component("enviaNFWS")
public class EnviaNFWS implements MetodoWS {
	
	@Autowired private URLService urlService;

	@Override
	public String call(OMElement elemento) throws Exception {
		NfeAutorizacao4Stub.NfeDadosMsg dados = new NfeAutorizacao4Stub.NfeDadosMsg();
		dados.setExtraElement(elemento);
		
		NfeAutorizacao4Stub stup = new NfeAutorizacao4Stub(getUrl());
		return stup.nfeAutorizacaoLote(dados).getExtraElement().toString();
	}

	@Override
	public String getUrl() {
		return urlService.getUrlEnviaNF();
	}
}