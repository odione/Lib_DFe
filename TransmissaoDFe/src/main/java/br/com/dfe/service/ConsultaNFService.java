package br.com.dfe.service;

import static br.com.dfe.utils.NFUtils.getModeloFromChave;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import br.com.dfe.api.MetodoWS;
import br.com.dfe.api.Servico;
import br.com.dfe.api.XMLConverter;
import br.com.dfe.builder.ConsultaNFBuilder;
import br.com.dfe.configuracao.DadosEmissor;

@Service("consultaNFService")
public class ConsultaNFService implements Servico {
	
	@Autowired private DadosEmissor dadosEmissor;
	@Autowired private ConsultaNFBuilder builder;
	@Autowired private XMLConverter xmlConverter;
	
	@Autowired
	@Qualifier("consultaWS")
	private MetodoWS metodoWS;
	
	public ConsultaNFService setChave(String chave) {
		builder.setChave(chave);
		this.dadosEmissor.setModelo(getModeloFromChave(chave));
		return this;
	}

	@Override
	public String getDados() throws Exception {
		return xmlConverter.toString(builder.build(), false);
	}

	@Override
	public MetodoWS getMetodo() {
		return this.metodoWS;
	}
}