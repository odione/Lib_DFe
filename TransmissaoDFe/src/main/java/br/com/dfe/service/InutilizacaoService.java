package br.com.dfe.service;

import br.com.dfe.api.AssinaDocumento;
import br.com.dfe.api.MetodoWS;
import br.com.dfe.api.Servico;
import br.com.dfe.api.XMLConverter;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.impl.AssinaXML;
import br.com.dfe.schema.TInutNFe;
import br.com.dfe.ws.InutilizacaoWS;

public class InutilizacaoService implements Servico {
	
	private MetodoWS metodo;
	private DadosEmissor dadosEmissor;
	private AssinaDocumento assinador;
	private XMLConverter xmlConverter;
	private String xmlAssinado;
	
	public InutilizacaoService(DadosEmissor dadosEmissor, XMLConverter xmlConverter) {
		this.metodo = new InutilizacaoWS();
		this.dadosEmissor = dadosEmissor;
		this.xmlConverter = xmlConverter;
		this.assinador = new AssinaXML();
	}
	
	public InutilizacaoService assina(String xml) throws Exception {
		xmlAssinado = assinador.assinarInutilizada(xml, dadosEmissor.getCertificado(), dadosEmissor.getPrivateKey());
		return this;
	}

	@Override
	public MetodoWS getMetodo() {
		return this.metodo;
	}

	@Override
	public Object getDados() {
		try {
			return xmlConverter.toObj(xmlAssinado, TInutNFe.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}