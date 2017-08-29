package br.com.transmissor.service;

import br.com.dfe.schema.TNFe;
import br.com.transmissor.api.MetodoWS;
import br.com.transmissor.api.Servico;
import br.com.transmissor.api.XMLConverter;
import br.com.transmissor.builder.EnviaNFBuilder;
import br.com.transmissor.configuracao.DadosEmissor;
import br.com.transmissor.ws.EnviaNFWS;

public class EnviaNFService implements Servico {
	
	private MetodoWS metodoWS;
	private DadosEmissor dadosEmissor;
	private TNFe nfe;
	private XMLConverter xmlConverter;
	
	public EnviaNFService(DadosEmissor dadosEmissor, XMLConverter xmlConverter) {
		this.metodoWS = new EnviaNFWS();
		this.dadosEmissor = dadosEmissor;
		this.xmlConverter = xmlConverter;
	}
	
	public EnviaNFService comNFe(String tNfe) throws Exception {
		this.nfe = xmlConverter.toObj(tNfe, TNFe.class);
		return this;
	}

	@Override
	public MetodoWS getMetodo() {
		return this.metodoWS;
	}

	@Override
	public Object getDados() {
		return new EnviaNFBuilder(dadosEmissor, xmlConverter).comNFe(nfe).assina().build();
	}
}