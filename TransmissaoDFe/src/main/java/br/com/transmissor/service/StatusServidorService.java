package br.com.transmissor.service;

import br.com.transmissor.api.MetodoWS;
import br.com.transmissor.api.Servico;
import br.com.transmissor.builder.StatusServidorBuilder;
import br.com.transmissor.configuracao.DadosEmissor;
import br.com.transmissor.ws.StatusServidorWS;

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