package br.com.dfe.ws;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RetornoWS implements Serializable {
	private static final long serialVersionUID = 4000779961065466175L;

	private String stat;
	private String msg;
}