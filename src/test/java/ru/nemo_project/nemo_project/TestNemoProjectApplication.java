package ru.nemo_project.nemo_project;

import org.springframework.boot.SpringApplication;

public class TestNemoProjectApplication {

	public static void main(String[] args) {
		SpringApplication.from(NemoProjectApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
