package org.acme.demo.service;

import org.acme.demo.dto.PredefinedLimitDTO;
import org.acme.demo.entity.PredefinedLimit;
import org.acme.demo.repository.PredefinedLimitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PredefinedLimitService {
    private final PredefinedLimitRepository predefinedLimitRepository;

    @Autowired
    public  PredefinedLimitService(PredefinedLimitRepository predefinedLimitRepository){
        this.predefinedLimitRepository = predefinedLimitRepository;
    }

    /**
     * Retrieves all predefined limits from the database.
     *
     * @return a list of predefined limits.
     */
    public List<PredefinedLimitDTO> getAllPredefinedLimits() {
        List<PredefinedLimit> predefinedLimits = predefinedLimitRepository.findAll();
        return predefinedLimits.stream()
                .map(limit -> new PredefinedLimitDTO(limit.getId(), limit.getAmount(), limit.getTitle(),limit.getDescription()))
                .collect(Collectors.toList());
    }

    /**
     * Finds a predefined limit by its ID.
     *
     * @param id the ID of the predefined limit.
     * @return an Optional containing the found PredefinedLimit, or empty if not found.
     */
    public Optional<PredefinedLimit>findById(Long id){
        return predefinedLimitRepository.findById(id);
    }


}
