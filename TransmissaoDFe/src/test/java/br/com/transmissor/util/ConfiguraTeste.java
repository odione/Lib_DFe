package br.com.transmissor.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import br.com.transmissor.api.PreparaConexaoSegura;
import br.com.transmissor.configuracao.DadosEmissor;
import br.com.transmissor.utils.CertificadoUtils;

@Component
public class ConfiguraTeste {
	
	@Autowired
	private DadosEmissor dados;
	
	@Autowired
	private PreparaConexaoSegura conexaoSegura;
	
	private CertificadoUtils utils;

	public void configuraDadosEmissor() {
		utils = new CertificadoUtils();
		try {
			utils.loadPFX(ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX+"certs/cert_A1.pfx").getAbsolutePath(), "1234");
			dados.setCertificado(utils.getCertificado());
			dados.setPrivateKey(utils.getPrivateKey());
			dados.setUf("SC");
			dados.setUfCodigo("42");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void configuraConexaoSegura() {
		conexaoSegura.setPathCacerts(System.getProperty("user.dir")+"/Resources/NFeCacerts");
	}
}