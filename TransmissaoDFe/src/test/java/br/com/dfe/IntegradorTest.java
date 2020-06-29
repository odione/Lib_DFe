package br.com.dfe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IntegradorTest {

    @Test
    void formatData() {
        String dataGeracao = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Assertions.assertNotNull(dataGeracao);
    }
}
