package com.vinsguru;

import com.vinsguru.dto.CarDto;
import com.vinsguru.entity.Car;
import com.vinsguru.entity.Engine;
import com.vinsguru.mapper.CarMapper;
import com.vinsguru.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLOutput;
import java.util.Date;

@SpringBootApplication
public class DtoEntityApplication implements CommandLineRunner {

	@Autowired
	private CarService carService;

	public static void main(String[] args) {
		SpringApplication.run(DtoEntityApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		Engine engine = new Engine("Skoda");
		//Car car = new Car(1l, "", 4, new Date(), engine);
		System.out.println(carService.getEngineDto(engine).toString());
	}
}
