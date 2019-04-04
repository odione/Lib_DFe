package br.com.dfe.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.utils.CertificadoUtils;

@Component
@Profile("dev")
public class ConfiguraTeste {
	
	@Autowired
	private DadosEmissor dados;
	
	private CertificadoUtils utils;

	public void configuraDadosEmissor() {
		utils = new CertificadoUtils();
		try {
			utils.loadPFX(ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX+"certs/cert_A1.pfx").getAbsolutePath(), "1234");
			dados.setCertificado(utils.getCertificado());
			dados.setPrivateKey(utils.getPrivateKey());
			dados.setUf("GO");
			dados.setUfCodigo("52");
			dados.setIdCSC("000009");
			dados.setCSC("5fsd1f561ad56f1a6d5f1a56f1a561f56a1");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void configuraConexaoSegura() {
		String pathCacerts = System.getProperty("user.dir")+"/NFeCacerts";
		dados.setPathCacerts(pathCacerts);
	}
}