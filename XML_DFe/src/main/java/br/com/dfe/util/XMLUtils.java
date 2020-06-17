package br.com.dfe.util;

import br.com.dfe.integrador.Integrador;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

@Log4j2
@UtilityClass
public class XMLUtils {

    /**
     * @param xml
     * @param clazz
     * @return Objeto Preenchido
     * @throws JAXBException
     */
    public <T> T toObj(String xml, Class<T> clazz) throws JAXBException {
        log.debug("Unmarshaller criando...");
        Unmarshaller unmarshaller = XMLContextFactory.getInstance().getUnmarshaller(clazz);
        log.debug("Unmarshaller criado!");
        return unmarshaller.unmarshal(new StreamSource(new StringReader(xml)), clazz).getValue();
    }

    /**
     * @param entidade
     * @return XML em string
     * @throws JAXBException
     */
    @SuppressWarnings("unchecked")
    public <T> String criaStrXML(T entidade) throws JAXBException, IOException {
        Class<T> classe = (Class<T>) entidade.getClass();

        QName qname = new QName("http://www.portalfiscal.inf.br/nfe", preparaNomeElemento(entidade.getClass().getSimpleName()));
        JAXBElement<T> elemento = new JAXBElement<T>(qname, classe, entidade);

        try (Writer writer = new StringWriter()) {
            log.debug("Marshaller criando...");
            Marshaller marshaller = XMLContextFactory.getInstance().getMarshaller(classe);
            log.debug("Marshaller criado!");
            marshaller.marshal(elemento, writer);

            return writer.toString().replaceAll(" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "")
                .replaceAll(":ns2", "")
                .replaceAll("ns2:", "")
                .replaceAll("<Signature>", "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">");
        }
    }

    public String criaStrXML(Integrador integrador) throws JAXBException {
        Marshaller marshaller = XMLContextFactory.getInstance().getMarshaller(Integrador.class);
        Writer writer = new StringWriter();
        marshaller.marshal(integrador, writer);
        return writer.toString();
    }

    private String preparaNomeElemento(String nomeClasse) {
        nomeClasse = nomeClasse.substring(1);
        if (nomeClasse.toUpperCase().equals("NFE")) {
            return "NFe";
        }
        return String.valueOf(nomeClasse.charAt(0)).toLowerCase() + nomeClasse.substring(1);
    }
}