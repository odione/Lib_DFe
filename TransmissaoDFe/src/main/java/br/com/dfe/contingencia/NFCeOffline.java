package br.com.dfe.contingencia;

import br.com.dfe.Configuracao;
import br.com.dfe.api.AssinaDocumento;
import br.com.dfe.impl.AssinaXML;
import br.com.dfe.schema.TNFe;
import br.com.dfe.url.URLRepository;
import br.com.dfe.util.XMLUtils;
import br.com.dfe.utils.NFCeBuilder;

public class NFCeOffline {

    private final URLRepository urlRepository;
    private final Configuracao configuracao;
    private final AssinaDocumento assinaDocumento;
    private final NFCeBuilder nfCeBuilder;

    public NFCeOffline(URLRepository urlRepository, Configuracao configuracao) {
        this.configuracao = configuracao;
        this.urlRepository = urlRepository;
        this.assinaDocumento = new AssinaXML(configuracao.getCertificado(), configuracao.getPrivateKey());
        this.nfCeBuilder = new NFCeBuilder(urlRepository, configuracao);
    }

    public String geraXML(String xmlTNFe) throws Exception {
        xmlTNFe = assinaDocumento.assinarTNFe(xmlTNFe);
        TNFe tnFe = XMLUtils.toObj(xmlTNFe, TNFe.class);
        tnFe.setInfNFeSupl(nfCeBuilder.buildInfNFeSupl(tnFe));
        return XMLUtils.criaStrXML(tnFe);
    }
}
