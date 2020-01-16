package br.com.dfe.ws;

import br.com.dfe.Configuracao;
import br.com.dfe.url.URLWS;
import br.com.dfe.api.TipoEmissao;
import br.com.dfe.schema.TConsSitNFe;
import br.com.dfe.url.Operacao;
import br.com.dfe.url.URLRepository;
import br.com.dfe.util.XMLUtils;
import br.com.dfe.utils.NFUtils;
import br.inf.portalfiscal.www.nfe.wsdl.nfeconsultaprotocolo4.NfeConsulta4Stub;
import lombok.RequiredArgsConstructor;
import org.apache.axiom.om.OMElement;

import java.rmi.RemoteException;

@RequiredArgsConstructor
public class ConsultaNFWS implements OperacaoWS {

    private final URLRepository urlRepository;
    private final Configuracao configuracao;

    private String chave;
    private String modelo;
    private TipoEmissao tipoEmissao;

    @Override
    public String getURL() {
        URLWS urlws = urlRepository.findBy(Operacao.CONSULTA_NF, modelo, tipoEmissao);
        return configuracao.getAmbiente() == 1 ? urlws.getProducao() : urlws.getHomologacao();
    }

    @Override
    public String montaEnvio() throws Exception {
        TConsSitNFe consSitNFe = new TConsSitNFe();
        consSitNFe.setChNFe(chave);
        consSitNFe.setTpAmb(String.valueOf(configuracao.getAmbiente()));
        consSitNFe.setVersao("4.00");
        consSitNFe.setXServ("CONSULTAR");
        return XMLUtils.criaStrXML(consSitNFe);
    }

    @Override
    public String callWS(OMElement omElement) throws RemoteException {
        NfeConsulta4Stub.NfeDadosMsg dados = new NfeConsulta4Stub.NfeDadosMsg();
        dados.setExtraElement(omElement);

        NfeConsulta4Stub stub = new NfeConsulta4Stub(getURL());
        return stub.nfeConsultaNF(dados).getExtraElement().toString();
    }

    public void setChave(String chave) {
        this.chave = chave;
        this.modelo = NFUtils.getModeloFromChave(chave);
        this.tipoEmissao = TipoEmissao.of(NFUtils.getTipoEmissaoFromChave(chave));
    }
}
