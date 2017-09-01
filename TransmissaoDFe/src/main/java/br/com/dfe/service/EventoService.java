package br.com.dfe.service;

import br.com.dfe.api.AssinaDocumento;
import br.com.dfe.api.MetodoWS;
import br.com.dfe.api.Servico;
import br.com.dfe.api.XMLConverter;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.impl.AssinaXML;
import br.com.dfe.schema.cce.TEnvEvento;
import br.com.dfe.ws.EventoWS;

public class EventoService implements Servico {
	
	private MetodoWS metodo;
	private DadosEmissor dadosEmissor;
	private AssinaDocumento assinador;
	private XMLConverter xmlConverter;
	private String xmlAssinado;
	private boolean isCancelamento;
	
	public EventoService(DadosEmissor dadosEmissor, boolean isCancelamento, XMLConverter xmlConverter) {
		this.dadosEmissor = dadosEmissor;
		this.xmlConverter = xmlConverter;
		this.metodo = new EventoWS();
		this.assinador = new AssinaXML();
		this.isCancelamento = isCancelamento;
	}
	
	public EventoService assina(String xml) throws Exception {
		xmlAssinado = assinador.assinarEvento(xml, dadosEmissor.getCertificado(), dadosEmissor.getPrivateKey());
		return this;
	}
	
	@Override
	public MetodoWS getMetodo() {
		return this.metodo;
	}

	@Override
	public Object getDados() {
		try {
			Class<?> clazz = isCancelamento ? br.com.dfe.schema.canc.TEnvEvento.class : TEnvEvento.class;
			return xmlConverter.toObj(this.xmlAssinado, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}