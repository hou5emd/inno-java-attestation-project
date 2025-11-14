package ru.inno.attestation.attestation03;

import org.springframework.boot.SpringApplication;

public class TestAttestation03Application {

	public static void main(String[] args) {
		SpringApplication.from(Attestation03Application::main).with(TestcontainersConfiguration.class).run(args);
	}

}
