package br.com.dfe.schema.util;

import br.com.dfe.schema.TNFe;
import br.com.dfe.schema.TNFe.InfNFe.Det.Imposto.ICMS;
import br.com.dfe.util.XMLUtils;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class LeitorXMLTest {

	@Test
	public void xmlFileToStr() throws Exception {
		Path caminhoXML = Paths.get(getClass().getClassLoader().getResource("001181.xml").toURI());
		assertNotNull(caminhoXML);
		assertTrue(caminhoXML.toFile().exists());
		
		XMLUtils parser = new XMLUtils();
		String xml = Files.readAllLines(caminhoXML).get(0);
		
		TNFe nfe = parser.toObj(xml, TNFe.class);
		assertNotNull(nfe);
		nfe.getInfNFe().getDet().get(0).getImposto().getContent().stream()
			.filter(el -> el.getName().getLocalPart().equals("ICMS"))
			.forEach(el -> {
				Object obj = el.getValue();
				ICMS icms = (ICMS) obj;
				assertNotNull(icms);
				assertFalse(icms.getICMS60().getVBCEfet().isEmpty());
			});

		xml = parser.criaStrXML(nfe);
		System.out.println(xml);
		assertFalse(xml.contains(":ns2"));
	}
}
