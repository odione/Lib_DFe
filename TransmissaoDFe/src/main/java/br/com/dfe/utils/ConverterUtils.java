package br.com.dfe.utils;

import lombok.NonNull;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axiom.om.util.StAXParserConfiguration;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.databinding.ADBBean;
import org.apache.axis2.databinding.ADBException;
import org.apache.axis2.databinding.utils.ConverterUtil;
import org.apache.axis2.jaxws.util.SoapUtils;
import org.apache.axis2.jaxws.utility.ConvertUtils;
import org.apache.axis2.saaj.SOAPEnvelopeImpl;
import org.apache.axis2.saaj.SOAPMessageImpl;
import org.apache.axis2.saaj.util.SAAJUtil;
import org.apache.axis2.transport.TransportUtils;
import org.apache.axis2.util.XMLUtils;
import org.xml.sax.InputSource;

import javax.xml.namespace.QName;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ConverterUtils {

    public static List<String> ufStr = Arrays.asList("AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO", "MA", "MG", "MS", "MT", "PA", "PB", "PE", "PI", "PR", "RJ", "RN", "RO", "RR", "RS", "SC", "SE", "SP", "TO");
    public static List<String> ufCodigo = Arrays.asList("12", "27", "13", "16", "29", "23", "53", "32", "52", "21", "31", "50", "51", "15", "25", "26", "22", "41", "33", "24", "11", "14", "43", "42", "28", "35", "17");
    private static SOAPFactory soapFactory = OMAbstractFactory.getSOAP12Factory();

    public static OMElement toOMElement(String xml) {
        OMFactory factory = OMAbstractFactory.getOMFactory();

        OMElement element = factory.getMetaFactory().createOMBuilder(factory, StAXParserConfiguration.NON_COALESCING,
            new InputSource(new StringReader(xml))).getDocumentElement();

        Iterator<?> children = element.getChildrenWithLocalName("NFe");
        while (children.hasNext()) {
            OMElement omElement = (OMElement) children.next();
            if (omElement != null && "NFe".equals(omElement.getLocalName())) {
                omElement.addAttribute("xmlns", "http://www.portalfiscal.inf.br/nfe", null);
            }
        }

        return element;
    }

    public static String toSoapEnvelope(ADBBean adbBean, QName qName) throws ADBException {
        SOAPEnvelope soapEnvelope = soapFactory.getDefaultEnvelope();
        soapEnvelope.getBody().addChild(adbBean.getOMElement(qName, soapFactory));
        return soapEnvelope.toString();
    }

    public static String formatToIntegrador(@NonNull LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    public static String UFStrToUFCodigo(String uf) {
        int index = ufStr.indexOf(uf);
        if (index > -1) {
            return ufCodigo.get(index);
        }
        return "";
    }

    public static String UFCodigoToUFStr(String codigo) {
        int index = ufCodigo.indexOf(codigo);
        if (index > -1) {
            return ufStr.get(index);
        }
        return "";
    }
}