package com.example.reservationclient.controller;

import com.example.reservationclient.entity.Reservation;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservations")
public class ReservationApiGatewayRestController {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

    public Collection<String> getReservationNamesFallback() {
        return new ArrayList<>();
    }

    @HystrixCommand(fallbackMethod = "getReservationNamesFallback")
    @RequestMapping(method = RequestMethod.GET, value = "/names")
    public Collection<String> getReservationNames() {

        ResponseEntity<Resources<Reservation>> response = restTemplate.exchange("http://localhost:9999/reservation-service/reservations", HttpMethod.GET,
                null, new ParameterizedTypeReference<Resources<Reservation>>(){});

        return response.getBody().getContent().stream().map(Reservation::getReservationName).collect(Collectors.toList());
    }

}
