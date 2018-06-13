package br.com.dfe.impl;

import org.apache.axiom.om.OMElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import br.com.dfe.api.TransmissorDFe;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.schema.TRetConsReciNFe;
import br.com.dfe.schema.TRetConsSitNFe;
import br.com.dfe.schema.TRetConsStatServ;
import br.com.dfe.schema.TRetEnviNFe;
import br.com.dfe.schema.TRetInutNFe;
import br.com.dfe.schema.canc.TRetEnvEvento;
import br.com.dfe.service.HubServicos;
import lombok.Getter;
import lombok.val;
import lombok.extern.log4j.Log4j2;
import br.com.dfe.api.PreparaConexaoSegura;
import br.com.dfe.api.Servico;
import br.com.dfe.api.XMLConverter;
import br.com.dfe.utils.ConverterUtils;

@Component
@Log4j2
public class TransmissorDFeImpl implements TransmissorDFe {
	
	@Autowired private PreparaConexaoSegura prepara;
	@Autowired private XMLConverter xmlConverter;
	@Autowired private DadosEmissor dadosEmissor;
	@Autowired private HubServicos hub;
	
	@Getter private Servico servico;
	
	@Override
	public TRetConsStatServ statusServico() throws Exception {
		this.servico = hub.getStatus();
		return executaComando(TRetConsStatServ.class);
	}

	@Override
	public TRetConsSitNFe consultarNF(String chave) throws Exception {
		this.servico = hub.getConsulta().setChave(chave);
		return executaComando(TRetConsSitNFe.class);
	}

	@Override
	public TRetEnviNFe enviarNF(String xmlTNFe) throws Exception {
		this.servico = hub.getEnviaNF().setNFe(xmlTNFe);
		val retEnviNF = executaComando(TRetEnviNFe.class);
		
		if (dadosEmissor.isAsync() && retEnviNF.getCStat().equals("103")) {
			consultaRetornoAutorizacao(retEnviNF);
			this.servico = hub.getEnviaNF().setNFe(xmlTNFe);
		}
		
		return retEnviNF;
	}

	@Override
	public TRetEnvEvento cancelarNF(String xmlTEnvEvento) throws Exception {
		this.servico = hub.getEvento().assina(xmlTEnvEvento);
		return executaComando(TRetEnvEvento.class);
	}

	@Override
	public br.com.dfe.schema.cce.TRetEnvEvento enviarCCe(String xmlTEnvEvento) throws Exception {
		this.servico = hub.getEvento().assina(xmlTEnvEvento);
		return executaComando(br.com.dfe.schema.cce.TRetEnvEvento.class);
	}

	@Override
	public TRetInutNFe inutilizar(String xmlInutNFe) throws Exception {
		this.servico = hub.getInutilizacao().assina(xmlInutNFe);
		return executaComando(TRetInutNFe.class);
	}
	
	@Override
	public br.com.dfe.schema.generico.TRetEnvEvento enviarEPEC(String envEvento) throws Exception {
		this.servico = hub.getEvento().assina(envEvento);
		return executaComando(br.com.dfe.schema.generico.TRetEnvEvento.class);
	}
	
	private void consultaRetornoAutorizacao(final br.com.dfe.schema.TRetEnviNFe retEnviNF) throws Exception {
		this.servico = hub.getRetornoAutorizacao().setRetEnviNF(retEnviNF);
		
		int vezes = 0;
		while (vezes < 10) {
			vezes++;
			Thread.sleep(600);
			val retConsulta = executaComando(TRetConsReciNFe.class);
			
			if (!retConsulta.getCStat().equals("105")) {
				retEnviNF.setCStat(retConsulta.getCStat());
				retEnviNF.setXMotivo(retConsulta.getXMotivo());
				if (!CollectionUtils.isEmpty(retConsulta.getProtNFe())) {
					retEnviNF.setCStat(retConsulta.getProtNFe().get(0).getInfProt().getCStat());
					retEnviNF.setXMotivo(retConsulta.getProtNFe().get(0).getInfProt().getXMotivo());
					retEnviNF.setProtNFe(retConsulta.getProtNFe().get(0));
					vezes = 10;
				}
			}
		}
	}
	
	private <T> T executaComando(Class<T> clazz) throws Exception {
		preparaConexao();
		
		String dadosXML = servico.getDados();
		OMElement elemento = ConverterUtils.toOMElement(dadosXML);
		log.debug("OMElement: "+elemento.toString());
		String retorno = servico.getMetodo().call(elemento);
		log.info("Retorno WS: "+retorno);
		return xmlConverter.toObj(retorno, clazz);
	}
	
	private void preparaConexao() throws Exception {
		prepara.setUrl(servico.getMetodo().getUrl());
		prepara.setPathCacerts(dadosEmissor.getPathCacerts());
		prepara.preparaConexaoSegura();
	}
}