package br.com.dfe.schema.util;

import br.com.dfe.integrador.*;
import br.com.dfe.schema.TConsSitNFe;
import br.com.dfe.schema.TNFe;
import br.com.dfe.schema.TNFe.InfNFe.Det.Imposto.ICMS;
import br.com.dfe.util.XMLContextFactory;
import br.com.dfe.util.XMLUtils;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class LeitorXMLTest {

	@Test
	public void xmlFileToStr() throws Exception {
		String xml = getXML();
		
		TNFe nfe = XMLUtils.toObj(xml, TNFe.class);
		assertNotNull(nfe);
		nfe.getInfNFe().getDet().get(0).getImposto().getContent().stream()
			.filter(el -> el.getName().getLocalPart().equals("ICMS"))
			.forEach(el -> {
				Object obj = el.getValue();
				ICMS icms = (ICMS) obj;
				assertNotNull(icms);
				assertFalse(icms.getICMS60().getVBCEfet().isEmpty());
			});

		xml = XMLUtils.criaStrXML(nfe);
		System.out.println(xml);
		assertFalse(xml.contains(":ns2"));
	}

	@Test
	void unmarshall() throws Exception {
		String xml = getXML();

		assertDoesNotThrow(() -> {
			Unmarshaller unmarshaller = JAXBContext.newInstance(TNFe.class).createUnmarshaller();
			XMLInputFactory factory = XMLInputFactory.newFactory();
			try (InputStream inputStream = new ByteArrayInputStream(xml.getBytes())) {
				XMLStreamReader reader = factory.createXMLStreamReader(inputStream);
				TNFe tnFe = unmarshaller.unmarshal(reader, TNFe.class).getValue();
				assertNotNull(tnFe);
				reader.close();
			}
		});
	}

	@Test
	void toStringConsSitNFe() {
		TConsSitNFe consSitNFe = new TConsSitNFe();
		consSitNFe.setChNFe("456");
		consSitNFe.setTpAmb("1");
		assertDoesNotThrow(() -> {
			System.out.println(XMLUtils.criaStrXML(consSitNFe));
		});
	}

	String getXML() throws IOException, URISyntaxException {
		Path caminhoXML = Paths.get(getClass().getClassLoader().getResource("001181.xml").toURI());
		assertNotNull(caminhoXML);
		assertTrue(caminhoXML.toFile().exists());

		return Files.readAllLines(caminhoXML).get(0);
	}

	@Test
	void integradorToXML() throws JAXBException, IOException {
		Integrador integrador = new Integrador();

		Identificador identificador = new Identificador();
		identificador.setValor("123");
		integrador.setIdentificador(identificador);

		Componente componente = new Componente();
		componente.setNome("NFCE");
		integrador.setComponente(componente);

		Metodo metodo = new Metodo();
		metodo.setNome("HNfeAutorizacaoLote12");
		metodo.setParametros(new Parametros());
		metodo.getParametros().setParametros(new ArrayList<>());

		integrador.getComponente().setMetodo(metodo);

		Parametro parametro1 = new Parametro();
		parametro1.setNome("numeroSessao");
		parametro1.setValor("123456");

		Parametro parametro2 = new Parametro();
		parametro2.setNome("versaoDados");
		parametro2.setValor("4.00");

		metodo.getParametros().getParametros().add(parametro1);
		metodo.getParametros().getParametros().add(parametro2);

		Writer writer = new StringWriter();
		XMLContextFactory.getInstance().getMarshaller(Integrador.class).marshal(integrador, writer);

		String xml = writer.toString(); // XMLUtils.criaStrXML(integrador);
		System.out.println(xml);
	}
}
