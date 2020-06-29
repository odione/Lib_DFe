package br.com.dfe.ws;

import br.com.dfe.Configuracao;
import br.com.dfe.api.AssinaDocumento;
import br.com.dfe.api.TipoEmissao;
import br.com.dfe.impl.AssinaXML;
import br.com.dfe.integrador.Integrador;
import br.com.dfe.integrador.IntegradorComunicador;
import br.com.dfe.integrador.IntegradorFactory;
import br.com.dfe.integrador.IntegradorMetodo;
import br.com.dfe.url.Operacao;
import br.com.dfe.url.URLRepository;
import br.com.dfe.url.URLWS;
import br.com.dfe.utils.ConverterUtils;
import br.inf.portalfiscal.www.nfe.wsdl.nferecepcaoevento4.RecepcaoEvento4Stub;
import lombok.Getter;
import org.apache.axiom.om.OMElement;

import java.rmi.RemoteException;
import java.time.LocalDateTime;

import static br.com.dfe.utils.NFUtils.*;

public class EventoWS implements OperacaoWS {

    private final URLRepository urlRepository;
    private final Configuracao configuracao;
    private final AssinaDocumento assinaDocumento;

    @Getter
    private String modelo;
    private TipoEmissao tipoEmissao;
    private String xmlTEnvEvento;
    private String chave;

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

    @Override
    public String enviaParaIntegrador(OMElement omElement) throws Exception {
        RecepcaoEvento4Stub.NfeDadosMsg dados = new RecepcaoEvento4Stub.NfeDadosMsg();
        dados.setExtraElement(omElement);

        Integrador integrador = IntegradorFactory
            .newInstance(IntegradorMetodo.getCancelamento(configuracao.getAmbiente()));

        String dataGeracao = ConverterUtils.formatToIntegrador(LocalDateTime.now());
        integrador.addParametro("DataHoraNFCeGerado", dataGeracao);
        integrador.addParametro("NumeroNFCe", chave);
        integrador.addParametro("ValorNFCe", "0.00");

        String soap = ConverterUtils.toSoapEnvelope(dados, RecepcaoEvento4Stub.NfeDadosMsg.MY_QNAME);
        integrador.addDadosXML(soap);

        IntegradorComunicador comunicador = new IntegradorComunicador();
        return comunicador.envia(integrador);
    }

    public void setXmlTEnvEvento(String xmlTEnvEvento) throws Exception {
        this.xmlTEnvEvento = assinaDocumento.assinarEvento(xmlTEnvEvento);

        chave = getChaveFromEvento(xmlTEnvEvento);
        modelo = getModeloFromChave(chave);
        tipoEmissao = TipoEmissao.of(getTipoEmissaoFromChave(chave));
    }
}
