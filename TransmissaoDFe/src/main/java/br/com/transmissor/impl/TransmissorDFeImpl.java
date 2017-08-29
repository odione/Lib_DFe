package br.com.transmissor.impl;

import org.apache.axiom.om.OMElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dfe.schema.TRetConsStatServ;
import br.com.dfe.schema.TRetEnviNFe;
import br.com.dfe.schema.TRetInutNFe;
import br.com.dfe.schema.canc.TRetEnvEvento;
import br.com.dfe.schema.situacao.TRetConsSitNFe;
import br.com.transmissor.api.PreparaConexaoSegura;
import br.com.transmissor.api.Servico;
import br.com.transmissor.api.TransmissorDFe;
import br.com.transmissor.api.XMLConverter;
import br.com.transmissor.configuracao.DadosEmissor;
import br.com.transmissor.service.EventoService;
import br.com.transmissor.service.InutilizacaoService;
import br.com.transmissor.service.ConsultaNFService;
import br.com.transmissor.service.EnviaNFService;
import br.com.transmissor.service.StatusServidorService;
import br.com.transmissor.service.URLService;
import br.com.transmissor.utils.ConverterUtils;

@Component
public class TransmissorDFeImpl implements TransmissorDFe {
	
	private Servico servico;
	
	@Autowired
	private PreparaConexaoSegura prepara;
	
	@Autowired
	private URLService urlService;
	
	@Autowired
	private XMLConverter xmlConverter;
	
	@Autowired
	private DadosEmissor dadosEmissor;
	
	@Override
	public TRetConsStatServ statusServico() throws Exception {
		this.servico = new StatusServidorService(dadosEmissor);
		
		String xml = executaComando(urlService.getUrlStatusServico());
		return xmlConverter.toObj(xml, TRetConsStatServ.class);
	}

	@Override
	public TRetConsSitNFe consultarNF(String chave) throws Exception {
		this.servico = new ConsultaNFService(dadosEmissor).comChave(chave);
		
		String xml = executaComando(urlService.getUrlConsultaNF());
		return xmlConverter.toObj(xml, TRetConsSitNFe.class);
	}

	@Override
	public TRetEnviNFe enviarNF(String xmlTNFe) throws Exception {
		this.servico = new EnviaNFService(dadosEmissor, xmlConverter).comNFe(xmlTNFe);
		
		String xml = executaComando(urlService.getUrlEnviaNF());
		return xmlConverter.toObj(xml, TRetEnviNFe.class);
	}

	@Override
	public TRetEnvEvento cancelarNF(String xmlTEnvEvento) throws Exception {
		this.servico = new EventoService(dadosEmissor, true, xmlConverter).assina(xmlTEnvEvento);
		
		String retorno = executaComando(urlService.getUrlEvento());
		return xmlConverter.toObj(retorno, TRetEnvEvento.class);
	}

	@Override
	public br.com.dfe.schema.cce.TRetEnvEvento enviarCCe(String xmlTEnvEvento) throws Exception {
		this.servico = new EventoService(dadosEmissor, false, xmlConverter).assina(xmlTEnvEvento);
		
		String retorno = executaComando(urlService.getUrlEvento());
		return xmlConverter.toObj(retorno, br.com.dfe.schema.cce.TRetEnvEvento.class);
	}

	@Override
	public TRetInutNFe inutilizar(String xmlInutNFe) throws Exception {
		this.servico = new InutilizacaoService(dadosEmissor, xmlConverter).assina(xmlInutNFe);
		
		String retorno = executaComando(urlService.getUrlInutilizacao());
		return xmlConverter.toObj(retorno, TRetInutNFe.class);
	}
	
	@Override
	public Servico getServico() {
		return this.servico;
	}
	
	public String executaComando(String url) throws Exception {
		prepara.setUrl(url);
		prepara.preparaConexaoSegura();
		
		String dadosXML = xmlConverter.toString(servico.getDados(), false);
		OMElement elemento = ConverterUtils.toOMElement(dadosXML);
		return servico.getMetodo().call(elemento, url);
	}
}