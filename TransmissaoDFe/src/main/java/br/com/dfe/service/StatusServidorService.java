package br.com.dfe.service;

import br.com.dfe.api.MetodoWS;
import br.com.dfe.api.Servico;
import br.com.dfe.builder.StatusServidorBuilder;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.ws.StatusServidorWS;

public class StatusServidorService implements Servico {

	private MetodoWS metodoWS;
	private DadosEmissor dadosEmissor;
	
	public StatusServidorService(DadosEmissor dadosEmissor) {
		this.dadosEmissor = dadosEmissor;
		this.metodoWS = new StatusServidorWS();
	}
	
	@Override
	public MetodoWS getMetodo() {
		return metodoWS;
	}

	@Override
	public Object getDados() {
		return new StatusServidorBuilder(dadosEmissor).build();
	}
}