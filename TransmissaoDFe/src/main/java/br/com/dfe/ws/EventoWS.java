package br.com.dfe.ws;

import br.com.dfe.Configuracao;
import br.com.dfe.api.AssinaDocumento;
import br.com.dfe.api.TipoEmissao;
import br.com.dfe.impl.AssinaXML;
import br.com.dfe.url.Operacao;
import br.com.dfe.url.URLRepository;
import br.com.dfe.url.URLWS;
import br.inf.portalfiscal.www.nfe.wsdl.nferecepcaoevento4.RecepcaoEvento4Stub;
import org.apache.axiom.om.OMElement;

import java.rmi.RemoteException;

import static br.com.dfe.utils.NFUtils.*;

public class EventoWS implements OperacaoWS {

    private final URLRepository urlRepository;
    private final Configuracao configuracao;
    private final AssinaDocumento assinaDocumento;

    private String modelo;
    private TipoEmissao tipoEmissao;
    private String xmlTEnvEvento;

    public EventoWS(URLRepository urlRepository, Configuracao configuracao) {
        this.urlRepository = urlRepository;
        this.configuracao = configuracao;
        this.assinaDocumento = new AssinaXML(configuracao.getCertificado(), configuracao.getPrivateKey());
    }

    @Override
    public String getURL() {
        URLWS urlws = urlRepository.findBy(Operacao.EVENTO, modelo, tipoEmissao);
        return configuracao.getAmbiente() == 1 ? urlws.getProducao() : urlws.getHomologacao();
    }

    @Override
    public String montaEnvio() {
        return xmlTEnvEvento;
    }

    @Override
    public String callWS(OMElement omElement) throws RemoteException {
        RecepcaoEvento4Stub.NfeDadosMsg dados = new RecepcaoEvento4Stub.NfeDadosMsg();
        dados.setExtraElement(omElement);

        RecepcaoEvento4Stub stub = new RecepcaoEvento4Stub(getURL());
        return stub.nfeRecepcaoEvento(dados).getExtraElement().toString();
    }

    public void setXmlTEnvEvento(String xmlTEnvEvento) throws Exception {
        this.xmlTEnvEvento = assinaDocumento.assinarEvento(xmlTEnvEvento);

        String chave = getChaveFromEvento(xmlTEnvEvento);
        modelo = getModeloFromChave(chave);
        tipoEmissao = TipoEmissao.of(getTipoEmissaoFromChave(chave));
    }
}
