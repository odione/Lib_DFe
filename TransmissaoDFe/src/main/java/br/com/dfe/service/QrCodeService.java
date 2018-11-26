package br.com.dfe.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.dfe.api.TipoEmissao;
import br.com.dfe.api.XMLConverter;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.schema.TNFe;
import br.com.dfe.schema.TNFe.InfNFeSupl;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class QrCodeService {
	
	@Autowired private DadosEmissor dadosEmissor;
	@Autowired private XMLConverter xmlConverter;
	@Autowired private URLService urlService;
	@Setter private TNFe nfe;
	
	private String xml;
	
	public void colocaQrCode() throws Exception {
		if (!nfe.getInfNFe().getIde().getMod().equals("65")) return;
		
		String urlQrCode = montaQrCodeUrl();
		
		nfe.setInfNFeSupl(new InfNFeSupl());
		nfe.getInfNFeSupl().setUrlChave(urlService.getUrlConsultaNFCe());
		nfe.getInfNFeSupl().setQrCode(urlQrCode);
	}
	
	public String getDigestValue() throws Exception {
		xml = xmlConverter.toString(nfe, false);
		int inicio = xml.lastIndexOf("<DigestValue>")+13;
		int fim = xml.lastIndexOf("</DigestValue>");
		return xml.substring(inicio, fim);
	}
	
	public String montaQrCodeUrl() throws Exception {
		StringBuffer qrCode = new StringBuffer()
			.append(nfe.getInfNFe().getId().substring(3)) 			  //chave
			.append("|").append("2") 								  //versao qrcode
			.append("|").append(nfe.getInfNFe().getIde().getTpAmb()); //ambiente
		
		if (dadosEmissor.getTipoEmissao().equals(TipoEmissao.CONTINGENCIA_OFFLINE)) {
			qrCode
				.append("|").append(nfe.getInfNFe().getIde().getDhEmi().substring(8, 10)) //dia da data de emissao
				.append("|").append(nfe.getInfNFe().getTotal().getICMSTot().getVNF())	  //total nf
				.append("|").append(getHex(getDigestValue().getBytes()));				  //digest
		}
		
		qrCode.append("|").append(dadosEmissor.getIdCSC());	// id CSC
		qrCode.append("|"+getHashQrCode(qrCode.toString()));
		String urlCompleta = urlService.getUrlQrCode().concat("?p=").concat(qrCode.toString());
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
	
	public String getTagInfNFeSupl() throws Exception {
		return "<infNFeSupl><qrCode><![CDATA["+montaQrCodeUrl()+"]]></qrCode>"
				+ "<urlChave>"+urlService.getUrlConsultaNFCe()+"</urlChave>"
				+ "</infNFeSupl>";
	}
}