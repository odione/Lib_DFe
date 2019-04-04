package br.com.dfe.ws;

import org.apache.axiom.om.OMElement;

import br.com.dfe.api.MetodoWS;
import br.inf.portalfiscal.www.nfe.wsdl.nfeinutilizacao4.NfeInutilizacao4Stub;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
public class InutilizacaoWS implements MetodoWS {

	@Override
	public String call(OMElement elemento, @NonNull String url) throws Exception {
		NfeInutilizacao4Stub.NfeDadosMsg dados = new NfeInutilizacao4Stub.NfeDadosMsg();
		dados.setExtraElement(elemento);

		NfeInutilizacao4Stub stub = new NfeInutilizacao4Stub(url);
		return stub.nfeInutilizacaoNF(dados).getExtraElement().toString();
	}
}