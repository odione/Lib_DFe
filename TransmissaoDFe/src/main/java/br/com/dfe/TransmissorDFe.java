package br.com.dfe;

import br.com.dfe.api.ConexaoSegura;
import br.com.dfe.api.TipoEmissao;
import br.com.dfe.impl.ConexaoSeguraImpl;
import br.com.dfe.schema.*;
import br.com.dfe.schema.canc.TRetEnvEvento;
import br.com.dfe.url.URLRepository;
import br.com.dfe.util.XMLUtils;
import br.com.dfe.utils.ConverterUtils;
import br.com.dfe.ws.*;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.axiom.om.OMElement;
import org.apache.commons.lang3.StringUtils;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Log4j2
public class TransmissorDFe {

    private final Configuracao configuracao;
    private URLRepository urlRepository;
    private ConexaoSegura conexaoSegura;

    public TransmissorDFe(Configuracao configuracao) {
        this.configuracao = configuracao;
        urlRepository = new URLRepository(configuracao.getUf());
        conexaoSegura = new ConexaoSeguraImpl(configuracao);
    }

    public TRetConsStatServ statusServico(int ambiente, String modelo, TipoEmissao tipoEmissao) throws Exception {
        StatusWS statusWS = new StatusWS(urlRepository, configuracao);
        statusWS.setAmbiente(ambiente);
        statusWS.setModelo(modelo);
        statusWS.setTipoEmissao(tipoEmissao);
        return XMLUtils.toObj(executa(statusWS), TRetConsStatServ.class);
    }

    public TRetConsSitNFe consultaNf(String chave) throws Exception {
        ConsultaNFWS consultaNFWS = new ConsultaNFWS(urlRepository, configuracao);
        consultaNFWS.setChave(chave);
        return XMLUtils.toObj(executa(consultaNFWS), TRetConsSitNFe.class);
    }

    public TRetEnviNFe enviaNf(@NonNull String xmlTNFe, @NonNull Consumer<String> xmlPreEnvioNF) throws Exception {
        EnvioNFWS envioNFWS = new EnvioNFWS(urlRepository, configuracao);
        envioNFWS.setXmlTNFe(xmlTNFe);
        xmlPreEnvioNF.accept(envioNFWS.montaEnvio());
        TRetEnviNFe retEnviNFe = XMLUtils.toObj(executa(envioNFWS), TRetEnviNFe.class);

        if (configuracao.isAsync() && retEnviNFe.getCStat().equals("103")) {
            consultaRecibo(envioNFWS, retEnviNFe);
        }

        return retEnviNFe;
    }

    private void consultaRecibo(EnvioNFWS envioNFWS, TRetEnviNFe retEnviNFe) throws Exception {
        RetEnvioNFWS retEnvioNFWS = new RetEnvioNFWS(urlRepository, configuracao);
        retEnvioNFWS.setRetEnviNFe(retEnviNFe);
        retEnvioNFWS.setModelo(envioNFWS.getModelo());
        retEnvioNFWS.setTipoEmissao(envioNFWS.getTipoEmissao());

        TRetConsReciNFe retConsReciNFe = aguardaRetornoRecibo(retEnvioNFWS);
        if (!retConsReciNFe.getProtNFe().isEmpty()) {
            TProtNFe protNFe = retConsReciNFe.getProtNFe().get(0);
            retEnviNFe.setCStat(protNFe.getInfProt().getCStat());
            retEnviNFe.setXMotivo(protNFe.getInfProt().getXMotivo());
            retEnviNFe.setProtNFe(protNFe);
        } else {
            retEnviNFe.setCStat(retConsReciNFe.getCStat());
            retEnviNFe.setXMotivo(retConsReciNFe.getXMotivo());
        }
    }

    public TRetEnvEvento cancela(String xmlTEnvEvento) throws Exception {
        EventoWS eventoWS = new EventoWS(urlRepository, configuracao);
        eventoWS.setXmlTEnvEvento(xmlTEnvEvento);
        return XMLUtils.toObj(executa(eventoWS), TRetEnvEvento.class);
    }

    public br.com.dfe.schema.cce.TRetEnvEvento enviaCCe(String xmlTEnvEvento) throws Exception {
        EventoWS eventoWS = new EventoWS(urlRepository, configuracao);
        eventoWS.setXmlTEnvEvento(xmlTEnvEvento);
        return XMLUtils.toObj(executa(eventoWS), br.com.dfe.schema.cce.TRetEnvEvento.class);
    }

    public TRetInutNFe inutiliza(String xmlTInut) throws Exception {
        InutilizacaoWS inutilizacaoWS = new InutilizacaoWS(urlRepository, configuracao);
        inutilizacaoWS.setXmlInutNFe(xmlTInut);
        return XMLUtils.toObj(executa(inutilizacaoWS), TRetInutNFe.class);
    }

    private TRetConsReciNFe aguardaRetornoRecibo(RetEnvioNFWS operacao) throws Exception {
        TRetConsReciNFe retConsReciNFe = null;
        int vezes = 0;
        while (vezes < 10) {
            String retorno = executa(operacao);
            retConsReciNFe = XMLUtils.toObj(retorno, TRetConsReciNFe.class);

            if (operacao.processada(retConsReciNFe)) {
                return retConsReciNFe;
            }
            vezes++;
            TimeUnit.SECONDS.sleep(1);
        }
        return retConsReciNFe;
    }

    private String executa(OperacaoWS operacaoWS) throws Exception {
        OMElement omElement = ConverterUtils.toOMElement(operacaoWS.montaEnvio());

        conexaoSegura.preparaConexaoSegura(operacaoWS.getURL());

        log.info("OMElement: " + omElement.toString());
        log.info(String.format("Chamando WebService %s ...", operacaoWS.getURL()));
        String retorno = operacaoWS.callWS(omElement);
        log.info("Retorno no WebService: " + retorno);
        return retorno;
    }

    public void atualizaConfiguracao(@NonNull Configuracao configuracao) {
        if (!StringUtils.equalsIgnoreCase(this.configuracao.getUf(), configuracao.getUf())) {
            this.configuracao.setUf(configuracao.getUf());
            urlRepository = new URLRepository(configuracao.getUf());
        }
        this.configuracao.setIdCSC(configuracao.getIdCSC());
        this.configuracao.setCSC(configuracao.getCSC());
        this.configuracao.setAmbiente(configuracao.getAmbiente());
    }

    public void setCertificateAndPrivateKey(X509Certificate certificate, PrivateKey privateKey) {
        if (certificate != null &&
            configuracao.getCertificado() != null &&
            certificate.getSerialNumber().compareTo(configuracao.getCertificado().getSerialNumber()) == 0) {
            return;
        }

        configuracao.setCertificado(certificate);
        configuracao.setPrivateKey(privateKey);
        conexaoSegura = new ConexaoSeguraImpl(configuracao);
    }
}
