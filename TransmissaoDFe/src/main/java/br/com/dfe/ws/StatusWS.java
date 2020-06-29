package br.com.dfe.ws;

import br.com.dfe.Configuracao;
import br.com.dfe.api.TipoEmissao;
import br.com.dfe.integrador.Integrador;
import br.com.dfe.integrador.IntegradorComunicador;
import br.com.dfe.integrador.IntegradorFactory;
import br.com.dfe.integrador.IntegradorMetodo;
import br.com.dfe.schema.TConsStatServ;
import br.com.dfe.url.Operacao;
import br.com.dfe.url.URLRepository;
import br.com.dfe.url.URLWS;
import br.com.dfe.util.XMLUtils;
import br.com.dfe.utils.ConverterUtils;
import br.inf.portalfiscal.www.nfe.wsdl.nfestatusservico4.NfeStatusServico4Stub;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.axiom.om.OMElement;

@RequiredArgsConstructor
public class StatusWS implements OperacaoWS {

    private final URLRepository urlRepository;
    private final Configuracao configuracao;

    @Setter
    private int ambiente;
    @Setter
    private TipoEmissao tipoEmissao;
    @Setter
    @Getter
    private String modelo;

    @Override
    public String getURL() {
        URLWS urlws = urlRepository.findBy(Operacao.STATUS, modelo, tipoEmissao);
        return ambiente == 1 ? urlws.getProducao() : urlws.getHomologacao();
    }

    @Override
    public String montaEnvio() throws Exception {
        TConsStatServ consulta = new TConsStatServ();
        consulta.setCUF(ConverterUtils.UFStrToUFCodigo(configuracao.getUf()));
        consulta.setTpAmb(String.valueOf(ambiente));
        consulta.setVersao("4.00");
        consulta.setXServ("STATUS");
        return XMLUtils.criaStrXML(consulta);
    }

    @Override
    public String callWS(OMElement omElement) throws Exception {
        NfeStatusServico4Stub.NfeDadosMsg dados = new NfeStatusServico4Stub.NfeDadosMsg();
        dados.setExtraElement(omElement);

        NfeStatusServico4Stub stub = new NfeStatusServico4Stub(getURL());
        NfeStatusServico4Stub.NfeResultMsg resultMsg = stub.nfeStatusServicoNF(dados);
        stub.cleanup();
        return resultMsg.getExtraElement().toString();
    }

    @Override
    public String enviaParaIntegrador(OMElement omElement) throws Exception {
        NfeStatusServico4Stub.NfeDadosMsg dados = new NfeStatusServico4Stub.NfeDadosMsg();
        dados.setExtraElement(omElement);

        Integrador integrador = IntegradorFactory
            .newInstance(IntegradorMetodo.getStatusServico(ambiente));

        String soap = ConverterUtils.toSoapEnvelope(dados, NfeStatusServico4Stub.NfeDadosMsg.MY_QNAME);
        integrador.addDadosXML(soap);

        IntegradorComunicador comunicador = new IntegradorComunicador();
        return comunicador.envia(integrador);
    }
}
