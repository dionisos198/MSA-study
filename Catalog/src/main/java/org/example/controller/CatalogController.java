package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.dto.ResponseCatalog;
import org.example.entity.CatalogEntity;
import org.example.service.CatalogService;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class CatalogController {

    private final Environment env;
    private final CatalogService catalogService;

    @GetMapping("/health_check")
    public String status(HttpServletRequest request){
        return String.format("It's working in catalog Service on Port %s",request.getServerPort());
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> getCatalogs(){

        return ResponseEntity.ok().body(catalogService.findAll().stream().map(catalogEntity -> new ModelMapper().map(catalogEntity,
                ResponseCatalog.class)).collect(Collectors.toList()));


    }



}
