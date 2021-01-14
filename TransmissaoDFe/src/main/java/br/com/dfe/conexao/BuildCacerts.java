package br.com.dfe.conexao;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Log4j2
@NoArgsConstructor
public class BuildCacerts {

    public static final String CACERTS_FILE_NAME = "cacerts";

    private final Path cacertLocal = Paths.get(CACERTS_FILE_NAME);
    private final int TIMEOUT_WS = 30;

    public void geraCacert(URL url) throws Exception {
        char[] passphrase = "changeit".toCharArray();
        log.info("Gerando Arquivo Cacerts...");
        Path cacertOrigem = Paths.get(System.getProperty("java.home"), "lib", "security", "cacerts");
        log.info("Cacert Java Home: " + cacertOrigem);

        log.info("| Loading KeyStore " + cacertOrigem + "...");
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        log.debug("KEY STORE: " + KeyStore.getDefaultType());

        try (InputStream in = new FileInputStream(cacertOrigem.toFile()); OutputStream out = new FileOutputStream(cacertLocal.toFile())) {
            ks.load(in, passphrase);
            get(url.getHost(), 443, ks);
            ks.store(out, passphrase);
        }

        log.info("Arquivo Cacert Gerado!");
    }

    public void get(String host, int port, KeyStore ks) throws Exception {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
        SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);

        SSLContext context = SSLContext.getInstance(SocketFactoryDinamico.TLSv1_2);
        context.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory factory = context.getSocketFactory();

        log.info("| Opening connection to " + host + ":" + port + "...");

        try (SSLSocket socket = (SSLSocket) factory.createSocket(host, port)) {
            socket.setSoTimeout(TIMEOUT_WS * 1000);
            log.info("| Starting SSL handshake...");
            socket.startHandshake();
            log.info("| No errors, certificate is already trusted");
        } catch (SSLHandshakeException e) {
        } catch (SSLException e) {
            log.error("| " + e.toString());
        } catch (Exception e) {
            log.catching(e);
        }

        X509Certificate[] chain = tm.chain;
        if (chain == null) {
            log.warn("| Could not obtain server certificate chain");
        }

        log.info("| Server sent " + chain.length + " certificate(s):");
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        for (int i = 0; i < chain.length; i++) {
            X509Certificate cert = chain[i];
            sha1.update(cert.getEncoded());
            md5.update(cert.getEncoded());

            String alias = host + "-" + (i);
            ks.setCertificateEntry(alias, cert);
            log.info("| Added certificate to keystore '" + cacertLocal + "' using alias '" + alias + "'");
        }
    }

    @RequiredArgsConstructor
    private static class SavingTrustManager implements X509TrustManager {
        private final X509TrustManager tm;
        private X509Certificate[] chain;

        public X509Certificate[] getAcceptedIssuers() {
            return this.tm.getAcceptedIssuers();
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            this.tm.checkClientTrusted(chain, authType);
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            this.chain = chain;
            tm.checkServerTrusted(chain, authType);
        }
    }
}