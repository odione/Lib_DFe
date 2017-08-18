package br.com.transmissor;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import br.com.transmissor.utils.enumarator.Ambiente;
import br.com.transmissor.utils.enumarator.Estado;
import br.com.transmissor.utils.enumarator.ModeloDF;
import br.com.transmissor.utils.enumarator.TipoEmissao;
import br.com.transmissor.utils.enumarator.VersaoDF;

public class ConfigDFe {
	
	private X509Certificate certificado;
	private PrivateKey privateKey;
	private String senha_certificado;
	private Estado uf;
	private Ambiente ambiente;
	private ModeloDF modelo_df;
	private VersaoDF versaoDF;
	private TipoEmissao tipoEmissao;
	private boolean atualizaXMLConsulta;
	private boolean horarioDeVerao;
	private String idCSC;
	private String CSC;
	
	public ConfigDFe() {
		super();
		senha_certificado = "";
		uf = Estado.SANTA_CATARINA;
		ambiente = Ambiente.HOMOLOGACAO;
		modelo_df = ModeloDF.MODELO_NFE;
		versaoDF = VersaoDF.VERSAO_310;
		tipoEmissao = TipoEmissao.NORMAL;
		atualizaXMLConsulta = true;
		horarioDeVerao = false;
		idCSC = "";
		CSC = "";
	}
	
	public ConfigDFe(X509Certificate certificado, PrivateKey privateKey, String senha_certificado, Estado uf,
			Ambiente ambiente, ModeloDF modelo_df, VersaoDF versaoDF, TipoEmissao tipoEmissao, String idCSC, String CSC) {
		super();
		this.certificado = certificado;
		this.privateKey = privateKey;
		this.senha_certificado = senha_certificado;
		this.uf = uf;
		this.ambiente = ambiente;
		this.modelo_df = modelo_df;
		this.versaoDF = versaoDF;
		this.tipoEmissao = tipoEmissao;
		this.idCSC = idCSC;
		this.CSC = CSC;
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
	public String getSenha_certificado() {
		return senha_certificado;
	}
	public void setSenha_certificado(String senha_certificado) {
		this.senha_certificado = senha_certificado;
	}
	public Estado getUf() {
		return uf;
	}
	public void setUf(Estado uf) {
		this.uf = uf;
	}
	public Ambiente getAmbiente() {
		return ambiente;
	}
	public void setAmbiente(Ambiente ambiente) {
		this.ambiente = ambiente;
	}
	public ModeloDF getModelo_df() {
		return modelo_df;
	}
	public void setModelo_df(ModeloDF modelo_df) {
		this.modelo_df = modelo_df;
	}
	public VersaoDF getVersaoDF() {
		return versaoDF;
	}
	public void setVersaoDF(VersaoDF versaoDF) {
		this.versaoDF = versaoDF;
	}
	public TipoEmissao getTipoEmissao() {
		return tipoEmissao;
	}
	public void setTipoEmissao(TipoEmissao tipoEmissao) {
		this.tipoEmissao = tipoEmissao;
	}
	public boolean isAtualizaXMLConsulta() {
		return atualizaXMLConsulta;
	}
	public void setAtualizaXMLConsulta(boolean atualizaXMLConsulta) {
		this.atualizaXMLConsulta = atualizaXMLConsulta;
	}
	public boolean isHorarioDeVerao() {
		return horarioDeVerao;
	}
	public void setHorarioDeVerao(boolean horarioDeVerao) {
		this.horarioDeVerao = horarioDeVerao;
	}
	public String getIdCSC() {
		return idCSC;
	}
	public void setIdCSC(String idCSC) {
		this.idCSC = idCSC;
	}
	public String getCSC() {
		return CSC;
	}
	public void setCSC(String cSC) {
		CSC = cSC;
	}
}