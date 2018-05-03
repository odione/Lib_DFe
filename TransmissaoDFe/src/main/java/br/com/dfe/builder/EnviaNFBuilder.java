package br.com.dfe.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dfe.api.AssinaDocumento;
import br.com.dfe.api.DadosBuilder;
import br.com.dfe.api.XMLConverter;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.schema.TEnviNFe;
import br.com.dfe.schema.TNFe;
import br.com.dfe.service.QrCodeService;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class EnviaNFBuilder implements DadosBuilder<TEnviNFe> {
	
	@Autowired private XMLConverter xmlConverter;
	@Autowired private AssinaDocumento assinador;
	@Autowired private DadosEmissor dadosEmissor;
	@Autowired private QrCodeService qrCodeService;
	
	private TEnviNFe envNfe;
	private String xml;
	
	public EnviaNFBuilder comNFe(TNFe nfe) {
		envNfe = new TEnviNFe();
		envNfe.setIdLote("1");
		envNfe.setIndSinc(dadosEmissor.isAsync() ? "0" :  "1");
		envNfe.setVersao(dadosEmissor.getVersao());
		envNfe.getNFe().add(nfe);
		return this;
	}
	
	public EnviaNFBuilder assina() throws Exception {
		xml = xmlConverter.toString(envNfe, false);
		xml = assinador.assinarEnvNFe(xml, dadosEmissor.getCertificado(), dadosEmissor.getPrivateKey());
		log.debug("Assinado: "+xml);
		envNfe = xmlConverter.toObj(xml, TEnviNFe.class);
		return this;
	}

	@Override
	public TEnviNFe build() {
		return envNfe;
	}
	
	public String colocaQrCodeSeNFCe(String xmlAssinado) throws Exception {
		if (dadosEmissor.getModelo().equals("65")) {
			qrCodeService.setNfe(envNfe.getNFe().get(0));
			String tag = qrCodeService.getTagInfNFeSupl();
			log.debug("TAG QrCode: "+tag);
			StringBuilder sb = new StringBuilder(xmlAssinado);
			sb.insert(sb.lastIndexOf("</infNFe>")+9, tag);
			return sb.toString();
		}
		return xmlAssinado;
	}
}