package br.com.dfe.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dfe.api.DadosBuilder;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.schema.TConsSitNFe;
import lombok.Setter;

@Component
public class ConsultaNFBuilder implements DadosBuilder<TConsSitNFe> {
	
	private TConsSitNFe consulta;
	@Setter	private String chave;
	
	@Autowired private DadosEmissor dadosEmissor;
	
	private void consultaBuild() {
		consulta = new TConsSitNFe();
		consulta.setTpAmb(dadosEmissor.getAmbienteStr());
		consulta.setVersao(dadosEmissor.getVersao());
		consulta.setChNFe(chave);
		consulta.setXServ("CONSULTAR");
	}
	
	@Override
	public TConsSitNFe build() {
		consultaBuild();
		return consulta;
	}
}