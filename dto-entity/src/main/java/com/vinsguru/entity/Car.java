package com.vinsguru.entity;


import java.util.Date;

public class Car {

    private long id;
    private String make;
    private int numOfSeats;
    private Date releaseDate;
    private Engine engine;

    public Car(long id, String make, int numOfSeats, Date releaseDate, Engine engine) {
        this.id = id;
        this.make = make;
        this.numOfSeats = numOfSeats;
        this.releaseDate = releaseDate;
        this.engine = engine;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public int getNumOfSeats() {
        return numOfSeats;
    }

    public void setNumOfSeats(int numOfSeats) {
        this.numOfSeats = numOfSeats;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }
}
