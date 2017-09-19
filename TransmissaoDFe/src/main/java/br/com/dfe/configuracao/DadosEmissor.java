package br.com.dfe.configuracao;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import br.com.dfe.api.TipoEmissao;
import br.com.dfe.utils.ConverterUtils;
import br.com.utils.StringUtils;
import lombok.Data;

@Component
@Data
public class DadosEmissor implements Serializable {
	private static final long serialVersionUID = 1L;

	private String uf;
	private String ufCodigo;
	private String modelo;
	private String versao;
	private int ambiente;
	private X509Certificate certificado;
	private PrivateKey privateKey;
	private String pathCacerts;
	private TipoEmissao tipoEmissao;
	
	@PostConstruct
	public void postConstruct() {
		this.modelo = "55";
		this.versao = "4.00";
		this.ambiente = 2;
		this.tipoEmissao = TipoEmissao.NORMAL;
	}
	
	public void setUf(String uf) {
		this.uf = StringUtils.strUpper(uf);
		this.ufCodigo = ConverterUtils.UFStrToUFCodigo(uf);
	}
	public String getAmbienteStr() {
		return String.valueOf(ambiente);
	}
	public void setTipoEmissao(int tipo) {
		this.tipoEmissao = TipoEmissao.getFromInt(tipo);
	}
}