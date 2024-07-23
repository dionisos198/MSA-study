package org.example.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.CatalogEntity;
import org.example.repository.CatalogRepository;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final CatalogRepository catalogRepository;
    private final Environment env;

    @Override
    public List<CatalogEntity> findAll() {
        return catalogRepository.findAll();
    }
}
