package br.com.dfe.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TRetInutNFe", namespace = "http://www.portalfiscal.inf.br/nfe", propOrder = {
    "infInut",
    "signature"
})
public class TRetInutNFe {

    @XmlElement(required = true)
    protected TRetInutNFe.InfInut infInut;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected SignatureType signature;
    @XmlAttribute(name = "versao", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String versao;
    
    public boolean isAutorizado() {
    	return "102".contains(getInfInut().getCStat());
    }

    public TRetInutNFe.InfInut getInfInut() {
        return infInut;
    }
    public void setInfInut(TRetInutNFe.InfInut value) {
        this.infInut = value;
    }
    public SignatureType getSignature() {
        return signature;
    }
    public void setSignature(SignatureType value) {
        this.signature = value;
    }
    public String getVersao() {
        return versao;
    }
    public void setVersao(String value) {
        this.versao = value;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "tpAmb",
        "verAplic",
        "cStat",
        "xMotivo",
        "cuf",
        "ano",
        "cnpj",
        "mod",
        "serie",
        "nnfIni",
        "nnfFin",
        "dhRecbto",
        "nProt"
    })
    public static class InfInut {

        @XmlElement(namespace = "http://www.portalfiscal.inf.br/nfe", required = true)
        protected String tpAmb;
        @XmlElement(namespace = "http://www.portalfiscal.inf.br/nfe", required = true)
        protected String verAplic;
        @XmlElement(namespace = "http://www.portalfiscal.inf.br/nfe", required = true)
        protected String cStat;
        @XmlElement(namespace = "http://www.portalfiscal.inf.br/nfe", required = true)
        protected String xMotivo;
        @XmlElement(name = "cUF", namespace = "http://www.portalfiscal.inf.br/nfe", required = true)
        protected String cuf;
        @XmlElement(namespace = "http://www.portalfiscal.inf.br/nfe")
        protected String ano;
        @XmlElement(name = "CNPJ", namespace = "http://www.portalfiscal.inf.br/nfe")
        protected String cnpj;
        @XmlElement(namespace = "http://www.portalfiscal.inf.br/nfe")
        protected String mod;
        @XmlElement(namespace = "http://www.portalfiscal.inf.br/nfe")
        protected String serie;
        @XmlElement(name = "nNFIni", namespace = "http://www.portalfiscal.inf.br/nfe")
        protected String nnfIni;
        @XmlElement(name = "nNFFin", namespace = "http://www.portalfiscal.inf.br/nfe")
        protected String nnfFin;
        @XmlElement(namespace = "http://www.portalfiscal.inf.br/nfe", required = true)
        protected String dhRecbto;
        @XmlElement(namespace = "http://www.portalfiscal.inf.br/nfe")
        protected String nProt;
        @XmlAttribute(name = "Id")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlID
        @XmlSchemaType(name = "ID")
        protected String id;

        public String getTpAmb() {
            return tpAmb;
        }
        public void setTpAmb(String value) {
            this.tpAmb = value;
        }
        public String getVerAplic() {
            return verAplic;
        }
        public void setVerAplic(String value) {
            this.verAplic = value;
        }
        public String getCStat() {
            return cStat;
        }
        public void setCStat(String value) {
            this.cStat = value;
        }
        public String getXMotivo() {
            return xMotivo;
        }
        public void setXMotivo(String value) {
            this.xMotivo = value;
        }
        public String getCUF() {
            return cuf;
        }
        public void setCUF(String value) {
            this.cuf = value;
        }
        public String getAno() {
            return ano;
        }
        public void setAno(String value) {
            this.ano = value;
        }
        public String getCNPJ() {
            return cnpj;
        }
        public void setCNPJ(String value) {
            this.cnpj = value;
        }
        public String getMod() {
            return mod;
        }
        public void setMod(String value) {
            this.mod = value;
        }
        public String getSerie() {
            return serie;
        }
        public void setSerie(String value) {
            this.serie = value;
        }
        public String getNNFIni() {
            return nnfIni;
        }
        public void setNNFIni(String value) {
            this.nnfIni = value;
        }
        public String getNNFFin() {
            return nnfFin;
        }
        public void setNNFFin(String value) {
            this.nnfFin = value;
        }
        public String getDhRecbto() {
            return dhRecbto;
        }
        public void setDhRecbto(String value) {
            this.dhRecbto = value;
        }
        public String getNProt() {
            return nProt;
        }
        public void setNProt(String value) {
            this.nProt = value;
        }
        public String getId() {
            return id;
        }
        public void setId(String value) {
            this.id = value;
        }
    }
}