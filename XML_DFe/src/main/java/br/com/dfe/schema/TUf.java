//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementa��o de Refer�ncia (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modifica��es neste arquivo ser�o perdidas ap�s a recompila��o do esquema de origem. 
// Gerado em: 2015.12.02 �s 04:05:39 PM BRST 
//


package br.com.dfe.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "TUf")
@XmlEnum
public enum TUf {

    AC,
    AL,
    AM,
    AP,
    BA,
    CE,
    DF,
    ES,
    GO,
    MA,
    MG,
    MS,
    MT,
    PA,
    PB,
    PE,
    PI,
    PR,
    RJ,
    RN,
    RO,
    RR,
    RS,
    SC,
    SE,
    SP,
    TO,
    EX;

    public String value() {
        return name();
    }

    public static TUf fromValue(String v) {
        return valueOf(v);
    }
}
