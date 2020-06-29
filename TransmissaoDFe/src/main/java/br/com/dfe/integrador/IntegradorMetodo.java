package br.com.dfe.integrador;

public enum IntegradorMetodo {
    STATUS_SERVICO("NfeStatusServico2Soap12", "HNfeStatusServico2Soap12"),
    ENVIO_NF("NfeAutorizacaoLote12", "HNfeAutorizacaoLote12"),
    CONSULTA_NF("NfeConsulta2Soap12", "HNfeConsulta2Soap12"),
    CANCELAMENTO("RecepcaoEvento", "HRecepcaoEvento"),
    INUTILIZACAO("NfeInutilizacao2Soap12", "HNfeInutilizacao2Soap12");

    public final String producao;
    public final String homologacao;

    IntegradorMetodo(String producao, String homologacao) {
        this.producao = producao;
        this.homologacao = homologacao;
    }

    public static String getStatusServico(int tpAmb) {
        return (tpAmb == 1) ? STATUS_SERVICO.producao : STATUS_SERVICO.homologacao;
    }

    public static String getEnvioNF(int tpAmb) {
        return (tpAmb == 1) ? ENVIO_NF.producao : ENVIO_NF.homologacao;
    }

    public static String getConsultaNF(int tpAmb) {
        return (tpAmb == 1) ? CONSULTA_NF.producao : CONSULTA_NF.homologacao;
    }

    public static String getCancelamento(int tpAmb) {
        return (tpAmb == 1) ? CANCELAMENTO.producao : CANCELAMENTO.homologacao;
    }

    public static String getInutilizacao(int tpAmb) {
        return (tpAmb == 1) ? INUTILIZACAO.producao : INUTILIZACAO.homologacao;
    }
}
