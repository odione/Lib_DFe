package br.com.dfe.ws;

import org.apache.axiom.om.OMElement;

import br.com.dfe.api.MetodoWS;
import br.inf.portalfiscal.www.nfe.wsdl.nfestatusservico4.NfeStatusServico4Stub;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
public class StatusServidorWS implements MetodoWS {

	@Override
	public String call(OMElement elemento, @NonNull String url) throws Exception {
		NfeStatusServico4Stub.NfeDadosMsg dados = new NfeStatusServico4Stub.NfeDadosMsg();
		dados.setExtraElement(elemento);

		NfeStatusServico4Stub stub = new NfeStatusServico4Stub(url);
		return stub.nfeStatusServicoNF(dados).getExtraElement().toString();
	}
}