package org.acme.demo.controller;

import lombok.extern.flogger.Flogger;
import org.acme.demo.dto.IconDTO;
import org.acme.demo.service.IconService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/icons")
public class IconController {

    private static final Logger logger = LoggerFactory.getLogger(IconController.class);
    private final IconService iconService;

    @Autowired
    public IconController(IconService iconService){
        this.iconService = iconService;
    }

    @GetMapping("/predefined")
    public ResponseEntity<List<IconDTO>>getPredefinedIcons(){
        List<IconDTO> icons = iconService.getAllIcons()
                .stream()
                .map(icon -> {
                    IconDTO dto = new IconDTO();
                    dto.setId(icon.getId());
                    dto.setName(icon.getIconName());
                    return dto;
                })
                .collect(Collectors.toList());

        logger.info("Fetched icons: {}", icons);  // Log the list of icons
        return ResponseEntity.ok(icons);
    }
}
