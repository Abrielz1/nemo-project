package ru.nemo_project.nemo_project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class NemoProjectApplicationTests {

	@Test
	void contextLoads() {
	}

}
