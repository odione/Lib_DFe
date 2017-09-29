package br.com.dfe.service;

import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.schema.TNFe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class QrCodeService {

	private TNFe nfe;
	private DadosEmissor dadosEmissor;
	
	public void colocaQrCode() {
		//return nfe;
		System.out.println("\n\n"+new String(nfe.getSignature().getSignedInfo().getReference().getDigestValue())); 
	}
}