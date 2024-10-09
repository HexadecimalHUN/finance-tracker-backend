package org.acme.demo.controller;

import org.acme.demo.dto.IconDTO;
import org.acme.demo.service.IconService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/icons")
public class IconController {

    @Autowired
    private IconService iconService;

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
        return ResponseEntity.ok(icons);
    }
}
