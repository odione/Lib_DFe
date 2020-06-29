package br.com.dfe.utils;

import br.com.dfe.Configuracao;
import br.com.dfe.api.TipoEmissao;
import br.com.dfe.schema.TNFe;
import br.com.dfe.url.Operacao;
import br.com.dfe.url.URLRepository;
import br.com.dfe.url.URLWS;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.util.Base64;

import static org.apache.commons.lang3.StringUtils.isBlank;

@RequiredArgsConstructor
@Log4j2
public class NFCeBuilder {

    private final URLRepository urlRepository;
    private final Configuracao configuracao;

    public TNFe.InfNFeSupl buildInfNFeSupl(@NonNull TNFe tnFe) {
        checkConfiguracao();
        TNFe.InfNFeSupl infNFeSupl = new TNFe.InfNFeSupl();

        URLWS urlws = urlRepository.findBy(Operacao.CONSULTA_NFCE, "65", TipoEmissao.NORMAL);
        infNFeSupl.setUrlChave(tnFe.getInfNFe().getIde().getTpAmb().equals("1") ? urlws.getProducao() : urlws.getHomologacao());
        infNFeSupl.setQrCode(montaQrCodeUrl(tnFe));

        return infNFeSupl;
    }

    private String montaQrCodeUrl(TNFe nfe) {
        StringBuilder qrCode = new StringBuilder()
            .append(nfe.getInfNFe().getId().substring(3))             //chave
            .append("|").append("2")                                  //versao qrcode
            .append("|").append(nfe.getInfNFe().getIde().getTpAmb()); //ambiente

        if (TipoEmissao.isContingenciaOffline(nfe.getInfNFe().getIde().getTpEmis())) {
            qrCode
                .append("|").append(nfe.getInfNFe().getIde().getDhEmi().substring(8, 10)) //dia da data de emissao
                .append("|").append(nfe.getInfNFe().getTotal().getICMSTot().getVNF())           //total nf
                .append("|").append(getHex(getDigestValue(nfe)));                               //digest
        }

        qrCode.append("|").append(configuracao.getIdCSC());    // id CSC
        qrCode.append("|" + getHashSHA1(qrCode.toString() + configuracao.getCSC())); // hash

        URLWS urlws = urlRepository.findBy(Operacao.QR_CODE, "65", TipoEmissao.NORMAL);
        String urlQRCODe = nfe.getInfNFe().getIde().getTpAmb().equals("1") ? urlws.getProducao() : urlws.getHomologacao();

        String urlCompleta = urlQRCODe.concat("?p=").concat(qrCode.toString());
        log.debug("QrCode: " + urlCompleta);
        return urlCompleta;
    }

    private byte[] getDigestValue(TNFe tnFe) {
        return Base64.getEncoder().encode(tnFe.getSignature().getSignedInfo().getReference().getDigestValue());
    }

    private String getHex(byte[] value) {
        return new String(Hex.encodeHex(value));
    }

    @SneakyThrows
    private String getHashSHA1(String value) {
        MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
        return getHex(mDigest.digest(value.getBytes())).toUpperCase();
    }

    private void checkConfiguracao() {
        if (isBlank(configuracao.getIdCSC()) || isBlank(configuracao.getCSC())) {
            throw new RuntimeException("Dados CSC incompletos (NFCe)");
        }
    }
}
