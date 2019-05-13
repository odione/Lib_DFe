package br.com.dfe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.dfe.api.AssinaDocumento;
import br.com.dfe.api.MetodoWS;
import br.com.dfe.api.Servico;
import br.com.dfe.api.TipoEmissao;
import br.com.dfe.api.XMLConverter;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.configuracao.DadosRequisicao;
import br.com.dfe.schema.TEnviNFe;
import br.com.dfe.schema.TNFe;
import br.com.dfe.ws.EnviaNFWS;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Service("enviaNF")
@Log4j2
public class EnviaNFService implements Servico {
	
	@Autowired private XMLConverter xmlConverter;
	@Autowired private QrCodeService qrCodeService;
	@Autowired private DadosEmissor dadosEmissor;
	@Autowired private AssinaDocumento assinador;
	
	@Override
	public MetodoWS getMetodo() {
		return new EnviaNFWS();
	}

	@Override
	public String getDados(DadosRequisicao dadosArquivo) throws Exception {
		TNFe nfe = xmlConverter.toObj(dadosArquivo.getRawFile(), TNFe.class);
		
		TEnviNFe envNfe = new TEnviNFe();
		envNfe.setIdLote("1");
		envNfe.setIndSinc(dadosEmissor.isAsync() ? "0" :  "1");
		envNfe.setVersao(dadosEmissor.getVersao());
		envNfe.getNFe().add(nfe);
		
		envNfe = assinaNFe(envNfe);
		
		qrCodeService.colocaQrCode(envNfe.getNFe().get(0), dadosArquivo);
		return xmlConverter.toString(envNfe, false);
	}

	private TEnviNFe assinaNFe(TEnviNFe envNfe) throws Exception {
		String xml = xmlConverter.toString(envNfe, false);
		xml = assinador.assinarEnvNFe(xml, dadosEmissor.getCertificado(), dadosEmissor.getPrivateKey());
		log.debug("XML Assinado");
		return xmlConverter.toObj(xml, TEnviNFe.class);
	}
	
	public DadosRequisicao createDados(@NonNull String xml) throws Exception {
		TNFe nfe = xmlConverter.toObj(xml, TNFe.class);
		return DadosRequisicao.builder()
				.rawFile(xml)
				.ambiente(Integer.parseInt(nfe.getInfNFe().getIde().getTpAmb()))
				.modelo(nfe.getInfNFe().getIde().getMod())
				.versao(nfe.getInfNFe().getVersao())
				.tipoEmissao(TipoEmissao.getFromStr(nfe.getInfNFe().getIde().getTpEmis()))
				.build();
	}
}