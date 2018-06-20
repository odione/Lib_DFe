package br.com.dfe.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.dfe.api.TipoEmissao;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.ws.UrlWS;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class URLService {

	@Autowired private DadosEmissor dados;
	
	private ObjectMapper mapper;
	private UrlWS url;
	
	@PostConstruct
	public void init() {
		mapper = new ObjectMapper();
	}
	
	public String getUrlStatusServico() throws IOException {
		carregaUrlFromFile("status");
		return getUrl();
	}
	
	public String getUrlConsultaNF() throws IOException {
		carregaUrlFromFile("consultaNF");
		return getUrl();
	}
	
	public String getUrlEnviaNF() throws IOException {
		carregaUrlFromFile("enviaNF");
		return getUrl();
	}
	
	public String getUrlRetEnviaNF() throws IOException {
		carregaUrlFromFile("retEnviaNF");
		return getUrl();
	}
	
	public String getUrlEvento() throws IOException {
		String evento = dados.getTipoEmissao().equals(TipoEmissao.EPEC) ? "epec" : "evento";
		carregaUrlFromFile(evento);
		return getUrl();
	}
	
	public String getUrlInutilizacao() throws IOException {
		carregaUrlFromFile("inutilizacao");
		return getUrl();
	}
	
	public String getUrlConsultaNFCe() throws IOException {
		carregaUrlFromFile("consultaNFCe");
		return getUrl();
	}
	
	public String getUrlQrCode() throws IOException {
		carregaUrlFromFile("qrCode");
		return getUrl();
	}
	
	private String getUrl() {
		return dados.getAmbiente() == 2 ? url.getHomologacao() : url.getProducao();
	}
	
	public void carregaUrlFromFile(String fileName) throws IOException {
		String caminhoResource = "url_webservices_"+dados.getModelo()+"/"
				+ (TipoEmissao.isContingenciaOnLine(dados.getTipoEmissao()) ? "contingencia/":"")
				+fileName+".json";
		
		InputStream json = getClass().getClassLoader().getResourceAsStream(caminhoResource);
		List<UrlWS> urls = mapper.readValue(json, new TypeReference<List<UrlWS>>() {});
		url = urls.stream()
			.filter(url -> url.getUf().contains(dados.getUf()))
			.findFirst()
			.orElseThrow(() -> new RuntimeException("URL do WebService não encontrado!"));
		log.info("Url encontrada: "+url.toString());
	}
}