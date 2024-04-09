package com.app.business.adapters.controller;

import com.app.business.domain.CRUDConst;
import com.app.business.dto.TargetIn;
import com.app.business.service.crud.CRUDTargetService;
import com.littlecode.parsers.PrimitiveUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crud/target")
@RequiredArgsConstructor
public class CRUDTargetController {

    private final CRUDTargetService notifyTargetCRUD;

    @PostMapping("/")
    public ResponseEntity<?> post(@RequestBody TargetIn in) {
        return ResponseEntity.ok(notifyTargetCRUD.saveIn(in));
    }

    @PutMapping("/")
    public ResponseEntity<?> put(@RequestBody TargetIn in) {
        return ResponseEntity.ok(notifyTargetCRUD.saveIn(in));
    }

    @GetMapping()
    public ResponseEntity<?> find(@RequestParam String id) {
        return ResponseEntity.ok(notifyTargetCRUD.findIn(PrimitiveUtil.toUUID(id)));
    }

    @DeleteMapping("/")
    public ResponseEntity<?> disabled(@RequestParam String id) {
        return ResponseEntity.ok(notifyTargetCRUD.disable(id));
    }

    @GetMapping(CRUDConst.PATH_LIST)
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(notifyTargetCRUD.list());
    }

    @GetMapping("/dispatchers")
    public ResponseEntity<?> dispatchers() {
        return ResponseEntity.ok(notifyTargetCRUD.dispatchers());
    }
}