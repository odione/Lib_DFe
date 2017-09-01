package br.com.dfe.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import br.com.dfe.api.AssinaDocumento;

@Component
public class AssinaXML implements AssinaDocumento {

	private static final String INFINUT = "infInut";
    private static final String INFEVENTO = "infEvento";
    private static final String NFE = "NFe";
    
    private X509Certificate certificado;
    private PrivateKey privateKey;
    private KeyInfo keyInfo;
    private XMLSignatureFactory signFactory;
    
    private static final Logger log = LogManager.getLogger(AssinaXML.class);
    
    public AssinaXML() {
    	super();
    }
    
	@Override
	public String assinarEnvNFe(String strXML, X509Certificate cert, PrivateKey key)
			throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, SAXException, IOException,
			ParserConfigurationException, MarshalException, XMLSignatureException, TransformerException {
		configura(cert, key);
		return assinaXML(strXML, NFE, "infNFe");
	}

	@Override
	public String assinarEvento(String strXML, X509Certificate cert, PrivateKey key)
			throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, SAXException, IOException,
			ParserConfigurationException, MarshalException, XMLSignatureException, TransformerException {
		configura(cert, key);
		return assinaXML(strXML, "evento", INFEVENTO);
	}

	@Override
	public String assinarInutilizada(String strXML, X509Certificate cert, PrivateKey key)
			throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, SAXException, IOException,
			ParserConfigurationException, MarshalException, XMLSignatureException, TransformerException {
		configura(cert, key);
		return assinaXML(strXML, "inutNFe", INFINUT);
	}
	
	private void configura(X509Certificate cert, PrivateKey key) {
		this.certificado = cert;
		this.privateKey = key;
		log.info("Criando Assinatura Documento...");
		signFactory = XMLSignatureFactory.getInstance("DOM");
		
		KeyInfoFactory keyInfoFactory = signFactory.getKeyInfoFactory();
		List<X509Certificate> x509Content = new ArrayList<X509Certificate>();
		x509Content.add(certificado);
		
		X509Data data509 = keyInfoFactory.newX509Data(x509Content);
		keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(data509));
		log.info("Criado Assinatura Documento!");
	}
            
    private String assinaXML(String xml, String tag, String strID) throws SAXException, IOException, ParserConfigurationException, 
    	NoSuchAlgorithmException, InvalidAlgorithmParameterException, MarshalException, XMLSignatureException, TransformerException {
    	Document documento;
		log.info("Iniciou assinatura documento...");
		xml = addXmlEncoding(xml);
		documento = documentFactory(xml);
		NodeList elementos = documento.getElementsByTagName(tag);
		Node elementoAssinar = elementos.item(0);
		
		elementos = documento.getElementsByTagName(strID);
		
		Element elemento = (Element)elementos.item(0);
		String ID = elemento.getAttribute("Id");
		elemento.setIdAttribute("Id", true);
		
		DOMSignContext domSignContext = new DOMSignContext(privateKey, elementoAssinar);
		
		List<Transform> transforms = signatureFactory(signFactory);
		
		Reference ref = signFactory.newReference("#"+ID,signFactory.newDigestMethod(DigestMethod.SHA1, null),transforms,null,null);
		
		SignedInfo signInfo = signFactory.newSignedInfo(signFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,
		       (C14NMethodParameterSpec) null), signFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
		        Collections.singletonList(ref));
		
		XMLSignature signature = signFactory.newXMLSignature(signInfo, keyInfo);
		
		signature.sign(domSignContext);
		log.info("Assinou Documento!");
		return outputXML(documento);
    }
    
    private Document documentFactory(String xml) throws SAXException, IOException, ParserConfigurationException {  
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		return factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
	}
    
    private ArrayList<Transform> signatureFactory(XMLSignatureFactory signatureFactory) {  
        ArrayList<Transform> transformList = new ArrayList<Transform>();  
        TransformParameterSpec tps = null;  
        Transform envelopedTransform;
		try {
			envelopedTransform = signatureFactory.newTransform(Transform.ENVELOPED, tps);
			Transform c14NTransform = signatureFactory.newTransform("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", tps);  
			
			transformList.add(envelopedTransform);  
			transformList.add(c14NTransform);  
			return transformList;  
		} catch (NoSuchAlgorithmException e) {
			log.error("", e);
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			log.error("", e);
			e.printStackTrace();
		}
		return null;
    }
    
    private String outputXML(Document doc) throws TransformerException {  
        ByteArrayOutputStream os = new ByteArrayOutputStream();  
        TransformerFactory tf = TransformerFactory.newInstance();  
        Transformer trans = tf.newTransformer();  
        trans.transform(new DOMSource(doc), new StreamResult(os));  
        String xml = os.toString();  
        if ((xml != null) && (!"".equals(xml))) {  
            xml = xml.replaceAll("\\r\\n", "");  
            xml = xml.replaceAll(" standalone=\"no\"", "");  
        }  
        return xml;  
    }
    
    public static String addXmlEncoding(String strXml) {
		if (!strXml.startsWith("<?xml version")) {
			strXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+strXml;
		}    			    				
    	return strXml;
    }
}