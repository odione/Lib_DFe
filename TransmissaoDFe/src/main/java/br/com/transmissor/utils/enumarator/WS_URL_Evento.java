package br.com.transmissor.utils.enumarator;

public enum WS_URL_Evento {
	
	//55 - NF-e
	SVRS_HOMOLOGACAO("https://nfe-homologacao.svrs.rs.gov.br/ws/recepcaoevento/recepcaoevento.asmx","AC|AL|AP|DF|PB|RJ|RN|RO|RR|SC|SE|TO|ES",
		Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	SVRS_PRODUCAO("https://nfe.svrs.rs.gov.br/ws/recepcaoevento/recepcaoevento.asmx","AC|AL|AP|DF|PB|RJ|RN|RO|RR|SC|SE|TO|ES",
		Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	AM_HOMOLOGACAO("https://homnfe.sefaz.am.gov.br/services2/services/RecepcaoEvento","AM",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	AM_PRODUCAO("https://nfe.sefaz.am.gov.br/services2/services/RecepcaoEvento","AM",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	BA_HOMOLOGACAO("https://hnfe.sefaz.ba.gov.br/webservices/sre/recepcaoevento.asmx","BA",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	BA_PRODUCAO("https://nfe.sefaz.ba.gov.br/webservices/sre/recepcaoevento.asmx","BA",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	CE_HOMOLOGACAO("https://nfeh.sefaz.ce.gov.br/nfe2/services/RecepcaoEvento","CE",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	CE_PRODUCAO("https://nfe.sefaz.ce.gov.br/nfe2/services/RecepcaoEvento","CE",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	GO_HOMOLOGACAO("https://homolog.sefaz.go.gov.br/nfe/services/v2/RecepcaoEvento","GO",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	GO_PRODUCAO("https://nfe.sefaz.go.gov.br/nfe/services/v2/RecepcaoEvento","GO",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	MG_HOMOLOGACAO("https://hnfe.fazenda.mg.gov.br/nfe2/services/RecepcaoEvento","MG",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	MG_PRODUCAO("https://nfe.fazenda.mg.gov.br/nfe2/services/RecepcaoEvento","MG",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	MS_HOMOLOGACAO("https://homologacao.nfe.ms.gov.br/homologacao/services2/RecepcaoEvento","MS",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	MS_PRODUCAO("https://nfe.fazenda.ms.gov.br/producao/services2/RecepcaoEvento","MS",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	MT_HOMOLOGACAO("https://homologacao.sefaz.mt.gov.br/nfews/v2/services/RecepcaoEvento","MT",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	MT_PRODUCAO("https://nfe.sefaz.mt.gov.br/nfews/v2/services/RecepcaoEvento","MT",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	PE_HOMOLOGACAO("https://nfehomolog.sefaz.pe.gov.br/nfe-service/services/RecepcaoEvento","PE",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	PE_PRODUCAO("https://nfe.sefaz.pe.gov.br/nfe-service/services/RecepcaoEvento","PE",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	PR_HOMOLOGACAO("https://homologacao.nfe.fazenda.pr.gov.br/nfe/NFeRecepcaoEvento","PR",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	PR_PRODUCAO("https://nfe.fazenda.pr.gov.br/nfe/NFeRecepcaoEvento","PR",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	RS_HOMOLOGACAO("https://nfe-homologacao.sefazrs.rs.gov.br/ws/recepcaoevento/recepcaoevento.asmx","RS",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	RS_PRODUCAO("https://nfe.sefazrs.rs.gov.br/ws/recepcaoevento/recepcaoevento.asmx","RS",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	SP_HOMOLOGACAO("https://homologacao.nfe.fazenda.sp.gov.br/ws/recepcaoevento.asmx","SP",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	SP_PRODUCAO("https://nfe.fazenda.sp.gov.br/ws/recepcaoevento.asmx","SP",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	SVAN_HOMOLOGACAO("https://hom.sefazvirtual.fazenda.gov.br/RecepcaoEvento/RecepcaoEvento.asmx","MA|PA|PI",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	SVAN_PRODUCAO("https://www.sefazvirtual.fazenda.gov.br/RecepcaoEvento/RecepcaoEvento.asmx","MA|PA|PI",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	SVC_AN_HOMOLOGACAO("https://hom.svc.fazenda.gov.br/RecepcaoEvento/RecepcaoEvento.asmx","AC|AL|AP|DF|ES|MG|PB|RJ|RN|RO|RR|RS|SC|SE|SP|TO",
		Ambiente.HOMOLOGACAO,TipoEmissao.CONTINGENCIA_SVCAN,ModeloDF.MODELO_NFE),
	SVC_AN_PRODUCAO("https://www.svc.fazenda.gov.br/RecepcaoEvento/RecepcaoEvento.asmx","AC|AL|AP|DF|ES|MG|PB|RJ|RN|RO|RR|RS|SC|SE|SP|TO",
		Ambiente.PRODUCAO,TipoEmissao.CONTINGENCIA_SVCAN,ModeloDF.MODELO_NFE),
	SVC_RS_HOMOLOGACAO("https://nfe-homologacao.svrs.rs.gov.br/ws/recepcaoevento/recepcaoevento.asmx","AM|BA|CE|GO|MA|MS|MT|PA|PE|PI|PR",
		Ambiente.HOMOLOGACAO,TipoEmissao.CONTINGENCIA_SVRS,ModeloDF.MODELO_NFE),
	SVC_RS_PRODUCAO("https://nfe.svrs.rs.gov.br/ws/recepcaoevento/recepcaoevento.asmx","AM|BA|CE|GO|MA|MS|MT|PA|PE|PI|PR",
		Ambiente.PRODUCAO,TipoEmissao.CONTINGENCIA_SVRS,ModeloDF.MODELO_NFE),
	
	//65 - NFC-e
	SVRS_HOMOLOGACAO_NFC("https://nfce-homologacao.svrs.rs.gov.br/ws/recepcaoevento/recepcaoevento.asmx",
		"AC|AP|DF|PB|RJ|RN|RO|RR|SE|TO|BA|MA|PA|PI",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFCE),
	SVRS_PRODUCAO_NFC("https://nfce.svrs.rs.gov.br/ws/recepcaoevento/recepcaoevento.asmx",
		"AC|AP|DF|PB|RJ|RN|RO|RR|SE|TO|BA|MA|PA|PI",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFCE),
	AM_HOMOLOGACAO_NFC("https://homnfce.sefaz.am.gov.br/nfce-services/services/RecepcaoEvento","AM",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFCE),
	AM_PRODUCAO_NFC("https://nfce.sefaz.am.gov.br/nfce-services/services/RecepcaoEvento","AM",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFCE),
	MT_HOMOLOGACAO_NFC("https://homologacao.sefaz.mt.gov.br/nfcews/services/RecepcaoEvento","MT",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFCE),
	MT_PRODUCAO_NFC("https://nfce.sefaz.mt.gov.br/nfcews/services/RecepcaoEvento","MT",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFCE),
	PR_HOMOLOGACAO_NFC("https://homologacao.nfce.fazenda.pr.gov.br/nfce/NFeRecepcaoEvento","PR",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFCE),
	PR_PRODUCAO_NFC("https://nfce.fazenda.pr.gov.br/nfce/NFeRecepcaoEvento","PR",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFCE),
	RS_HOMOLOGACAO_NFC("https://nfce-homologacao.sefazrs.rs.gov.br/ws/recepcaoevento/recepcaoevento.asmx","RS",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFCE),
	RS_PRODUCAO_NFC("https://nfce.sefazrs.rs.gov.br/ws/recepcaoevento/recepcaoevento.asmx","RS",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFCE),
	SP_HOMOLOGACAO_NFC("https://homologacao.nfce.fazenda.sp.gov.br/ws/recepcaoevento.asmx","SP",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFCE),
	SP_PRODUCAO_NFC("https://nfce.fazenda.sp.gov.br/ws/recepcaoevento.asmx","SP",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFCE);
	
	private String url;
	private String uf;
	private Ambiente ambiente;
	private TipoEmissao tipoEmissao;
	private ModeloDF modelo;
	
	private WS_URL_Evento(String url, String uf, Ambiente ambiente, TipoEmissao tipoEmissao, ModeloDF modelo) {
		setUrl(url);
		setUf(uf);
		setAmbiente(ambiente);
		setTipoEmissao(tipoEmissao);
		setModelo(modelo);
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