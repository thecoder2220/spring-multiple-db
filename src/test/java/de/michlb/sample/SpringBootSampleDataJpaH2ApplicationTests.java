package de.michlb.sample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.web.WebAppConfiguration;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootApplication
@WebAppConfiguration
public class SpringBootSampleDataJpaH2ApplicationTests {

	@Test
	public void contextLoads() {
	}

}
