package br.com.dfe.ws;

import br.com.dfe.Configuracao;
import br.com.dfe.api.AssinaDocumento;
import br.com.dfe.api.TipoEmissao;
import br.com.dfe.impl.AssinaXML;
import br.com.dfe.integrador.Integrador;
import br.com.dfe.integrador.IntegradorComunicador;
import br.com.dfe.integrador.IntegradorFactory;
import br.com.dfe.integrador.IntegradorMetodo;
import br.com.dfe.schema.TEnviNFe;
import br.com.dfe.schema.TNFe;
import br.com.dfe.url.Operacao;
import br.com.dfe.url.URLRepository;
import br.com.dfe.url.URLWS;
import br.com.dfe.util.XMLUtils;
import br.com.dfe.utils.ConverterUtils;
import br.com.dfe.utils.NFCeBuilder;
import br.inf.portalfiscal.www.nfe.wsdl.nfeautorizacao4.NfeAutorizacao4Stub;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.axiom.om.OMElement;
import org.apache.commons.lang3.StringUtils;

import java.rmi.RemoteException;
import java.time.LocalDateTime;

@Log4j2
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

    @SneakyThrows
    public EnvioNFWS(URLRepository urlRepository, Configuracao configuracao) {
        this.urlRepository = urlRepository;
        this.configuracao = configuracao;
        this.assinaDocumento = new AssinaXML(configuracao.getCertificado());
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
                NFCeBuilder nfCeBuilder = new NFCeBuilder(urlRepository, configuracao);
                tnFe.setInfNFeSupl(nfCeBuilder.buildInfNFeSupl(tnFe));
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
        NfeAutorizacao4Stub.NfeResultMsg resultMsg = stub.nfeAutorizacaoLote(dados);
        stub.cleanup();
        return resultMsg.getExtraElement().toString();
    }

    @Override
    public String enviaParaIntegrador(OMElement omElement) throws Exception {
        NfeAutorizacao4Stub.NfeDadosMsg dados = new NfeAutorizacao4Stub.NfeDadosMsg();
        dados.setExtraElement(omElement);

        Integrador integrador = IntegradorFactory
            .newInstance(IntegradorMetodo.getEnvioNF(ambiente));

        integrador.addParametro("NumeroNFCe", tnFe.getInfNFe().getId());
        String dataGeracao = ConverterUtils.formatToIntegrador(LocalDateTime.now());
        integrador.addParametro("DataHoraNFCeGerado", dataGeracao);
        integrador.addParametro("ValorNFCe", tnFe.getInfNFe().getTotal().getICMSTot().getVNF());

        String soap = ConverterUtils.toSoapEnvelope(dados, NfeAutorizacao4Stub.NfeDadosMsg.MY_QNAME);
        integrador.addDadosXML(soap);

        IntegradorComunicador comunicador = new IntegradorComunicador();
        return comunicador.envia(integrador);
    }

    public void setXmlTNFe(String xmlTNFe) throws Exception {
        xmlTNFe = assinaDocumento.assinarTNFe(xmlTNFe);
        this.tnFe = XMLUtils.toObj(xmlTNFe, TNFe.class);
        log.info("XML convertido para TNFe obj");

        modelo = tnFe.getInfNFe().getIde().getMod();
        ambiente = Integer.parseInt(tnFe.getInfNFe().getIde().getTpAmb());
        tipoEmissao = TipoEmissao.of(tnFe.getInfNFe().getIde().getTpEmis());

        if (modelo.equals("65") && tipoEmissao.equals(TipoEmissao.CONTINGENCIA_OFFLINE)) {
            tipoEmissao = TipoEmissao.NORMAL;
        }
    }
}
