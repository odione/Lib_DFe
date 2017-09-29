package br.com.dfe.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.dfe.api.XMLConverter;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.schema.TNFe;
import lombok.Setter;

@Service
public class QrCodeService {

	@Autowired private DadosEmissor dadosEmissor;
	@Autowired private XMLConverter xmlConverter;
	@Autowired private URLService urlService;
	@Setter private TNFe nfe;
	
	private String xml;
	
	public void colocaQrCode() throws Exception {
		xml = xmlConverter.toString(nfe, false);
		String urlQrCode = montaQrCodeUrl(); 
		nfe.getInfNFeSupl().setUrlChave(urlQrCode);
		nfe.getInfNFeSupl().setQrCode(String.format("<![CDATA[%s]]>", urlQrCode));
	}
	
	public String getDigestValue() {
		int inicio = xml.lastIndexOf("<DigestValue>")+13;
		int fim = xml.lastIndexOf("</DigestValue>");
		return xml.substring(inicio, fim);
	}
	
	public String montaQrCodeUrl() throws NoSuchAlgorithmException {
		StringBuffer qrCode = new StringBuffer()
			.append("chNFe=").append(nfe.getInfNFe().getId().substring(3))
			.append("&nVersao=100")
			.append("&tpAmb=").append(nfe.getInfNFe().getIde().getTpAmb())
			.append(getDest())
			.append("&dhEmi=").append(getHex(nfe.getInfNFe().getIde().getDhEmi().getBytes()))
			.append("&vNF=").append(nfe.getInfNFe().getTotal().getICMSTot().getVNF())
			.append("&vICMS=").append(nfe.getInfNFe().getTotal().getICMSTot().getVICMS())
			.append("&digVal=").append(getHex(getDigestValue().getBytes()))
			.append("&cIdToken=").append(dadosEmissor.getIdCSC());
			
		qrCode.append(getHashQrCode(qrCode.toString()));
		return urlService.getUrlConsultaNFCe().concat("?").concat(qrCode.toString());
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
		return new String(Hex.encodeHex(value));
	}
	
	public String getHashSHA1(String value) throws NoSuchAlgorithmException{
		MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
		return getHex(mDigest.digest(value.getBytes()));
	}
}