package br.com.dfe.ws;

import java.io.IOException;

import org.apache.axiom.om.OMElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dfe.service.URLService;
import br.inf.portalfiscal.www.nfe.wsdl.nferecepcaoevento4.RecepcaoEvento4Stub;
import br.com.dfe.api.MetodoWS;

@Component("eventoWS")
public class EventoWS implements MetodoWS {
	
	@Autowired private URLService urlService;

	@Override
	public String call(OMElement elemento) throws Exception {
		RecepcaoEvento4Stub.NfeDadosMsg dados = new RecepcaoEvento4Stub.NfeDadosMsg();
		dados.setExtraElement(elemento);
		
		RecepcaoEvento4Stub stub = new RecepcaoEvento4Stub(getUrl());
		return stub.nfeRecepcaoEvento(dados).getExtraElement().toString();
	}

	@Override
	public String getUrl() throws IOException {
		return urlService.getUrlEvento();
	}
}