package tcc.enterprise.fakenewsbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FakeNewsBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(FakeNewsBotApplication.class, args);
	}

}
