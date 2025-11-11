package com.barberApp.scheduleService.infra.clients;

import com.barberApp.scheduleService.dtos.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service", url = "${user.service.url:http://localhost:8081}")
public interface UserServiceClient {

    @GetMapping("/user/{id}")
    UserDTO getUser(@PathVariable("id") UUID id);

    @GetMapping("/user/{id}/is-barber")
    Boolean isBarber(@PathVariable("id") UUID id);
}

