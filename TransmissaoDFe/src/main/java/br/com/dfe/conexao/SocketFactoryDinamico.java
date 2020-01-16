package br.com.dfe.conexao;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

import javax.net.SocketFactory;
import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Log4j2
public class SocketFactoryDinamico implements ProtocolSocketFactory {
    private SSLContext ssl = null;
    private X509Certificate certificate;
    private PrivateKey privateKey;
    @Setter
    private String fileCacerts;

    public static final String TLSv1_2 = "TLSv1.2";

    public SocketFactoryDinamico(X509Certificate certificate, PrivateKey privateKey) {
        this.certificate = certificate;
        this.privateKey = privateKey;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localAddress, int localPort)
        throws IOException {
        return getSSLContext().getSocketFactory().createSocket(host, port, localAddress, localPort);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params) throws IOException {
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }

        SocketFactory socketfactory = getSSLContext().getSocketFactory();
        int timeout = params.getConnectionTimeout();
        if (timeout == 0) {
            return socketfactory.createSocket(host, port, localAddress, localPort);
        }

        Socket socket = socketfactory.createSocket();
        ((SSLSocket) socket).setEnabledProtocols(new String[]{TLSv1_2, "TLSv1"});
        SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
        SocketAddress remoteaddr = new InetSocketAddress(host, port);
        socket.bind(localaddr);
        try {
            socket.connect(remoteaddr, timeout);
        } catch (Exception e) {
            log.error(e.toString());
            throw new ConnectTimeoutException("Possível timeout de conexão", e);
        }

        return socket;
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        return getSSLContext().getSocketFactory().createSocket(host, port);
    }

    private SSLContext createSSLContext() {
        try {
            KeyManager[] keyManagers = createKeyManagers();
            TrustManager[] trustManagers = createTrustManagers();
            SSLContext sslContext = SSLContext.getInstance(TLSv1_2);
            log.info("PROTOCOLO SSL CRIADO: " + sslContext.getProtocol());
            sslContext.init(keyManagers, trustManagers, null);

            return sslContext;
        } catch (KeyManagementException e) {
            log.trace("", e);
            log.error(e.toString());
        } catch (KeyStoreException e) {
            log.trace("", e);
            log.error(e.toString());
        } catch (NoSuchAlgorithmException e) {
            log.trace("", e);
            log.error(e.toString());
        } catch (CertificateException e) {
            log.trace("", e);
            log.error(e.toString());
        } catch (IOException e) {
            log.trace("", e);
            log.error(e.toString());
        }
        return null;
    }

    private SSLContext getSSLContext() {
        if (ssl == null) {
            ssl = createSSLContext();
        }
        return ssl;
    }

    public KeyManager[] createKeyManagers() {
        HSKeyManager keyManager = new HSKeyManager(certificate, privateKey);

        return new KeyManager[]{keyManager};
    }

    public TrustManager[] createTrustManagers() throws KeyStoreException,
        NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore trustStore = KeyStore.getInstance("JKS");

        trustStore.load(new FileInputStream(fileCacerts), "changeit".toCharArray());
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);
        return trustManagerFactory.getTrustManagers();
    }

    class HSKeyManager implements X509KeyManager {
        private X509Certificate certificate;
        private PrivateKey privateKey;

        public HSKeyManager(X509Certificate certificate, PrivateKey privateKey) {
            this.certificate = certificate;
            this.privateKey = privateKey;
        }

        public String chooseClientAlias(String[] arg0, Principal[] arg1,
                                        Socket arg2) {
            return certificate.getIssuerDN().getName();
        }

        public String chooseServerAlias(String arg0, Principal[] arg1,
                                        Socket arg2) {
            return null;
        }

        public X509Certificate[] getCertificateChain(String arg0) {
            return new X509Certificate[]{certificate};
        }

        public String[] getClientAliases(String arg0, Principal[] arg1) {
            return new String[]{certificate.getIssuerDN().getName()};
        }

        public PrivateKey getPrivateKey(String arg0) {
            return privateKey;
        }

        public String[] getServerAliases(String arg0, Principal[] arg1) {
            return null;
        }
    }
}