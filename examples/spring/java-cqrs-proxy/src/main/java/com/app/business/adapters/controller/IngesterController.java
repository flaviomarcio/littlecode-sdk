package com.app.business.adapters.controller;

import com.app.business.dto.EventIn;
import com.app.business.service.event.EventIngesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class IngesterController {

    private final EventIngesterService notifyIngesterService;

    @PostMapping("/insert")
    public ResponseEntity<?> insert(@RequestBody EventIn eventIn) {
        return notifyIngesterService.register(eventIn).asResultHttp();
    }

}