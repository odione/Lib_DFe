package br.com.dfe.ws;

import org.apache.axiom.om.OMElement;

import br.com.dfe.api.MetodoWS;
import br.inf.portalfiscal.www.nfe.wsdl.nferecepcaoevento4.RecepcaoEvento4Stub;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
public class EventoWS implements MetodoWS {

	@Override
	public String call(OMElement elemento, @NonNull String url) throws Exception {
		RecepcaoEvento4Stub.NfeDadosMsg dados = new RecepcaoEvento4Stub.NfeDadosMsg();
		dados.setExtraElement(elemento);

		RecepcaoEvento4Stub stub = new RecepcaoEvento4Stub(url);
		return stub.nfeRecepcaoEvento(dados).getExtraElement().toString();
	}
}