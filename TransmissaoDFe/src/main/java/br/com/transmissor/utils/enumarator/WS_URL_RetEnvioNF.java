package br.com.transmissor.utils.enumarator;

public enum WS_URL_RetEnvioNF {
	
	//55 - NF-e
	SP_HOMOLOGACAO("https://homologacao.nfe.fazenda.sp.gov.br/ws/nferetautorizacao.asmx","SP",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	SP_PRODUCAO("https://nfe.fazenda.sp.gov.br/ws/nferetautorizacao.asmx","SP",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE);
	
	private String url;
	private String uf;
	private Ambiente ambiente;
	private TipoEmissao tipoEmissao;
	private ModeloDF modelo;
	
	private WS_URL_RetEnvioNF(String url, String uf, Ambiente ambiente, TipoEmissao tipoEmissao, ModeloDF modelo) {
		this.url = url;
		this.uf = uf;
		this.setAmbiente(ambiente);
		this.setTipoEmissao(tipoEmissao);
		this.setModelo(modelo);
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public Ambiente getAmbiente() {
		return ambiente;
	}
	public void setAmbiente(Ambiente ambiente) {
		this.ambiente = ambiente;
	}
	public TipoEmissao getTipoEmissao() {
		return tipoEmissao;
	}
	public void setTipoEmissao(TipoEmissao tipoEmissao) {
		this.tipoEmissao = tipoEmissao;
	}
	public ModeloDF getModelo() {
		return modelo;
	}
	public void setModelo(ModeloDF modelo) {
		this.modelo = modelo;
	}
}