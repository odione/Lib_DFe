package br.com.dfe.ws;

import org.apache.axiom.om.OMElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dfe.service.URLService;
import br.inf.portalfiscal.www.nfe.wsdl.nferetautorizacao4.NfeRetAutorizacao4Stub;
import lombok.val;
import br.com.dfe.api.MetodoWS;

@Component("retornoAutorizacaoWS")
public class RetornoAutorizacaoWS implements MetodoWS {
	
	@Autowired
	private URLService url;

	@Override
	public String call(OMElement elemento) throws Exception {
		val dados = new NfeRetAutorizacao4Stub.NfeDadosMsg();
		dados.setExtraElement(elemento);
		
		val stub = new NfeRetAutorizacao4Stub(getUrl());
		return stub.nfeRetAutorizacaoLote(dados).getExtraElement().toString();
	}

	@Override
	public String getUrl() {
		return url.getUrlRetEnviaNF();
	}
}
