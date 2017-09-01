package br.com.dfe.service;

import br.com.dfe.api.MetodoWS;
import br.com.dfe.api.Servico;
import br.com.dfe.builder.ConsultaNFBuilder;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.ws.ConsultaNFWS;

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