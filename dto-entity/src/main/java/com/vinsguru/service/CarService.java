package com.vinsguru.service;

import com.vinsguru.dto.CarDto;
import com.vinsguru.dto.EngineDto;
import com.vinsguru.entity.Car;
import com.vinsguru.entity.Engine;
import com.vinsguru.mapper.CarMapper;
import org.springframework.stereotype.Service;

@Service
public class CarService {

    private final CarMapper carMapper;

    private CarService(CarMapper carMapper) {
        this.carMapper = carMapper;
    }

    public CarDto getCarDto(Car car) {
        return carMapper.toDto(car);
    }

    public EngineDto getEngineDto(Engine engine) {
        return carMapper.toDto(engine);
    }
}

//    Engine engine = new Engine("Skoda");
////Car car = new Car(1l, "", 4, new Date(), engine);
//		System.out.println(carMapper.toDto(engine).toString());
