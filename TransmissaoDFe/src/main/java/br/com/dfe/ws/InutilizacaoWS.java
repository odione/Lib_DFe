package br.com.dfe.ws;

import br.com.dfe.Configuracao;
import br.com.dfe.api.AssinaDocumento;
import br.com.dfe.impl.AssinaXML;
import br.com.dfe.integrador.Integrador;
import br.com.dfe.integrador.IntegradorComunicador;
import br.com.dfe.integrador.IntegradorFactory;
import br.com.dfe.integrador.IntegradorMetodo;
import br.com.dfe.url.URLRepository;
import br.com.dfe.url.URLWS;
import br.com.dfe.utils.ConverterUtils;
import br.inf.portalfiscal.www.nfe.wsdl.nfeinutilizacao4.NfeInutilizacao4Stub;
import lombok.Getter;
import org.apache.axiom.om.OMElement;

import java.rmi.RemoteException;
import java.time.LocalDateTime;

import static br.com.dfe.utils.NFUtils.getChaveFromInutilizacao;
import static br.com.dfe.utils.NFUtils.getModeloFromInutilizacao;

public class InutilizacaoWS implements OperacaoWS {

    private final URLRepository urlRepository;
    private final Configuracao configuracao;
    private final AssinaDocumento assinaDocumento;

    @Getter
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

    @Override
    public String enviaParaIntegrador(OMElement omElement) throws Exception {
        NfeInutilizacao4Stub.NfeDadosMsg dados = new NfeInutilizacao4Stub.NfeDadosMsg();
        dados.setExtraElement(omElement);

        Integrador integrador = IntegradorFactory
            .newInstance(IntegradorMetodo.getInutilizacao(configuracao.getAmbiente()));

        String dataGeracao = ConverterUtils.formatToIntegrador(LocalDateTime.now());
        integrador.addParametro("DataHoraNFCeGerado", dataGeracao);
        integrador.addParametro("NumeroNFCe", getChaveFromInutilizacao(xmlInutNFe));
        integrador.addParametro("ValorNFCe", "0.00");

        String soap = ConverterUtils.toSoapEnvelope(dados, NfeInutilizacao4Stub.NfeDadosMsg.MY_QNAME);
        integrador.addDadosXML(soap);

        IntegradorComunicador comunicador = new IntegradorComunicador();
        return comunicador.envia(integrador);
    }

    public void setXmlInutNFe(String xmlInutNFe) throws Exception {
        this.xmlInutNFe = assinaDocumento.assinarInutilizada(xmlInutNFe);
        modelo = getModeloFromInutilizacao(xmlInutNFe);
    }
}
