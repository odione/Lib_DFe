package br.com.dfe.service;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.dfe.api.TipoEmissao;
import br.com.dfe.configuracao.DadosRequisicao;
import br.com.dfe.ws.UrlWS;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class URLService {

	@Setter
	private String uf;
	
	private ObjectMapper mapper;
	
	public URLService() {
		mapper = new ObjectMapper();
	}
	
	public String getUrlStatusServico(@NonNull DadosRequisicao dados) {
		return carregaUrlFromFileNew(dados, "status");
	}
	
	public String getUrlConsultaNF(@NonNull DadosRequisicao dados) {
		return carregaUrlFromFileNew(dados, "consultaNF");
	}
	
	public String getUrlEnviaNF(@NonNull DadosRequisicao dados) {
		return carregaUrlFromFileNew(dados, "enviaNF");
	}
	
	public String getUrlRetEnviaNF(@NonNull DadosRequisicao dados) {
		return carregaUrlFromFileNew(dados, "retEnviaNF");
	}
	
	public String getUrlEvento(@NonNull DadosRequisicao dados) {
		String evento = dados.getTipoEmissao().equals(TipoEmissao.EPEC) ? "epec" : "evento";
		return carregaUrlFromFileNew(dados, evento);
	}
	
	public String getUrlInutilizacao(@NonNull DadosRequisicao dados) {
		return carregaUrlFromFileNew(dados, "inutilizacao");
	}
	
	public String getUrlConsultaNFCe(@NonNull DadosRequisicao dados) {
		return carregaUrlFromFileNew(dados, "consultaNFCe");
	}
	
	public String getUrlQrCode(@NonNull DadosRequisicao dados) {
		return carregaUrlFromFileNew(dados, "qrCode");
	}
	
	private String carregaUrlFromFileNew(DadosRequisicao dados, String fileName) {
		List<UrlWS> urls = getURLs(dados.getModelo(), dados.getTipoEmissao(), fileName);
		Optional<String> optionalUrl = urls.stream()
				.filter(url -> url.getUf().contains(this.uf))
				.map(url -> dados.getAmbiente() == 2 ? url.getHomologacao() : url.getProducao())
				.findFirst();
		
		optionalUrl.ifPresent(url -> log.info("Url encontrada: "+url));
		return optionalUrl.orElseThrow(() -> new RuntimeException("URL do WebService não encontrado!"));
	}
	
	@SneakyThrows
	private List<UrlWS> getURLs(String modelo, TipoEmissao tipoEmissao, String fileName) {
		String caminhoResource = "url_webservices_"+modelo+"/"
				+ (TipoEmissao.isContingenciaOnLine(tipoEmissao) ? "contingencia/":"")
				+fileName+".json";
		
		@Cleanup InputStream json = getClass().getClassLoader().getResourceAsStream(caminhoResource);
		return mapper.readValue(json, new TypeReference<List<UrlWS>>() {});
	}
}