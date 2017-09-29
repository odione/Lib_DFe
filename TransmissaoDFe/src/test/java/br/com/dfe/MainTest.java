package br.com.dfe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.dfe.configuracao.BeanConfiguracao;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=BeanConfiguracao.class)
@ActiveProfiles("dev")
public abstract class MainTest {

	@Test
	public void contextLoads() throws Exception {
    }
}