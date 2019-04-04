package br.com.dfe.configuracao;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DadosResposta<T> {
	private String xmlEnviado;
	private T retorno;
}
