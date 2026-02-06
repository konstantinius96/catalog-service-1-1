package ru.samsebemehanik.catalog.service;
 
import org.springframework.http.HttpStatus;
 import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.samsebemehanik.catalog.dto.AutoComponentDto;
import ru.samsebemehanik.catalog.mapper.AutoComponentMapper;
import ru.samsebemehanik.catalog.repository.AutoComponentRepository;
 
 import java.util.List;
 
 @Service
 public class AutoComponentServiceImpl implements AutoComponentService {
 
    private final AutoComponentRepository autoComponentRepository;

    public AutoComponentServiceImpl(AutoComponentRepository autoComponentRepository) {
        this.autoComponentRepository = autoComponentRepository;
    }

     @Override

    public List<AutoComponentDto> getAll() {
        return autoComponentRepository.findAll().stream()
                .map(AutoComponentMapper::toDto)
                .toList();
     }
 
     @Override

    public AutoComponentDto getById(Long id) {
        return autoComponentRepository.findById(id)
                .map(AutoComponentMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Компонент не найден"));
     }
 }
