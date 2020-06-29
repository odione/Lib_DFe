package br.com.dfe.integrador;

import java.util.ArrayList;
import java.util.Random;

public class IntegradorFactory {

    private static Random random = new Random();

    public static Integrador newInstance(String metodoName) {
        String randomInt = String.valueOf(random.nextInt(999999));

        Identificador identificador = Identificador.builder()
            .valor(randomInt)
            .build();

        Metodo metodo = Metodo.builder()
            .nome(metodoName)
            .parametros(new Parametros())
            .build();

        Componente componente = Componente.builder()
            .nome("NFCE")
            .metodo(metodo)
            .build();

        Integrador integrador = Integrador.builder()
            .identificador(identificador)
            .componente(componente)
            .build();

        integrador.addParametro("numeroSessao", randomInt);
        integrador.addParametro("versaoDados", "4.00");
        integrador.addParametro("cUF", "23");

        return integrador;
    }
}
