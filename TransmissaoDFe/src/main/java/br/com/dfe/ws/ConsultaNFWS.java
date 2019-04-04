package br.com.dfe.ws;

import org.apache.axiom.om.OMElement;

import br.com.dfe.api.MetodoWS;
import br.inf.portalfiscal.www.nfe.wsdl.nfeconsultaprotocolo4.NfeConsulta4Stub;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
public class ConsultaNFWS implements MetodoWS {

	@Override
	public String call(OMElement elemento, @NonNull String url) throws Exception {
		NfeConsulta4Stub.NfeDadosMsg dados = new NfeConsulta4Stub.NfeDadosMsg();
		dados.setExtraElement(elemento);
		
		NfeConsulta4Stub stub = new NfeConsulta4Stub(url);
		return stub.nfeConsultaNF(dados).getExtraElement().toString();
	}
}