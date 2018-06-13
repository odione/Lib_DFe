package br.com.dfe.service;

import static br.com.dfe.utils.NFUtils.getChaveFromEvento;
import static br.com.dfe.utils.NFUtils.getModeloFromChave;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.api.AssinaDocumento;
import br.com.dfe.api.MetodoWS;
import br.com.dfe.api.Servico;

@Service("evento")
public class EventoService implements Servico {
	
	@Autowired private DadosEmissor dadosEmissor;
	@Autowired private AssinaDocumento assinador;

	@Autowired
	@Qualifier("eventoWS")
	private MetodoWS metodo;
	
	private String xmlAssinado;
	
	@Override
	public MetodoWS getMetodo() {
		return this.metodo;
	}
	
	@Override
	public String getDados() {
		return xmlAssinado;
	}
	
	public EventoService assina(String xmlEnvEvento) throws Exception {
		xmlAssinado = assinador.assinarEvento(xmlEnvEvento, dadosEmissor.getCertificado(), dadosEmissor.getPrivateKey());
		dadosEmissor.setModelo(getModeloFromChave(getChaveFromEvento(xmlAssinado)));
		return this;
	}
}