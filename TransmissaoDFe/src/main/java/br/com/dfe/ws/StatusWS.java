package br.com.dfe.ws;

import br.com.dfe.Configuracao;
import br.com.dfe.url.Operacao;
import br.com.dfe.url.URLRepository;
import br.com.dfe.url.URLWS;
import br.com.dfe.api.TipoEmissao;
import br.com.dfe.schema.TConsStatServ;
import br.com.dfe.util.XMLUtils;
import br.com.dfe.utils.ConverterUtils;
import br.inf.portalfiscal.www.nfe.wsdl.nfestatusservico4.NfeStatusServico4Stub;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.axiom.om.OMElement;

import java.rmi.RemoteException;

@RequiredArgsConstructor
public class StatusWS implements OperacaoWS {

    private final URLRepository urlRepository;
    private final Configuracao configuracao;

    @Setter
    private int ambiente;
    @Setter
    private TipoEmissao tipoEmissao;
    @Setter
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
    public String callWS(OMElement omElement) throws RemoteException {
        NfeStatusServico4Stub.NfeDadosMsg dados = new NfeStatusServico4Stub.NfeDadosMsg();
        dados.setExtraElement(omElement);

        NfeStatusServico4Stub stub = new NfeStatusServico4Stub(getURL());
        return stub.nfeStatusServicoNF(dados).getExtraElement().toString();
    }
}
