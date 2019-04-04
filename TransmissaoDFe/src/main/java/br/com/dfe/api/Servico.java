package br.com.dfe.api;

import br.com.dfe.configuracao.DadosRequisicao;

public interface Servico {
	MetodoWS getMetodo();
	String getDados(DadosRequisicao dadosArquivo) throws Exception;
	DadosRequisicao createDados(String raw) throws Exception;
}