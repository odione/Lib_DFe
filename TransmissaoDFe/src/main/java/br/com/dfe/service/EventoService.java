package br.com.dfe.service;

import br.com.dfe.api.AssinaDocumento;
import br.com.dfe.api.EventoDFe;
import br.com.dfe.api.MetodoWS;
import br.com.dfe.api.Servico;
import br.com.dfe.api.XMLConverter;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.impl.AssinaXML;
import br.com.dfe.schema.cce.TEnvEvento;
import static br.com.dfe.utils.NFUtils.*;
import br.com.dfe.ws.EventoWS;

public class EventoService implements Servico {
	
	private MetodoWS metodo;
	private DadosEmissor dadosEmissor;
	private AssinaDocumento assinador;
	private XMLConverter xmlConverter;
	private String xmlAssinado;
	private EventoDFe evento;
	
	public EventoService(DadosEmissor dadosEmissor, EventoDFe evento, XMLConverter xmlConverter) {
		this.dadosEmissor = dadosEmissor;
		this.xmlConverter = xmlConverter;
		this.metodo = new EventoWS();
		this.assinador = new AssinaXML();
		this.evento = evento;
	}
	
	public EventoService assina(String xml) throws Exception {
		xmlAssinado = assinador.assinarEvento(xml, dadosEmissor.getCertificado(), dadosEmissor.getPrivateKey());
		dadosEmissor.setModelo(getModeloFromChave(getChaveFromEvento(xmlAssinado)));
		return this;
	}
	
	@Override
	public MetodoWS getMetodo() {
		return this.metodo;
	}

	@Override
	public Object getDados() {
		try {
			return xmlConverter.toObj(this.xmlAssinado, getClasseEnvEvento());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Class<?> getClasseEnvEvento() {
		if (evento.equals(EventoDFe.CANCELAMENTO)) {
			return br.com.dfe.schema.canc.TEnvEvento.class;
		} else if (evento.equals(EventoDFe.EPEC)) {
			return br.com.dfe.schema.generico.TEnvEvento.class;
		}
		return TEnvEvento.class;
	}
}