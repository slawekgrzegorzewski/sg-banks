package pl.sg;

import org.springframework.boot.SpringApplication;

public class TestSgGoCardlessIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.from(BanksApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
