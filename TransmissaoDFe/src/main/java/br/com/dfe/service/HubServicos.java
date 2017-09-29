package br.com.dfe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import br.com.dfe.api.Servico;
import lombok.Getter;

@Service
@Getter
public class HubServicos {

	@Autowired
	@Qualifier("statusService")
	private Servico status;
	
	@Autowired private ConsultaNFService consulta;
	@Autowired private EventoService evento;
	@Autowired private InutilizacaoService inutilizacao;
	@Autowired private EnviaNFService enviaNF;
}