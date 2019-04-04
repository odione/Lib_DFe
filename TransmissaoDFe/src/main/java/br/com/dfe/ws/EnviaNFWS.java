package br.com.dfe.ws;

import org.apache.axiom.om.OMElement;

import br.com.dfe.api.MetodoWS;
import br.inf.portalfiscal.www.nfe.wsdl.nfeautorizacao4.NfeAutorizacao4Stub;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
public class EnviaNFWS implements MetodoWS {

	@Override
	public String call(OMElement elemento, @NonNull String url) throws Exception {
		NfeAutorizacao4Stub.NfeDadosMsg dados = new NfeAutorizacao4Stub.NfeDadosMsg();
		dados.setExtraElement(elemento);

		NfeAutorizacao4Stub stub = new NfeAutorizacao4Stub(url);
		return stub.nfeAutorizacaoLote(dados).getExtraElement().toString();
	}
}