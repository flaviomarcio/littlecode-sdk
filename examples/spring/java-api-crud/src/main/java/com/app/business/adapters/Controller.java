package com.app.business.adapters;

import com.app.business.dto.ItemModelDTO;
import com.app.business.service.CRUDService;
import com.littlecode.parsers.PrimitiveUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crud")
@RequiredArgsConstructor
public class Controller {

    private final CRUDService crudService;

    @GetMapping("/get")
    public ResponseEntity<?> get(@RequestParam String id) {
        return crudService.get(PrimitiveUtil.toUUID(id)).asResultHttp();
    }

    @GetMapping("/list")
    public ResponseEntity<?> list() {
        return crudService.list().asResultHttp();
    }

    @PostMapping("/save")
    public ResponseEntity<?> post(@RequestBody ItemModelDTO in) {
        return crudService.saveIn(in).asResultHttp();
    }

    @PutMapping("/save")
    public ResponseEntity<?> put(@RequestBody ItemModelDTO in) {
        return crudService.saveIn(in).asResultHttp();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody String id) {
        return crudService.delete(id).asResultHttp();
    }
}