package br.com.dfe.builder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.dfe.api.AssinaDocumento;
import br.com.dfe.api.DadosBuilder;
import br.com.dfe.api.XMLConverter;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.impl.AssinaXML;
import br.com.dfe.schema.TEnviNFe;
import br.com.dfe.schema.TNFe;
import br.com.dfe.service.QrCodeService;

public class EnviaNFBuilder implements DadosBuilder {
	
	private final Logger log = LogManager.getLogger(EnviaNFBuilder.class);
	
	private TEnviNFe envNfe;
	private XMLConverter xmlConverter;
	private AssinaDocumento assinador;
	private DadosEmissor dadosEmissor;
	
	public EnviaNFBuilder(DadosEmissor dadosEmissor, XMLConverter xmlConverter) {
		this.xmlConverter = xmlConverter;
		this.dadosEmissor = dadosEmissor;
		this.assinador = new AssinaXML();
		
		envNfe = new TEnviNFe();
		envNfe.setIdLote("1");
		envNfe.setIndSinc("1");
		envNfe.setVersao(dadosEmissor.getVersao());
	}
	
	public EnviaNFBuilder comNFe(TNFe nfe) {
		envNfe.getNFe().add(nfe);
		dadosEmissor.setModelo(nfe.getInfNFe().getIde().getMod());
		return this;
	}
	
	public EnviaNFBuilder assina() {
		String xml;
		try {
			xml = xmlConverter.toString(envNfe, false);
			xml = assinador.assinarEnvNFe(xml, dadosEmissor.getCertificado(), dadosEmissor.getPrivateKey());
			log.debug(xml);
			envNfe = xmlConverter.toObj(xml, TEnviNFe.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public Object build() {
		colocaQrCode();
		return envNfe;
	}
	
	public void colocaQrCode() {
		if (dadosEmissor.getModelo().equals("55")) {
			return;
		}
		
		QrCodeService.builder()
				.dadosEmissor(dadosEmissor)
				.nfe(envNfe.getNFe().get(0))
				.build()
				.colocaQrCode();
	}
}