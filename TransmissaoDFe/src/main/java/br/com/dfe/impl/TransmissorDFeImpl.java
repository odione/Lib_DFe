package br.com.dfe.impl;

import javax.annotation.PostConstruct;

import org.apache.axiom.om.OMElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import br.com.dfe.api.PreparaConexaoSegura;
import br.com.dfe.api.Servico;
import br.com.dfe.api.TipoEmissao;
import br.com.dfe.api.TransmissorDFe;
import br.com.dfe.api.XMLConverter;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.configuracao.DadosRequisicao;
import br.com.dfe.configuracao.DadosResposta;
import br.com.dfe.schema.TRetConsReciNFe;
import br.com.dfe.schema.TRetConsSitNFe;
import br.com.dfe.schema.TRetConsStatServ;
import br.com.dfe.schema.TRetEnviNFe;
import br.com.dfe.schema.TRetInutNFe;
import br.com.dfe.schema.canc.TRetEnvEvento;
import br.com.dfe.service.HubServicos;
import br.com.dfe.service.URLService;
import br.com.dfe.utils.ConverterUtils;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class TransmissorDFeImpl implements TransmissorDFe {
	
	private URLService urlService;
	
	@Autowired private PreparaConexaoSegura prepara;
	@Autowired private XMLConverter xmlConverter;
	@Autowired private DadosEmissor dadosEmissor;
	@Autowired private HubServicos hub;
	
	@PostConstruct
	public void init() {
		this.urlService = new URLService();
	}
	
	@Override
	public DadosResposta<TRetConsStatServ> statusServico(int ambiente, String modelo, TipoEmissao tipoEmissao) throws Exception {
		Servico service = hub.getStatus();
		DadosRequisicao dados = service.createDados("");
		dados.setAmbiente(ambiente);
		dados.setModelo(modelo);
		dados.setTipoEmissao(tipoEmissao);
		dados.setUrl(urlService::getUrlStatusServico);
		
		return executaComando(dados, TRetConsStatServ.class, service);
	}

	@Override
	public DadosResposta<TRetConsSitNFe> consultarNF(String chave) throws Exception {
		Servico service = hub.getConsulta();
		DadosRequisicao dados = service.createDados(chave);
		dados.setUrl(urlService::getUrlConsultaNF);
		return executaComando(dados, TRetConsSitNFe.class, service);
	}

	@Override
	public DadosResposta<TRetEnviNFe> enviarNF(String xmlTNFe) throws Exception {
		Servico service = hub.getEnviaNF();
		DadosRequisicao dados = service.createDados(xmlTNFe);
		dados.setUrl(urlService::getUrlEnviaNF);
		
		DadosResposta<TRetEnviNFe> resposta = executaComando(dados, TRetEnviNFe.class, service);
		
		if (dadosEmissor.isAsync() && resposta.getRetorno().getCStat().equals("103")) {
			consultaRetornoAutorizacao(resposta.getRetorno(), dados);
		}
		
		return resposta;
	}

	@Override
	public DadosResposta<TRetEnvEvento> cancelarNF(String xmlTEnvEvento) throws Exception {
		Servico service = hub.getEvento();
		DadosRequisicao dados = service.createDados(xmlTEnvEvento);
		dados.setUrl(urlService::getUrlEvento);
		return executaComando(dados, TRetEnvEvento.class, service);
	}

	@Override
	public DadosResposta<br.com.dfe.schema.cce.TRetEnvEvento> enviarCCe(String xmlTEnvEvento) throws Exception {
		Servico service = hub.getEvento();
		DadosRequisicao dados = service.createDados(xmlTEnvEvento);
		dados.setUrl(urlService::getUrlEvento);
		return executaComando(dados, br.com.dfe.schema.cce.TRetEnvEvento.class, service);
	}

	@Override
	public DadosResposta<TRetInutNFe> inutilizar(String xmlInutNFe) throws Exception {
		Servico service = hub.getInutilizacao();
		DadosRequisicao dados = service.createDados(xmlInutNFe);
		dados.setUrl(urlService::getUrlInutilizacao);
		return executaComando(dados, TRetInutNFe.class, service);
	}
	
	@Override
	public DadosResposta<br.com.dfe.schema.generico.TRetEnvEvento> enviarEPEC(String envEvento) throws Exception {
		Servico service = hub.getInutilizacao();
		DadosRequisicao dados = service.createDados(envEvento);
		dados.setUrl(urlService::getUrlEvento);
		return executaComando(dados, br.com.dfe.schema.generico.TRetEnvEvento.class, service);
	}
	
	private void consultaRetornoAutorizacao(final br.com.dfe.schema.TRetEnviNFe retEnviNF, DadosRequisicao dados) throws Exception {
		Servico service = hub.getRetornoAutorizacao();
		dados.setRawFile(xmlConverter.toString(retEnviNF, false));
		dados.setUrl(urlService::getUrlRetEnviaNF);
		
		int vezes = 0;
		while (vezes < 10) {
			vezes++;
			Thread.sleep(600);
			TRetConsReciNFe retConsulta = executaComando(dados, TRetConsReciNFe.class, service).getRetorno();
			
			retEnviNF.setCStat(retConsulta.getCStat());
			retEnviNF.setXMotivo(retConsulta.getXMotivo());
			if (!retConsulta.getCStat().equals("105")) {
				if (!CollectionUtils.isEmpty(retConsulta.getProtNFe())) {
					retEnviNF.setCStat(retConsulta.getProtNFe().get(0).getInfProt().getCStat());
					retEnviNF.setXMotivo(retConsulta.getProtNFe().get(0).getInfProt().getXMotivo());
					retEnviNF.setProtNFe(retConsulta.getProtNFe().get(0));
				}
				vezes = 10;
			}
		}
	}
	
	private <T> DadosResposta<T> executaComando(DadosRequisicao dados, Class<T> clazz, Servico servico) throws Exception {
		String url = preparaConexao(dados);
		
		String dadosXML = servico.getDados(dados);
		OMElement elemento = ConverterUtils.toOMElement(dadosXML);
		log.debug("OMElement: "+elemento.toString());
		
		String retorno = servico.getMetodo().call(elemento, url);
		log.info("Retorno WS: "+retorno);
		T objRetorno = xmlConverter.toObj(retorno, clazz);
		return new DadosResposta<T>(dadosXML, objRetorno);
	}

	private String preparaConexao(DadosRequisicao dados) throws Exception {
		urlService.setUf(dadosEmissor.getUf());
		String url = dados.getUrl().apply(dados);
		prepara.setPathCacerts(dadosEmissor.getPathCacerts());
		prepara.preparaConexaoSegura(url);
		return url;
	}
}