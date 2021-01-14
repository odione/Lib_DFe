package br.com.dfe.conexao;

import br.com.dfe.Configuracao;
import br.com.dfe.certificado.Certificado;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

import javax.net.SocketFactory;
import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Log4j2
public class SocketFactoryDinamico implements ProtocolSocketFactory {
    public static final String TLSv1_2 = "TLSv1.2";
    public static final int TIMEOUT_PADRAO_EM_MILLIS = 60_000;

    private final SSLContext ssl;
    private final Configuracao configuracao;

    public SocketFactoryDinamico(Configuracao configuracao) throws Exception {
        this.configuracao = configuracao;
        this.ssl = this.createSSLContext();
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localAddress, int localPort) throws IOException {
        return this.ssl.getSocketFactory().createSocket(host, port, localAddress, localPort);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, @NonNull HttpConnectionParams params) throws IOException {
        SocketFactory socketfactory = ssl.getSocketFactory();
        int timeout = (params.getConnectionTimeout() > 0) ? params.getConnectionTimeout() : TIMEOUT_PADRAO_EM_MILLIS;

        Socket socket = socketfactory.createSocket();
        ((SSLSocket) socket).setEnabledProtocols(new String[]{TLSv1_2});
        SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
        socket.bind(localaddr);

        SocketAddress remoteaddr = new InetSocketAddress(host, port);
        socket.connect(remoteaddr, timeout);
        return socket;
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        return ssl.getSocketFactory().createSocket(host, port);
    }

    private SSLContext createSSLContext() throws Exception {
        KeyManager[] keyManagers = createKeyManagers();
        TrustManager[] trustManagers = createTrustManagers();
        SSLContext sslContext = SSLContext.getInstance(TLSv1_2);
        sslContext.init(keyManagers, trustManagers, null);
        log.info("PROTOCOLO SSL CRIADO: " + sslContext.getProtocol());

        return sslContext;
    }

    public KeyManager[] createKeyManagers() {
        HSKeyManager keyManager = new HSKeyManager(configuracao.getCertificado());
        return new KeyManager[]{keyManager};
    }

    public TrustManager[] createTrustManagers() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore trustStore = KeyStore.getInstance("JKS");

        try (InputStream in = new FileInputStream(BuildCacerts.CACERTS_FILE_NAME)) {
            trustStore.load(in, "changeit".toCharArray());
        }
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);
        return trustManagerFactory.getTrustManagers();
    }

    @RequiredArgsConstructor
    class HSKeyManager implements X509KeyManager {
        private final Certificado certificado;

        public String chooseClientAlias(String[] arg0, Principal[] arg1, Socket arg2) {
            return certificado.getAlias();
        }

        public String chooseServerAlias(String arg0, Principal[] arg1, Socket arg2) {
            return certificado.getAlias();
        }

        public X509Certificate[] getCertificateChain(String alias) {
            try {
                Certificate[] certificates = certificado.getKeyStore().getCertificateChain(alias);
                X509Certificate[] x509Certificates = new X509Certificate[certificates.length];
                System.arraycopy(certificates, 0, x509Certificates, 0, certificates.length);
                return x509Certificates;
            } catch (KeyStoreException e) {
                log.catching(e);
            }

            return new X509Certificate[]{certificado.getCertificate()};
        }

        public String[] getClientAliases(String arg0, Principal[] arg1) {
            return new String[]{certificado.getAlias()};
        }

        public PrivateKey getPrivateKey(String arg0) {
            return certificado.getPrivateKey();
        }

        public String[] getServerAliases(String arg0, Principal[] arg1) {
            return new String[]{certificado.getAlias()};
        }
    }
}