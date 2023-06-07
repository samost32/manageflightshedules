package com.example.manageflightshedules;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Flight {
    private final SimpleStringProperty date;
    private final SimpleStringProperty time;
    private final SimpleStringProperty from;
    private final SimpleStringProperty to;
    private final SimpleStringProperty flightNumber;
    private final SimpleStringProperty aircraft;
    private final SimpleDoubleProperty economyPrice;
    private final SimpleDoubleProperty businessPrice;
    private final SimpleDoubleProperty firstClassPrice;

    public Flight(String date, String time, String from, String to, String flightNumber, String aircraft,
                  double economyPrice, double businessPrice, double firstClassPrice) {
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.from = new SimpleStringProperty(from);
        this.to = new SimpleStringProperty(to);
        this.flightNumber = new SimpleStringProperty(flightNumber);
        this.aircraft = new SimpleStringProperty(aircraft);
        this.economyPrice = new SimpleDoubleProperty(economyPrice);
        this.businessPrice = new SimpleDoubleProperty(businessPrice);
        this.firstClassPrice = new SimpleDoubleProperty(firstClassPrice);
    }

    // Геттеры для свойств

    public String getDate() {
        return date.get();
    }

    public String getTime() {
        return time.get();
    }

    public String getFrom() {
        return from.get();
    }

    public String getTo() {
        return to.get();
    }

    public String getFlightNumber() {
        return flightNumber.get();
    }

    public String getAircraft() {
        return aircraft.get();
    }

    public double getEconomyPrice() {
        return economyPrice.get();
    }

    public double getBusinessPrice() {
        return businessPrice.get();
    }

    public double getFirstClassPrice() {
        return firstClassPrice.get();
    }

    // Свойства для привязки данных

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }

    public SimpleStringProperty fromProperty() {
        return from;
    }

    public SimpleStringProperty toProperty() {
        return to;
    }

    public SimpleStringProperty flightNumberProperty() {
        return flightNumber;
    }

    public SimpleStringProperty aircraftProperty() {
        return aircraft;
    }

    public SimpleDoubleProperty economyPriceProperty() {
        return economyPrice;
    }

    public SimpleDoubleProperty businessPriceProperty() {
        return businessPrice;
    }

    public SimpleDoubleProperty firstClassPriceProperty() {
        return firstClassPrice;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public void setEconomyPrice(double economyPrice) {
        this.economyPrice.set(economyPrice);
    }
}
