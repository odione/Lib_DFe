package br.com.transmissor.service;

import br.com.transmissor.api.MetodoWS;
import br.com.transmissor.api.Servico;
import br.com.transmissor.builder.ConsultaNFBuilder;
import br.com.transmissor.configuracao.DadosEmissor;
import br.com.transmissor.ws.ConsultaNFWS;

public class ConsultaNFService implements Servico {
	
	private DadosEmissor dadosEmissor;
	private MetodoWS metodoWS;
	private String chave;
	
	public ConsultaNFService(DadosEmissor dadosEmissor) {
		this.dadosEmissor = dadosEmissor;
		this.metodoWS = new ConsultaNFWS();
	}
	
	public ConsultaNFService comChave(String chave) {
		this.chave = chave;
		return this;
	}
	
	@Override
	public MetodoWS getMetodo() {
		return this.metodoWS;
	}

	@Override
	public Object getDados() {
		return new ConsultaNFBuilder(dadosEmissor).comChave(chave).build();
	}
}