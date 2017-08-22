package br.com.transmissor.configuracao;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class DadosEmissor implements Serializable {
	private static final long serialVersionUID = 1L;

	private String uf;
	private String ufCodigo;
	private String modelo;
	private String versao;
	private int ambiente;
	private X509Certificate certificado;
	private PrivateKey privateKey;
	
	public DadosEmissor(String uf, String ufCodigo, String modelo, String versao, int ambiente,
			X509Certificate certificado, PrivateKey privateKey) {
		super();
		this.uf = uf;
		this.ufCodigo = ufCodigo;
		this.modelo = modelo;
		this.versao = versao;
		this.ambiente = ambiente;
		this.certificado = certificado;
		this.privateKey = privateKey;
	}

	public DadosEmissor() {
		super();
	}
	
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public String getUfCodigo() {
		return ufCodigo;
	}
	public void setUfCodigo(String ufCodigo) {
		this.ufCodigo = ufCodigo;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public X509Certificate getCertificado() {
		return certificado;
	}
	public void setCertificado(X509Certificate certificado) {
		this.certificado = certificado;
	}
	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	public String getVersao() {
		return versao;
	}
	public void setVersao(String versao) {
		this.versao = versao;
	}
	public int getAmbiente() {
		return ambiente;
	}
	public String getAmbienteStr() {
		return String.valueOf(ambiente);
	}
	public void setAmbiente(int ambiente) {
		this.ambiente = ambiente;
	}
}