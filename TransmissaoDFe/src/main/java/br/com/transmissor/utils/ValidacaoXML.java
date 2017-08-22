package br.com.transmissor.utils;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import br.com.dfe.schema.TNfeProc;
import br.com.dfe.util.LeitorXML;

public class ValidacaoXML {
	
	public static String errosValidacao;

	public static boolean isProcNfe(String strXML){
		try {
			TNfeProc procNFe = new LeitorXML().toObj(strXML, TNfeProc.class);
			return ((procNFe != null) && (procNFe.getNFe() != null));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean validaXMLEnvio(String strXML) throws ParserConfigurationException, SAXException, IOException  {
		return validaSchema(strXML, 1);
	}
	
	/**
	 * @param opcao -> 0 = TNFe / 1 = TEnviNFe
	 * @author Desenvolvimento
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws Exception 
	 */
	public static boolean validaSchema(String strXML, int opcao) throws ParserConfigurationException, SAXException, IOException  {
		String pathXSD = "";
//		if (opcao == 0) {
//			pathXSD = UtilsXML.getPathXSD()+"nfe_v3.10.xsd";
//		} else if (opcao == 1) {
//			pathXSD = UtilsXML.getPathXSD()+"enviNFe_v3.10.xsd";
//		}
		strXML = normalizeXML(strXML);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        factory.setNamespaceAware(true);  
        factory.setValidating(true);  
        factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");  
        factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", pathXSD);  
        DocumentBuilder builder = factory.newDocumentBuilder();
        XMLErrorHandler errorHandler = new XMLErrorHandler();
        builder.setErrorHandler(errorHandler);
        
        builder.parse(new InputSource(new StringReader(strXML)));
        
        if (!errorHandler.getListaComErrosDeValidacao().isEmpty()) {
        	errosValidacao = errorHandler.getListaComErrosDeValidacao().toString();
        	return false;
        } 
        return true;
	}
	
	public static String normalizeXML(String xml) {  
        if ((xml != null) && (!"".equals(xml))) {  
            xml = xml.replaceAll("\\r\\n", "");  
            xml = xml.replaceAll(" standalone=\"no\"", "");  
        }  
        return xml;  
    }
}