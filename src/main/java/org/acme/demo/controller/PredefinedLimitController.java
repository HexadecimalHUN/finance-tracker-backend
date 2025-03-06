package org.acme.demo.controller;

import org.acme.demo.dto.PredefinedLimitDTO;
import org.acme.demo.service.PredefinedLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/predefined-limits")
public class PredefinedLimitController {

    private final PredefinedLimitService predefinedLimitService;

    @Autowired
    public PredefinedLimitController(PredefinedLimitService predefinedLimitService) {
        this.predefinedLimitService = predefinedLimitService;
    }

    @GetMapping
    public List<PredefinedLimitDTO> getAllPredefinedLimits() {
        return predefinedLimitService.getAllPredefinedLimits();
    }
}
