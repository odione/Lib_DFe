package br.com.dfe.integrador;

import br.com.dfe.util.XMLUtils;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Log4j2
@NoArgsConstructor
public class IntegradorComunicador {

    private final Path caminhoEnvio = Paths.get("c:/Integrador/Input");
    private final Path caminhoResposta = Paths.get("c:/Integrador/Output");

    private String identificador = "";

    public String envia(@NonNull Integrador integrador) throws Exception {
        String xmlIntegrador = XMLUtils.criaStrXML(integrador);
        identificador = integrador.getIdentificador().getValor();

        log.info("Integrador | XML Envio: "+xmlIntegrador);

        File arquivoInput = caminhoEnvio.resolve(identificador + ".xml").toFile();
        FileUtils.writeStringToFile(arquivoInput, xmlIntegrador);

        Path arquivoResposta = aguardaRetorno()
            .orElseThrow(() -> new RuntimeException("Integrador não devolveu arquivo de Resposta"));

        String xmlResposta = readFile(arquivoResposta);
        log.info("Integrador | XML Resposta: "+xmlResposta);
        Files.deleteIfExists(arquivoResposta);
        return getRetornoWS(xmlResposta);
    }

    private Optional<Path> aguardaRetorno() throws Exception {
        TimeUnit.SECONDS.sleep(3);

        int count = 0;
        while (true) {
            TimeUnit.SECONDS.sleep(1);
            Optional<Path> arquivoResposta = Files.list(caminhoResposta)
                .filter(this::possuiIdentificador)
                .findFirst();
            if (arquivoResposta.isPresent()) {
                return arquivoResposta;
            }
            count++;
            if (count >= 15) {
                break;
            }
        }
        return Optional.empty();
    }

    private boolean possuiIdentificador(Path path) {
        try {
            String tag = "<Valor>" + identificador + "</Valor>";
            return Files.readAllLines(path).stream()
                .anyMatch(linha -> linha.contains(tag));
        } catch (IOException e) {
            log.catching(e);
        }
        return false;
    }

    public String readFile(Path path) throws IOException {
        return Files.readAllLines(path).stream()
            .reduce("", (ini, fim) -> ini + fim.replaceAll("\\p{C}", ""));
    }

    public String getRetornoWS(String xml) throws JAXBException {
        Integrador integrador = XMLUtils.toObj(xml, Integrador.class);
        String retornoIntegrador = integrador.getResposta().getRetorno().substring(1).replaceAll("\"", "");
        List<String> campos = Arrays.asList(retornoIntegrador.split("\\|"));
        String soapEnvelope = new String(Base64.getDecoder().decode(campos.get(6)));
        int indexIni = soapEnvelope.indexOf("<ret");
        int indexUltimo = soapEnvelope.lastIndexOf("</nfeResultMsg");
        return soapEnvelope.substring(indexIni, indexUltimo);
    }
}
