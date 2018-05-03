package br.com.dfe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import br.com.dfe.api.MetodoWS;
import br.com.dfe.api.Servico;
import br.com.dfe.api.XMLConverter;
import br.com.dfe.builder.EnviaNFBuilder;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.schema.TNFe;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Service("enviaNF")
@Log4j2
public class EnviaNFService implements Servico {
	
	@Autowired
	@Qualifier("enviaNFWS")
	private MetodoWS metodoWS;
	
	@Autowired private DadosEmissor dadosEmissor;
	@Autowired private XMLConverter xmlConverter;
	@Autowired private EnviaNFBuilder builder;
	@Autowired private QrCodeService qrCodeService;
	private String dados;
	
	private TNFe nfe;
	
	public EnviaNFService setNFe(String tNfe) throws Exception {
		dados = "";
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
		montaDados();
		return dados;
	}

	private void montaDados() throws Exception {
		if (!dados.equals("")) return;
		
		val envio = builder
				.comNFe(nfe)
				.assina()
				.build();
		
		qrCodeService.setNfe(envio.getNFe().get(0));
		
		dados = xmlConverter.toString(envio, false);
		dados = builder.colocaQrCodeSeNFCe(dados);
		log.debug("XML Assinado com QrCode: "+dados);
	}
}