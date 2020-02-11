package br.com.dfe.ws;

import br.com.dfe.Configuracao;
import br.com.dfe.utils.NFCeBuilder;
import br.com.dfe.url.URLWS;
import br.com.dfe.api.AssinaDocumento;
import br.com.dfe.api.TipoEmissao;
import br.com.dfe.impl.AssinaXML;
import br.com.dfe.schema.TEnviNFe;
import br.com.dfe.schema.TNFe;
import br.com.dfe.url.Operacao;
import br.com.dfe.url.URLRepository;
import br.com.dfe.util.XMLUtils;
import br.inf.portalfiscal.www.nfe.wsdl.nfeautorizacao4.NfeAutorizacao4Stub;
import lombok.Getter;
import org.apache.axiom.om.OMElement;
import org.apache.commons.lang3.StringUtils;

import java.rmi.RemoteException;

public class EnvioNFWS implements OperacaoWS {

    private final URLRepository urlRepository;
    private final Configuracao configuracao;
    private final AssinaDocumento assinaDocumento;

    private TNFe tnFe;
    private int ambiente;
    private String xml;

    @Getter
    private String modelo;
    @Getter
    private TipoEmissao tipoEmissao;

    public EnvioNFWS(URLRepository urlRepository, Configuracao configuracao) {
        this.urlRepository = urlRepository;
        this.configuracao = configuracao;
        this.assinaDocumento = new AssinaXML(configuracao.getCertificado(), configuracao.getPrivateKey());
    }

    @Override
    public String getURL() {
        URLWS urlws = urlRepository.findBy(Operacao.ENVIO_NF, modelo, tipoEmissao);
        return ambiente == 1 ? urlws.getProducao() : urlws.getHomologacao();
    }

    @Override
    public String montaEnvio() throws Exception {
        if (StringUtils.isBlank(xml)) {
            if (tnFe.getInfNFe().getIde().getMod().equals("65")) {
                NFCeBuilder nfCeService = new NFCeBuilder(urlRepository, configuracao);
                tnFe.setInfNFeSupl(nfCeService.buildInfNFeSupl(tnFe));
            }

            TEnviNFe envNfe = new TEnviNFe();
            envNfe.setIdLote("1");
            envNfe.setIndSinc(configuracao.isAsync() ? "0" : "1");
            envNfe.setVersao("4.00");
            envNfe.getNFe().add(tnFe);
            xml = XMLUtils.criaStrXML(envNfe);
        }
        return xml;
    }

    @Override
    public String callWS(OMElement omElement) throws RemoteException {
        NfeAutorizacao4Stub.NfeDadosMsg dados = new NfeAutorizacao4Stub.NfeDadosMsg();
        dados.setExtraElement(omElement);

        NfeAutorizacao4Stub stub = new NfeAutorizacao4Stub(getURL());
        return stub.nfeAutorizacaoLote(dados).getExtraElement().toString();
    }

    public void setXmlTNFe(String xmlTNFe) throws Exception {
        xmlTNFe = assinaDocumento.assinarTNFe(xmlTNFe);
        this.tnFe = XMLUtils.toObj(xmlTNFe, TNFe.class);

        modelo = tnFe.getInfNFe().getIde().getMod();
        ambiente = Integer.parseInt(tnFe.getInfNFe().getIde().getTpAmb());
        tipoEmissao = TipoEmissao.of(tnFe.getInfNFe().getIde().getTpEmis());

        if (modelo.equals("65") && tipoEmissao.equals(TipoEmissao.CONTINGENCIA_OFFLINE)) {
            tipoEmissao = TipoEmissao.NORMAL;
        }
    }
}
