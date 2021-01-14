package br.com.dfe.impl;

import br.com.dfe.Configuracao;
import br.com.dfe.api.ConexaoSegura;
import br.com.dfe.conexao.BuildCacerts;
import br.com.dfe.conexao.SocketFactoryDinamico;
import lombok.NonNull;
import org.apache.commons.httpclient.protocol.Protocol;

import java.net.URL;
import java.security.Security;
import java.util.HashSet;
import java.util.Set;

public class ConexaoSeguraImpl implements ConexaoSegura {

    private final Configuracao configuracao;
    private final BuildCacerts buildCacerts;
    private final Set<String> urlRegistrada = new HashSet<>();
    private SocketFactoryDinamico socketDinamico;

    public ConexaoSeguraImpl(Configuracao configuracao) {
        this.configuracao = configuracao;
        this.buildCacerts = new BuildCacerts();
        System.setProperty("jdk.tls.client.protocols", SocketFactoryDinamico.TLSv1_2);
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
        System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
    }

    @Override
    public void preparaConexaoSegura(@NonNull String urlStr) throws Exception {
        URL url = new URL(urlStr);
        if (urlRegistrada.add(url.getHost())) {
            buildCacerts.geraCacert(url);
        }
        socketDinamico = new SocketFactoryDinamico(configuracao);
        registraSocketDinamico();
    }

    private void registraSocketDinamico() {
        Protocol.unregisterProtocol("https");
        Protocol protocol = new Protocol("https", socketDinamico, 443);
        Protocol.registerProtocol("https", protocol);
    }
}