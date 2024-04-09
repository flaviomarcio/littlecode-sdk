package com.app.business.adapters;

import com.app.business.dto.CommandDTO;
import com.app.business.service.ConsumerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class Controller {
    private final ConsumerService service;

    @GetMapping("/exec")
    public ResponseEntity<?> exec(@Valid @RequestBody CommandDTO commandDTO) {
        return service.register(commandDTO).asResultHttp();
    }
}