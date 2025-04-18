package com.shakhawat.myedu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoAuditing
public class MyeduApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyeduApplication.class, args);
	}

}
