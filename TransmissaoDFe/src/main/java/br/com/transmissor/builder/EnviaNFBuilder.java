package br.com.transmissor.builder;

import br.com.dfe.schema.TEnviNFe;
import br.com.dfe.schema.TNFe;
import br.com.transmissor.api.AssinaDocumento;
import br.com.transmissor.api.DadosBuilder;
import br.com.transmissor.api.XMLConverter;
import br.com.transmissor.configuracao.DadosEmissor;
import br.com.transmissor.impl.AssinaXML;

public class EnviaNFBuilder implements DadosBuilder {
	
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
		return this;
	}
	
	public EnviaNFBuilder assina() {
		String xml;
		try {
			xml = xmlConverter.toString(envNfe, false);
			xml = assinador.assinarEnvNFe(xml, dadosEmissor.getCertificado(), dadosEmissor.getPrivateKey());
			envNfe = xmlConverter.toObj(xml, TEnviNFe.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public Object build() {
		return envNfe;
	}
}