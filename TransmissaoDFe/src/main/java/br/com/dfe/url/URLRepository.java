package br.com.dfe.url;

import br.com.dfe.api.TipoEmissao;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
public class URLRepository {

    private List<URLWS> urls = new ArrayList<>();
    private final String uf;

    @SneakyThrows
    public URLRepository(String uf) {
        this.uf = uf;
        carregaURL("55", TipoEmissao.NORMAL);
        carregaURL("65", TipoEmissao.NORMAL);
        carregaURL("55", TipoEmissao.CONTINGENCIA_SV_AN);
    }

    public URLWS findBy(Operacao operacao, String modelo, TipoEmissao tipoEmissao) {
        return urls.stream()
            .filter(u -> u.getModelo().equals(modelo) && u.getOperacao().equals(operacao) && u.getTipoEmissao().equals(tipoEmissao))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("URL não encontrado! Operação: "+operacao.name()+" Modelo: "+modelo+" Tipo Emissão: "+tipoEmissao.name()));
    }

    public URLWS findByInutilizacao(String modelo) {
        return findBy(Operacao.INUTILIZACAO, modelo, TipoEmissao.NORMAL);
    }

    private void carregaURL(String modelo, TipoEmissao tipoEmissao) {
        var folderUrl = "/url_webservices_"+modelo +
            (TipoEmissao.isContingenciaOnLine(tipoEmissao) ? "/contingencia/" : "/");

        var arquivos = new ArrayList<String>();
        Collections.addAll(arquivos, "consulta_nf.json", "envio_nf.json", "evento.json", "ret_envio_nf.json", "status.json");

        if (TipoEmissao.NORMAL.equals(tipoEmissao)) {
            arquivos.add("inutilizacao.json");
            if (modelo.equals("55")) {
                arquivos.add("epec.json");
            }
        }
        if (modelo.equals("65")) {
            arquivos.add("consulta_nfce.json");
            arquivos.add("qr_code.json");
        }

        arquivos.stream()
            .map(f -> folderUrl + f)
            .forEach(p -> add(p, modelo, tipoEmissao));
    }

    @SneakyThrows
    private void add(String pathJson, String modelo, TipoEmissao tipoEmissao) {
        @Cleanup var inputStream = getClass().getResourceAsStream(pathJson);

        IOUtils.readLines(inputStream, "UTF8").stream()
            .filter(l -> l.startsWith("{"))
            .map(JSONObject::new)
            .filter(json -> json.getString("uf").contains(this.uf))
            .map(json -> URLWS.builder()
                .operacao(Operacao.valueOf(FilenameUtils.getBaseName(pathJson).toUpperCase()))
                .modelo(modelo)
                .tipoEmissao(tipoEmissao)
                .ufs(json.getString("uf"))
                .producao(json.getString("producao"))
                .homologacao(json.getString("homologacao"))
                .build())
            .forEach(urls::add);
    }
}

