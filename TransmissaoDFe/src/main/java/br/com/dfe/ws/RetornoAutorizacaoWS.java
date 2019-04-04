package br.com.dfe.ws;

import org.apache.axiom.om.OMElement;

import br.com.dfe.api.MetodoWS;
import br.inf.portalfiscal.www.nfe.wsdl.nferetautorizacao4.NfeRetAutorizacao4Stub;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.val;

@NoArgsConstructor
public class RetornoAutorizacaoWS implements MetodoWS {
	
	@Override
	public String call(OMElement elemento, @NonNull String url) throws Exception {
		val dados = new NfeRetAutorizacao4Stub.NfeDadosMsg();
		dados.setExtraElement(elemento);
		
		val stub = new NfeRetAutorizacao4Stub(url);
		return stub.nfeRetAutorizacaoLote(dados).getExtraElement().toString();
	}
}
