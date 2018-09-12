package br.com.dfe.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class BuildCacerts {
    
    private Path pathCacerts;
    private final int TIMEOUT_WS = 30;  
    
    public BuildCacerts(String pathCacerts) {
		this.pathCacerts = Paths.get(pathCacerts);
	}
    
    public void geraCacert(URL url){
        try {
            char[] passphrase = "changeit".toCharArray();
            
            log.info("Gerando Arquivo Cacerts...");
            
            if (Files.notExists(pathCacerts)) {
            	Files.createDirectories(pathCacerts.getParent());
            	Files.createFile(pathCacerts);
            }
            
            Path cacertJRE = Paths.get(System.getProperty("java.home"),"lib","security","cacerts");
            
            info("| Loading KeyStore " + pathCacerts + "...");
            InputStream in = new FileInputStream(cacertJRE.toFile());
            
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            log.debug("KEY STORE: "+KeyStore.getDefaultType());
            ks.load(in, passphrase);
            in.close();
            
            get(url.getHost(), 443, ks);
            
            OutputStream out = new FileOutputStream(pathCacerts.toFile());
            ks.store(out, passphrase);
            out.close();

            log.info("Arquivo Cacert Gerado!");
        } catch (Exception e) {
        	log.error("Exception", e);
            e.printStackTrace();
        }  
    }  
  
    public void get(String host, int port, KeyStore ks) throws Exception {  
        SSLContext context = SSLContext.getInstance(SocketFactoryDinamico.TLSv1_2);  
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());  
        tmf.init(ks);  
        X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];  
        SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);  
        context.init(null, new TrustManager[] { tm }, null);  
        SSLSocketFactory factory = context.getSocketFactory();  
       
        info("| Opening connection to " + host + ":" + port + "...");  
        SSLSocket socket = (SSLSocket) factory.createSocket(host, port);  
        socket.setSoTimeout(TIMEOUT_WS * 1000);  
        try {  
            info("| Starting SSL handshake...");  
            socket.startHandshake();  
            socket.close();  
            info("| No errors, certificate is already trusted");  
        } catch (SSLHandshakeException e) {  
        } catch (SSLException e) {  
            error("| " + e.toString());  
        } catch (Exception e) {
        	log.trace("Exception", e);
			e.printStackTrace();
		} 
          
        X509Certificate[] chain = tm.chain;  
        if (chain == null) {  
            info("| Could not obtain server certificate chain");  
        }  
  
        info("| Server sent " + chain.length + " certificate(s):");  
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");  
        MessageDigest md5 = MessageDigest.getInstance("MD5");  
        for (int i = 0; i < chain.length; i++) {  
            X509Certificate cert = chain[i];  
            sha1.update(cert.getEncoded());  
            md5.update(cert.getEncoded());  
              
            String alias = host + "-" + (i);  
            ks.setCertificateEntry(alias, cert);  
            info("| Added certificate to keystore '" + pathCacerts + "' using alias '" + alias + "'");            
        }  
    }  
  
    private static class SavingTrustManager implements X509TrustManager {  
        private final X509TrustManager tm;  
        private X509Certificate[] chain;  
       
        SavingTrustManager(X509TrustManager tm) {  
            this.tm = tm;  
        }  
       
        public X509Certificate[] getAcceptedIssuers() {
        	return new X509Certificate[0];
        }  
       
        public void checkClientTrusted(X509Certificate[] chain, String authType)  
            throws CertificateException {  
            throw new UnsupportedOperationException();  
        }  
       
        public void checkServerTrusted(X509Certificate[] chain, String authType)  
            throws CertificateException {  
            this.chain = chain;  
            tm.checkServerTrusted(chain, authType);
        }
    }
      
    private void info(String logStr) {  
        log.debug(logStr);
    }
  
    private void error(String logStr) {  
        log.error(logStr);
    }
}