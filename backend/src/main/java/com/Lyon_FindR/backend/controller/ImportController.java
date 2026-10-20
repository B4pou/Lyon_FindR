package com.Lyon_FindR.backend.controller;

import com.Lyon_FindR.backend.dto.ImportReport;
import com.Lyon_FindR.backend.service.ImportService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class ImportController {

    private final ImportService importService;

    public ImportController(ImportService importService) {
        this.importService = importService;
    }

    @PostMapping("/import")
    public ImportReport importCity(@RequestParam(defaultValue = "Lyon") String city) {
        return importService.importCity(city);
    }
}
