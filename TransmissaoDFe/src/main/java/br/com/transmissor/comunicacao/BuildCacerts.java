package br.com.transmissor.comunicacao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BuildCacerts {
    
    private static String JSSECACERTS;
    private static final int TIMEOUT_WS = 30;  
    
    private static final Logger log = LogManager.getLogger(BuildCacerts.class.getSimpleName());
  
    public static void geraCacert(URL url, String locCacerts){
        try {
        	JSSECACERTS = locCacerts;
            char[] passphrase = "changeit".toCharArray();
            
            log.info("Gerando Arquivo Cacerts...");
  
            File file = new File(JSSECACERTS);  
            if (file.isFile() == false) {  
                char SEP = File.separatorChar;
                System.out.println(System.getProperty("java.home") + SEP + "lib" + SEP + "security");
                log.debug(System.getProperty("java.home") + SEP + "lib" + SEP + "security");
                File dir = new File(System.getProperty("java.home") + SEP + "lib" + SEP + "security");  
                file = new File(dir, JSSECACERTS);  
                if (file.isFile() == false) {  
                    file = new File(dir, "cacerts");  
                }  
            }  
  
            info("| Loading KeyStore " + file + "...");  
            InputStream in = new FileInputStream(file);
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            log.debug("KEY STORE: "+KeyStore.getDefaultType());
            System.out.println("KEYSTORE TIPO: "+KeyStore.getDefaultType());
            ks.load(in, passphrase);  
            in.close();
            
            get(url.getHost(), 443, ks);
            
            File cafile = new File(JSSECACERTS);  
            OutputStream out = new FileOutputStream(cafile);  
            ks.store(out, passphrase);  
            out.close();
            log.info("Arquivo Cacert Gerado!");
        } catch (Exception e) {
        	log.catching(e);
            e.printStackTrace();  
        }  
    }  
  
    public static void get(String host, int port, KeyStore ks) throws Exception {  
        SSLContext context = SSLContext.getInstance("TLSv1.2");  
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
            /** 
             * PKIX path building failed:  
             * sun.security.provider.certpath.SunCertPathBuilderException:  
             * unable to find valid certification path to requested target 
             * Não tratado, pois sempre ocorre essa exceção quando o cacerts 
             * nao esta gerado. 
             */  
        } catch (SSLException e) {  
            error("| " + e.toString());  
        } catch (Exception e) {
        	log.catching(e);
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
            info("| Added certificate to keystore '" + JSSECACERTS + "' using alias '" + alias + "'");            
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
            //throw new UnsupportedOperationException();  
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
      
    private static void info(String logStr) {  
        System.out.println("INFO: " + logStr);
        log.debug(logStr);
    }  
  
    private static void error(String logStr) {  
        System.out.println("ERROR: " + logStr);
        log.error(logStr);
    } 
}