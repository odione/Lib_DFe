package br.com.dfe.util;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

public class XMLUtils {

    /**
     * @param xml
     * @param clazz
     * @return Objeto Preenchido
     * @throws JAXBException
     */
    public static <T> T toObj(String xml, Class<T> clazz) {
        return JAXB.unmarshal(new StringReader(xml), clazz);
    }

    /**
     * @param entidade
     * @return XML em string
     * @throws JAXBException
     */
	@SuppressWarnings("unchecked")
    public static <T> String criaStrXML(T entidade) throws JAXBException {
        Class<T> classe = (Class<T>) entidade.getClass();

        QName qname = new QName("http://www.portalfiscal.inf.br/nfe", preparaNomeElemento(entidade.getClass().getSimpleName()));
        JAXBElement<T> elemento = new JAXBElement<T>(qname, classe, entidade);

        Writer writer = new StringWriter();

        Marshaller marshaller = JAXBContext.newInstance(classe).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        marshaller.marshal(elemento, writer);

        return writer.toString().replaceAll(" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "")
            .replaceAll(":ns2", "")
            .replaceAll("ns2:", "")
            .replaceAll("<Signature>", "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">");
    }

    private static String preparaNomeElemento(String nomeClasse) {
        nomeClasse = nomeClasse.substring(1);
        if (nomeClasse.toUpperCase().equals("NFE")) {
            return "NFe";
        }
        return String.valueOf(nomeClasse.charAt(0)).toLowerCase() + nomeClasse.substring(1);
    }
}