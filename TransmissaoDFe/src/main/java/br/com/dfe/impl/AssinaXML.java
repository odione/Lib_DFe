package br.com.dfe.impl;

import br.com.dfe.api.AssinaDocumento;
import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.*;
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Log4j2
public class AssinaXML implements AssinaDocumento {

    private static final String INFINUT = "infInut";
    private static final String INFEVENTO = "infEvento";
    private static final String NFE = "NFe";

    private X509Certificate certificado;
    private PrivateKey privateKey;
    private KeyInfo keyInfo;
    private XMLSignatureFactory signFactory;
    private DocumentBuilderFactory documentBuilderFactory;

    public AssinaXML(X509Certificate certificado, PrivateKey privateKey) {
        this.certificado = certificado;
        this.privateKey = privateKey;
        configura();
    }

    @Override
    public String assinarTNFe(String strXML)
        throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, SAXException, IOException,
        ParserConfigurationException, MarshalException, XMLSignatureException, TransformerException {
        return assinaXML(strXML, NFE, "infNFe");
    }

    @Override
    public String assinarEvento(String strXML)
        throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, SAXException, IOException,
        ParserConfigurationException, MarshalException, XMLSignatureException, TransformerException {
        return assinaXML(strXML, "evento", INFEVENTO);
    }

    @Override
    public String assinarInutilizada(String strXML)
        throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, SAXException, IOException,
        ParserConfigurationException, MarshalException, XMLSignatureException, TransformerException {
        return assinaXML(strXML, "inutNFe", INFINUT);
    }

    private void configura() {
        signFactory = XMLSignatureFactory.getInstance("DOM");

        KeyInfoFactory keyInfoFactory = signFactory.getKeyInfoFactory();
        List<X509Certificate> x509Content = Arrays.asList(certificado);

        X509Data data509 = keyInfoFactory.newX509Data(x509Content);
        keyInfo = keyInfoFactory.newKeyInfo(Arrays.asList(data509));

        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
    }

    private String assinaXML(String xml, String tag, String strID) throws SAXException, IOException, ParserConfigurationException,
        NoSuchAlgorithmException, InvalidAlgorithmParameterException, MarshalException, XMLSignatureException, TransformerException {
        log.info("Iniciou assinatura documento...");
        xml = addXmlEncoding(xml);
        Document documento = documentFactory(xml);
        NodeList elementos = documento.getElementsByTagName(tag);
        Node elementoAssinar = elementos.item(0);

        elementos = documento.getElementsByTagName(strID);

        Element elemento = (Element) elementos.item(0);
        String ID = elemento.getAttribute("Id");
        elemento.setIdAttribute("Id", true);

        DOMSignContext domSignContext = new DOMSignContext(privateKey, elementoAssinar);

        List<Transform> transforms = signatureFactory(signFactory);

        Reference ref = signFactory.newReference("#" + ID, signFactory.newDigestMethod(DigestMethod.SHA1, null), transforms, null, null);

        SignedInfo signInfo = signFactory.newSignedInfo(signFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,
            (C14NMethodParameterSpec) null), signFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
            Collections.singletonList(ref));

        XMLSignature signature = signFactory.newXMLSignature(signInfo, keyInfo);

        signature.sign(domSignContext);
        return outputXML(documento);
    }

    private Document documentFactory(String xml) throws SAXException, IOException, ParserConfigurationException {
		return documentBuilderFactory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
    }

    private List<Transform> signatureFactory(XMLSignatureFactory signatureFactory) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        Transform envelopedTransform = signatureFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null);
        Transform c14NTransform = signatureFactory.newTransform("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", (TransformParameterSpec) null);
        return Arrays.asList(envelopedTransform, c14NTransform);
    }

    private String outputXML(Document doc) throws TransformerException, IOException {
        try(OutputStream os = new ByteArrayOutputStream()) {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.transform(new DOMSource(doc), new StreamResult(os));
            String xml = os.toString();
            log.info("Assinou Documento!");

            return (StringUtils.isBlank(xml)) ? xml : xml
                .replaceAll("\\r|\\n", "")
                .replaceAll("&#13;", "")
                .replaceAll(" standalone=\"no\"", "");
        }
    }

    public static String addXmlEncoding(String strXml) {
        if (!strXml.startsWith("<?xml version")) {
            strXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + strXml;
        }
        return strXml;
    }
}