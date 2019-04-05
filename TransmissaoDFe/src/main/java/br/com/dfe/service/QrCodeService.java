package br.com.dfe.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.dfe.api.TipoEmissao;
import br.com.dfe.api.XMLConverter;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.configuracao.DadosRequisicao;
import br.com.dfe.schema.TNFe;
import br.com.dfe.schema.TNFe.InfNFeSupl;
import br.com.dfe.utils.ConverterUtils;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class QrCodeService {
	
	private URLService urlService;
	@Autowired private DadosEmissor dadosEmissor;
	@Autowired private XMLConverter xmlConverter;
	
	@PostConstruct
	public void init() {
		this.urlService = new URLService();
	}
	
	public void colocaQrCode(TNFe nfe, DadosRequisicao dados) throws Exception {
		if (!nfe.getInfNFe().getIde().getMod().equals("65")) return;
		urlService.setUf(ConverterUtils.UFCodigoToUFStr(nfe.getInfNFe().getIde().getCUF()));
		
		String urlQrCode = montaQrCodeUrl(nfe, dados);
		
		nfe.setInfNFeSupl(new InfNFeSupl());
		nfe.getInfNFeSupl().setUrlChave(urlService.getUrlConsultaNFCe(dados));
		nfe.getInfNFeSupl().setQrCode(urlQrCode);
	}
	
	public String getDigestValue(TNFe nfe) throws Exception {
		String xml = xmlConverter.toString(nfe, false);
		int inicio = xml.lastIndexOf("<DigestValue>")+13;
		int fim = xml.lastIndexOf("</DigestValue>");
		return xml.substring(inicio, fim);
	}
	
	public String montaQrCodeUrl(TNFe nfe, DadosRequisicao dados) throws Exception {
		StringBuffer qrCode = new StringBuffer()
			.append(nfe.getInfNFe().getId().substring(3)) 			  //chave
			.append("|").append("2") 								  //versao qrcode
			.append("|").append(nfe.getInfNFe().getIde().getTpAmb()); //ambiente
		
		if (dados.getTipoEmissao().equals(TipoEmissao.CONTINGENCIA_OFFLINE)) {
			qrCode
				.append("|").append(nfe.getInfNFe().getIde().getDhEmi().substring(8, 10)) //dia da data de emissao
				.append("|").append(nfe.getInfNFe().getTotal().getICMSTot().getVNF())	  //total nf
				.append("|").append(getHex(getDigestValue(nfe).getBytes()));			  //digest
		}
		
		qrCode.append("|").append(dadosEmissor.getIdCSC());	// id CSC
		qrCode.append("|"+getHashQrCode(qrCode.toString()));
		String urlCompleta = urlService.getUrlQrCode(dados).concat("?p=").concat(qrCode.toString());
		log.debug("QrCode: "+urlCompleta);
		return urlCompleta;
	}

	private String getHashQrCode(String qrCode) throws NoSuchAlgorithmException {
		return getHashSHA1(qrCode+dadosEmissor.getCSC());
	}
	
	private String getHex(byte[] value) {
		return new String(Hex.encodeHex(value));
	}
	
	public String getHashSHA1(String value) throws NoSuchAlgorithmException{
		MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
		return getHex(mDigest.digest(value.getBytes())).toUpperCase();
	}
}