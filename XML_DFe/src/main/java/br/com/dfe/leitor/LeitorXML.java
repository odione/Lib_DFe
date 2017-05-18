package br.com.dfe.leitor;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

public class LeitorXML {

	/**
	 * @param StrXML
	 * @param clazz
	 * @return Objeto Preenchido
	 */
	public <T> T toObj(String StrXML, Class<T> clazz){
		try {
			Unmarshaller unmarshaller = JAXBContext.newInstance(clazz).createUnmarshaller();
			return ((JAXBElement<T>) unmarshaller.unmarshal(new StreamSource(new StringReader(StrXML)),clazz)).getValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
			
	@SuppressWarnings("unchecked")
	/**
	 * @param entidade
	 * @param formatado
	 * @return
	 */
	public <T> String criaStrXML(T entidade, boolean formatado){
		try {
			final StringWriter writer = new StringWriter();
			Class<T> classe = (Class<T>) entidade.getClass();
			
			Marshaller marshaller = JAXBContext.newInstance(classe).createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formatado);
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
			QName qname = new QName("http://www.portalfiscal.inf.br/nfe", preparaNomeElemento(entidade.getClass().getSimpleName()));
			JAXBElement<T> elemento = new JAXBElement<T>(qname, classe,entidade); 
			marshaller.marshal(elemento, writer);
			
			return writer.toString().replaceAll(" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "")
				.replaceAll(":ns2", "")
				.replaceAll("ns2:", "")
				.replaceAll("<Signature>", "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String preparaNomeElemento(String nomeClasse){
		return String.valueOf(nomeClasse.charAt(1)).toLowerCase()+nomeClasse.substring(2);
	}
}