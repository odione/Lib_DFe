package br.com.dfe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import br.com.dfe.api.AssinaDocumento;
import br.com.dfe.api.MetodoWS;
import br.com.dfe.api.Servico;
import br.com.dfe.api.XMLConverter;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.schema.TInutNFe;

@Service("inutilizacao")
public class InutilizacaoService implements Servico {
	
	@Autowired
	@Qualifier("inutilizacaoWS")
	private MetodoWS metodo;
	
	@Autowired private DadosEmissor dadosEmissor;
	@Autowired private AssinaDocumento assinador;
	@Autowired private XMLConverter xmlConverter;
	
	private String xmlAssinado;
	
	@Override
	public MetodoWS getMetodo() {
		return this.metodo;
	}

	@Override
	public String getDados() {
		return xmlAssinado;
	}
	
	public InutilizacaoService assina(String xml) throws Exception {
		xmlAssinado = assinador.assinarInutilizada(xml, dadosEmissor.getCertificado(), dadosEmissor.getPrivateKey());
		TInutNFe inutNFe = xmlConverter.toObj(xmlAssinado, TInutNFe.class);
		dadosEmissor.setModelo(inutNFe.getInfInut().getMod());
		return this;
	}
}