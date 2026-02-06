package ru.samsebemehanik.catalog.service;
 
import ru.samsebemehanik.catalog.dto.AutoComponentDto;

 import java.util.List;
import java.util.Map;
 
 public interface AutoComponentService {
 
    List<Map<String, Object>> getAll();
    List<AutoComponentDto> getAll();
 
    Map<String, Object> getById(Long id);
    AutoComponentDto getById(Long id);
 }
