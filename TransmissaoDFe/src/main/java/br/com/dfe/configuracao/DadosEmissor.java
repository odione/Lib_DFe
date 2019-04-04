package br.com.dfe.configuracao;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import br.com.dfe.api.TipoEmissao;
import br.com.dfe.utils.ConverterUtils;
import lombok.Data;

@Component
@Data
public class DadosEmissor implements Serializable {
	private static final long serialVersionUID = 1L;

	private String uf;
	private String ufCodigo;
	private String versao;
	private int ambiente;
	private X509Certificate certificado;
	private PrivateKey privateKey;
	private String pathCacerts;
	private TipoEmissao tipoEmissao;
	private String idCSC;
	private String CSC;
	
	@PostConstruct
	public void postConstruct() {
		this.versao = "4.00";
		this.tipoEmissao = TipoEmissao.NORMAL;
	}
	
	public void setUf(String uf) {
		this.uf = StringUtils.upperCase(uf);
		this.ufCodigo = ConverterUtils.UFStrToUFCodigo(uf);
	}
	public void setTipoEmissao(int tipo) {
		this.tipoEmissao = TipoEmissao.getFromInt(tipo);
	}
	public boolean isAsync() {
		return "BA|SP".contains(uf.toUpperCase());
	}
}