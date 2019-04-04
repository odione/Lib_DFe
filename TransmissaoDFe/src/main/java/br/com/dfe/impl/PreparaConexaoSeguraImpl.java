package br.com.dfe.impl;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.httpclient.protocol.Protocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dfe.configuracao.DadosEmissor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import br.com.dfe.api.PreparaConexaoSegura;
import br.com.dfe.utils.BuildCacerts;
import br.com.dfe.utils.SocketFactoryDinamico;

@Component
@NoArgsConstructor
public class PreparaConexaoSeguraImpl implements PreparaConexaoSegura {
	
	private BuildCacerts buildCacerts;
	private SocketFactoryDinamico socketDinamico;
	
	@Setter
	private String pathCacerts;
	
	@Autowired
	private DadosEmissor dados;
	
	@Override
	public void preparaConexaoSegura(@NonNull String url) throws Exception {
		gerarCacerts(url);
		registraSocketDinamico();
	}

	private void gerarCacerts(String url) throws MalformedURLException {
		if (buildCacerts == null) {
			buildCacerts = new BuildCacerts(pathCacerts);
		}
		
		buildCacerts.geraCacert(new URL(url));
	}
	
	private void registraSocketDinamico() {
		socketDinamico = new SocketFactoryDinamico(dados.getCertificado(), dados.getPrivateKey());
		socketDinamico.setFileCacerts(pathCacerts);
		
		Protocol.unregisterProtocol("https");
		Protocol protocol = new Protocol("https", socketDinamico, 443);
		Protocol.registerProtocol("https", protocol);
	}
}