package br.com.dfe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lombok.Getter;
import br.com.dfe.api.Servico;

@Service
@Getter
public class HubServicos {

	@Autowired
	@Qualifier("statusService")
	private Servico status;

	@Autowired
	@Qualifier("consultaNFService")
	private Servico consulta;

	@Autowired
	@Qualifier("evento")
	private Servico evento;

	@Autowired
	@Qualifier("inutilizacao")
	private Servico inutilizacao;

	@Autowired
	@Qualifier("enviaNF")
	private Servico enviaNF;

	@Autowired
	@Qualifier("retornoAutorizacao")
	private Servico retornoAutorizacao;
}