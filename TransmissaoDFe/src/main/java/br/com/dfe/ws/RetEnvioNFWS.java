package br.com.dfe.ws;

import br.com.dfe.Configuracao;
import br.com.dfe.url.URLWS;
import br.com.dfe.api.TipoEmissao;
import br.com.dfe.schema.TConsReciNFe;
import br.com.dfe.schema.TRetConsReciNFe;
import br.com.dfe.schema.TRetEnviNFe;
import br.com.dfe.url.Operacao;
import br.com.dfe.url.URLRepository;
import br.com.dfe.util.XMLUtils;
import br.inf.portalfiscal.www.nfe.wsdl.nferetautorizacao4.NfeRetAutorizacao4Stub;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.axiom.om.OMElement;
import org.apache.commons.lang3.StringUtils;

import java.rmi.RemoteException;

@RequiredArgsConstructor
public class RetEnvioNFWS implements OperacaoWS {

    private final URLRepository urlRepository;
    private final Configuracao configuracao;

    private String xml;

    @Setter
    private TRetEnviNFe retEnviNFe;

    @Setter
    @Getter
    private String modelo;

    @Setter
    private TipoEmissao tipoEmissao;

    @Override
    public String getURL() {
        URLWS urlws = urlRepository.findBy(Operacao.RET_ENVIO_NF, modelo, tipoEmissao);
        return configuracao.getAmbiente() == 1 ? urlws.getProducao() : urlws.getHomologacao();
    }

    @Override
    public String montaEnvio() throws Exception {
        if (StringUtils.isBlank(xml)) {
            TConsReciNFe consultaRecibo = new TConsReciNFe();
            consultaRecibo.setVersao(retEnviNFe.getVersao());
            consultaRecibo.setNRec(retEnviNFe.getInfRec().getNRec());
            consultaRecibo.setTpAmb(retEnviNFe.getTpAmb());
            xml = XMLUtils.criaStrXML(consultaRecibo);
        }
        return xml;
    }

    @Override
    public String callWS(OMElement omElement) throws RemoteException {
        NfeRetAutorizacao4Stub.NfeDadosMsg dados = new NfeRetAutorizacao4Stub.NfeDadosMsg();
        dados.setExtraElement(omElement);

        NfeRetAutorizacao4Stub stub = new NfeRetAutorizacao4Stub(getURL());
        return stub.nfeRetAutorizacaoLote(dados).getExtraElement().toString();
    }

    @Override
    public String enviaParaIntegrador(OMElement omElement) throws Exception {
        throw new RuntimeException("enviaParaIntegrador | Função não implementada");
    }

    public boolean processada(TRetConsReciNFe retConsReciNFe) {
        return !retConsReciNFe.getCStat().equals("105");
    }
}
