package br.com.dfe.impl;

import br.com.dfe.Configuracao;
import br.com.dfe.api.ConexaoSegura;
import br.com.dfe.conexao.BuildCacerts;
import br.com.dfe.conexao.SocketFactoryDinamico;
import lombok.NonNull;
import org.apache.commons.httpclient.protocol.Protocol;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class ConexaoSeguraImpl implements ConexaoSegura {

    private final BuildCacerts buildCacerts;
    private final SocketFactoryDinamico socketDinamico;
    private final Set<String> urlRegistrada = new HashSet<>();

    public ConexaoSeguraImpl(Configuracao configuracao) {
        this.buildCacerts = new BuildCacerts(configuracao.getPathCacerts());
        this.socketDinamico = new SocketFactoryDinamico(configuracao.getCertificado(), configuracao.getPrivateKey());
        this.socketDinamico.setFileCacerts(configuracao.getPathCacerts());
        registraSocketDinamico();
    }

    @Override
    public void preparaConexaoSegura(@NonNull String urlStr) throws Exception {
        URL url = new URL(urlStr);
        if (urlRegistrada.add(url.getHost())) {
            gerarCacerts(url);
        }
    }

    private void gerarCacerts(URL url) {
        buildCacerts.geraCacert(url);
    }

    private void registraSocketDinamico() {
        Protocol.unregisterProtocol("https");
        Protocol protocol = new Protocol("https", socketDinamico, 443);
        Protocol.registerProtocol("https", protocol);
    }
}