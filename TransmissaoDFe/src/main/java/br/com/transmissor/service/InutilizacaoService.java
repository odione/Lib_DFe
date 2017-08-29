package br.com.transmissor.service;

import br.com.dfe.schema.TInutNFe;
import br.com.transmissor.api.AssinaDocumento;
import br.com.transmissor.api.MetodoWS;
import br.com.transmissor.api.Servico;
import br.com.transmissor.api.XMLConverter;
import br.com.transmissor.configuracao.DadosEmissor;
import br.com.transmissor.impl.AssinaXML;
import br.com.transmissor.ws.InutilizacaoWS;

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