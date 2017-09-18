package br.com.dfe.impl;

import org.springframework.stereotype.Component;

import br.com.dfe.api.NaoAutorizadoException;
import br.com.dfe.api.PosTransmissao;
import br.com.dfe.schema.TRetConsSitNFe;
import br.com.dfe.schema.TRetEnviNFe;
import br.com.dfe.schema.TRetInutNFe;
import br.com.dfe.schema.cce.TRetEnvEvento;

@Component
public class PosTransmissaoImpl implements PosTransmissao {
	
	@Override
	public void validaRetEnvNFe(TRetEnviNFe retorno) throws NaoAutorizadoException {
		String stat = retorno.getProtNFe() != null ? retorno.getProtNFe().getInfProt().getCStat() : retorno.getCStat();
		
		if (!STAT_AUTORIZADA.contains(stat)) {
			String msg = retorno.getProtNFe() == null ? retorno.getXMotivo() : retorno.getProtNFe().getInfProt().getXMotivo();
			
			throw new NaoAutorizadoException(stat, msg);
		}
	}

	@Override
	public void validaRetEnvEvento(TRetEnvEvento retorno) throws NaoAutorizadoException {
		String stat = retorno.getRetEvento().isEmpty() ? retorno.getCStat() : retorno.getRetEvento().get(0).getInfEvento().getCStat();
		
		if (!STAT_INUTILIZADA.contains(stat)) {
			String msg = retorno.getRetEvento().isEmpty() ? retorno.getXMotivo() : retorno.getRetEvento().get(0).getInfEvento().getXMotivo();
			
			throw new NaoAutorizadoException(stat, msg);
		}
	}

	@Override
	public void validaRetEnvEvento(br.com.dfe.schema.canc.TRetEnvEvento retorno) throws NaoAutorizadoException {
		String stat = retorno.getRetEvento().isEmpty() ? retorno.getCStat() : retorno.getRetEvento().get(0).getInfEvento().getCStat();
		
		if (!STAT_CANCELADA.contains(stat)) {
			String msg = retorno.getRetEvento().isEmpty() ? retorno.getXMotivo() : retorno.getRetEvento().get(0).getInfEvento().getXMotivo();
			
			throw new NaoAutorizadoException(stat, msg);
		}
	}

	@Override
	public void validaRetInutNFe(TRetInutNFe retorno) throws NaoAutorizadoException {
		String stat = retorno.getInfInut().getCStat();
		if (!STAT_INUTILIZADA.contains(stat)) {
			throw new NaoAutorizadoException(stat, retorno.getInfInut().getXMotivo());
		}
	}

	@Override
	public void validaRetConsSitNFe(TRetConsSitNFe retorno) throws NaoAutorizadoException {
		String stat = retorno.getCStat();
		
		if ((!STAT_AUTORIZADA.contains(stat)) && (!STAT_DENEGADA.contains(stat)) && (!STAT_CANCELADA.contains(stat)))  {
			throw new NaoAutorizadoException(stat, retorno.getXMotivo());
		}
	}
}