package br.com.dfe.util;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.dfe.MainTest;
import br.com.dfe.api.XMLConverter;
import br.com.dfe.schema.TNFe;

public class XmlConverterTest extends MainTest {

	@Autowired
	private XMLConverter converter;
	
	@Test
	public void convertNfe() throws Exception {
		String xml = Files.readAllLines(Paths.get("/home/odione/df_documentos/nfe/xml", "/nfe.xml")).get(0);
		
		TNFe nfe = converter.toObj(xml, TNFe.class);
		assertThat(nfe).isNotNull();
		assertThat(nfe.getInfNFe().getId()).isNotEmpty();
	}
}