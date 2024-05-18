package com.courses.microservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
@EntityScan({"com.commons.alumnos.models.entity",
		     "com.courses.microservices.models.entity",
		     "com.commonsexamns.entity"})
public class CoursesMicroservicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoursesMicroservicesApplication.class, args);
	}

}
