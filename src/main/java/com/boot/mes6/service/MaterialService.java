package com.boot.mes6.service;

import com.boot.mes6.entity.MaterialInOut;
import com.boot.mes6.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialService {
    private final MaterialRepository materialRepository;

    public List<MaterialInOut> getAllMaterial() {
        return materialRepository.findAll();
    }

    public void addMaterial(MaterialInOut materialInOut) {
        materialRepository.save(materialInOut);
    }
}
