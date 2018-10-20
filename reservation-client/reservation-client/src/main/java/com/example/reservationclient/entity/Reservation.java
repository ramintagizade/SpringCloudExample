package com.example.reservationclient.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.web.bind.annotation.ResponseBody;


@JsonIgnoreProperties(ignoreUnknown = true)
@ResponseBody
public class Reservation {

    private String reservationName;

    public Reservation(String name) {
        this.reservationName = name;
    }

    public Reservation() {

    }

    public String getReservationName() {
        return reservationName;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationName='" + reservationName + '\'' +
                '}';
    }
}