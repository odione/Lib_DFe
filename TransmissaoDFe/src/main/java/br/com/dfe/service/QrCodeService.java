package br.com.dfe.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.dfe.api.XMLConverter;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.schema.TNFe;
import br.com.dfe.schema.TNFe.InfNFeSupl;
import lombok.Setter;

@Service
public class QrCodeService {
	private final Logger log = LogManager.getLogger(QrCodeService.class);

	@Autowired private DadosEmissor dadosEmissor;
	@Autowired private XMLConverter xmlConverter;
	@Autowired private URLService urlService;
	@Setter private TNFe nfe;
	
	private String xml;
	
	public void colocaQrCode() throws Exception {
		String urlQrCode = montaQrCodeUrl();
		
		nfe.setInfNFeSupl(new InfNFeSupl());
		nfe.getInfNFeSupl().setUrlChave(urlService.getUrlConsultaNF());
		nfe.getInfNFeSupl().setQrCode("<![CDATA["+urlQrCode+"]]>");
	}
	
	public String getDigestValue() throws Exception {
		xml = xmlConverter.toString(nfe, false);
		int inicio = xml.lastIndexOf("<DigestValue>")+13;
		int fim = xml.lastIndexOf("</DigestValue>");
		return xml.substring(inicio, fim);
	}
	
	public String montaQrCodeUrl() throws Exception {
		StringBuffer qrCode = new StringBuffer()
			.append("chNFe=").append(nfe.getInfNFe().getId().substring(3))
			.append("&nVersao=").append("100")
			.append("&tpAmb=").append(nfe.getInfNFe().getIde().getTpAmb())
			.append(getDest())
			.append("&dhEmi=").append(getHex(nfe.getInfNFe().getIde().getDhEmi().getBytes()))
			.append("&vNF=").append(nfe.getInfNFe().getTotal().getICMSTot().getVNF())
			.append("&vICMS=").append(nfe.getInfNFe().getTotal().getICMSTot().getVICMS())
			.append("&digVal=").append(getHex(getDigestValue().getBytes()))
			.append("&cIdToken=").append(dadosEmissor.getIdCSC());
			
		qrCode.append(getHashQrCode(qrCode.toString()));
		String urlCompleta = urlService.getUrlConsultaNFCe().concat("?").concat(qrCode.toString());
		log.debug("QrCode: "+urlCompleta);
		return urlCompleta;
	}

	private String getHashQrCode(String qrCode) throws NoSuchAlgorithmException {
		return "&cHashQRCode="+getHashSHA1(qrCode+dadosEmissor.getCSC());
	}

	private String getDest() {
		if (nfe.getInfNFe().getDest() != null) {
			return "&cDest="+nfe.getInfNFe().getDest().getCPF() != null ? 
				nfe.getInfNFe().getDest().getCPF() : 
				nfe.getInfNFe().getDest().getCNPJ();
		}
		return "";
	}
	
	private String getHex(byte[] value) {
		return new String(Hex.encodeHex(value)).toLowerCase();
	}
	
	public String getHashSHA1(String value) throws NoSuchAlgorithmException{
		MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
		return getHex(mDigest.digest(value.getBytes()));
	}
	
	public String getTagInfNFeSupl() throws Exception {
		return "<infNFeSupl><qrCode><![CDATA["+montaQrCodeUrl()+"]]></qrCode>"
				+ "<urlChave>"+urlService.getUrlConsultaNFCe()+"</urlChave>"
				+ "</infNFeSupl>";
	}
}