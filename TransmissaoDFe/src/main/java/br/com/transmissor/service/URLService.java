package br.com.transmissor.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.transmissor.configuracao.DadosEmissor;
import br.com.transmissor.ws.UrlWS;

@Service
public class URLService {

	@Autowired
	private DadosEmissor dados;
	
	private ObjectMapper mapper;
	private UrlWS url;
	
	@PostConstruct
	public void init() {
		mapper = new ObjectMapper();
	}
	
	public String getUrlStatusServico() {
		carregaUrlFromFile("status");
		return getUrl();
	}
	
	public String getUrlConsultaNF() {
		carregaUrlFromFile("consultaNF");
		return getUrl();
	}
	
	public String getUrlEnviaNF() {
		carregaUrlFromFile("enviaNF");
		return getUrl();
	}
	
	public String getUrlEvento() {
		carregaUrlFromFile("evento");
		return getUrl();
	}
	
	public String getUrlInutilizacao() {
		carregaUrlFromFile("inutilizacao");
		return getUrl();
	}
	
	public String getUrl() {
		return dados.getAmbiente() == 2 ? url.getHomologacao() : url.getProducao();
	}
	
	public void carregaUrlFromFile(String fileName) {
		try {
			File json = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX+"url_webservices/"+fileName+".json"); 
			List<UrlWS> urls = mapper.readValue(json, new TypeReference<List<UrlWS>>() {});
			url = urls.stream()
				.filter(url -> url.getUf().contains(dados.getUf()))
				.findFirst()
				.get();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}