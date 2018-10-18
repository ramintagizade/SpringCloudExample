package com.example.reservationclient;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.sun.net.httpserver.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.*;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@EnableCircuitBreaker
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class ReservationClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationClientApplication.class, args);
	}
}


@RestController
@RequestMapping("/reservations")
class ReservationApiGatewayRestController {

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

@JsonIgnoreProperties(ignoreUnknown = true)
@ResponseBody
class Reservation {

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