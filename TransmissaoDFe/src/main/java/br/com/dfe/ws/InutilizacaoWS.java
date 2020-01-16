package br.com.dfe.ws;

import br.com.dfe.Configuracao;
import br.com.dfe.url.URLRepository;
import br.com.dfe.url.URLWS;
import br.com.dfe.api.AssinaDocumento;
import br.com.dfe.impl.AssinaXML;
import br.inf.portalfiscal.www.nfe.wsdl.nfeinutilizacao4.NfeInutilizacao4Stub;
import org.apache.axiom.om.OMElement;

import java.rmi.RemoteException;

import static br.com.dfe.utils.NFUtils.*;

public class InutilizacaoWS implements OperacaoWS {

    private final URLRepository urlRepository;
    private final Configuracao configuracao;
    private final AssinaDocumento assinaDocumento;

    private String modelo;
    private String xmlInutNFe;

    public InutilizacaoWS(URLRepository urlRepository, Configuracao configuracao) {
        this.urlRepository = urlRepository;
        this.configuracao = configuracao;
        this.assinaDocumento = new AssinaXML(configuracao.getCertificado(), configuracao.getPrivateKey());
    }

    @Override
    public String getURL() {
        URLWS urlws = urlRepository.findByInutilizacao(modelo);
        return configuracao.getAmbiente() == 1 ? urlws.getProducao() : urlws.getHomologacao();
    }

    @Override
    public String montaEnvio() {
        return xmlInutNFe;
    }

    @Override
    public String callWS(OMElement omElement) throws RemoteException {
        NfeInutilizacao4Stub.NfeDadosMsg dados = new NfeInutilizacao4Stub.NfeDadosMsg();
        dados.setExtraElement(omElement);

        NfeInutilizacao4Stub stub = new NfeInutilizacao4Stub(getURL());
        return stub.nfeInutilizacaoNF(dados).getExtraElement().toString();
    }

    public void setXmlInutNFe(String xmlInutNFe) throws Exception {
        this.xmlInutNFe = assinaDocumento.assinarInutilizada(xmlInutNFe);
        modelo = getModeloFromInutilizacao(xmlInutNFe);
    }
}
