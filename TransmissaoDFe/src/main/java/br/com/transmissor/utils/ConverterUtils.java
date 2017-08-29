package br.com.transmissor.utils;

import java.io.StringReader;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.util.StAXParserConfiguration;
import org.xml.sax.InputSource;

public class ConverterUtils {

	public static OMElement toOMElement(String xml) {
		OMFactory factory = OMAbstractFactory.getOMFactory();
		
		return factory.getMetaFactory().createOMBuilder(factory, StAXParserConfiguration.NON_COALESCING , 
			new InputSource(new StringReader(xml))).getDocumentElement();
	}
}