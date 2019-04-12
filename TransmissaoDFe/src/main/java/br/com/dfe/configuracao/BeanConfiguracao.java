package br.com.dfe.configuracao;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import br.com.dfe.util.LeitorXML;
import br.com.dfe.api.XMLConverter;

@ComponentScan(basePackages = "br.com.dfe")
@SpringBootConfiguration
public class BeanConfiguracao {

    @Bean
    public XMLConverter getXMLConverter() {
        return new XMLConverter() {
            private LeitorXML leitor = new LeitorXML();

            @Override
            public String toString(Object obj, boolean formatado) throws Exception {
                return leitor.criaStrXML(obj, formatado);
            }

            @Override
            public <T> T toObj(String xml, Class<T> clazz) throws Exception {
                xml = normaliza(xml);
                return leitor.toObj(xml, clazz);
            }

            private String normaliza(String xml) {
                if (xml.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
                    xml = xml.substring(38);
                }
                return xml;
            }
        };
    }
}