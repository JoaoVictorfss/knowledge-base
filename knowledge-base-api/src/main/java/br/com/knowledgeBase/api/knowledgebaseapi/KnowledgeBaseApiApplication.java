package br.com.knowledgeBase.api.knowledgebaseapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class KnowledgeBaseApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KnowledgeBaseApiApplication.class, args);
	}

}
