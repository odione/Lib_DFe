package br.com.dfe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import br.com.dfe.api.MetodoWS;
import br.com.dfe.api.Servico;
import br.com.dfe.api.XMLConverter;
import br.com.dfe.builder.EnviaNFBuilder;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.schema.TEnviNFe;
import br.com.dfe.schema.TNFe;

@Service("enviaNF")
public class EnviaNFService implements Servico {
	
	@Autowired
	@Qualifier("enviaNFWS")
	private MetodoWS metodoWS;
	
	@Autowired private DadosEmissor dadosEmissor;
	@Autowired private XMLConverter xmlConverter;
	@Autowired private EnviaNFBuilder builder;
	
	private TNFe nfe;
	
	public EnviaNFService setNFe(String tNfe) throws Exception {
		this.nfe = xmlConverter.toObj(tNfe, TNFe.class);
		this.dadosEmissor.setModelo(this.nfe.getInfNFe().getIde().getMod());
		return this;
	}

	@Override
	public MetodoWS getMetodo() {
		return this.metodoWS;
	}

	@Override
	public String getDados() throws Exception {
		TEnviNFe envio = builder
				.comNFe(nfe)
				.assina()
				.colocaQrCodeSeNFCe()
				.build();
		
		return xmlConverter.toString(envio, false);
	}
}