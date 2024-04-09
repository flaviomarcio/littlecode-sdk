package com.app.business.adapters.controller;

import com.app.business.domain.CRUDConst;
import com.app.business.dto.GroupIn;
import com.app.business.service.crud.CRUDGroupService;
import com.littlecode.parsers.PrimitiveUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crud/group")
@RequiredArgsConstructor
public class CRUDProducerController {

    private final CRUDGroupService notifyGroupCRUD;

    @PostMapping("/")
    public ResponseEntity<?> post(@RequestBody GroupIn in) {
        return notifyGroupCRUD.saveIn(in).asResultHttp();
    }

    @PutMapping("/")
    public ResponseEntity<?> put(@RequestBody GroupIn in) {
        return notifyGroupCRUD.saveIn(in).asResultHttp();
    }

    @GetMapping()
    public ResponseEntity<?> find(@RequestParam String id) {
        return notifyGroupCRUD.findIn(PrimitiveUtil.toUUID(id)).asResultHttp();
    }

    @DeleteMapping("/")
    public ResponseEntity<?> disabled(@RequestParam String id) {
        return notifyGroupCRUD.disable(id).asResultHttp();
    }

    @GetMapping(CRUDConst.PATH_LIST)
    public ResponseEntity<?> list() {
        return notifyGroupCRUD.list().asResultPagination();
    }
}