package ru.samsebemehanik.catalog.service;
 
import ru.samsebemehanik.catalog.dto.AutoComponentDto;

 import java.util.List;
 
 public interface AutoComponentService {
 
    List<AutoComponentDto> getAll();
 
    AutoComponentDto getById(Long id);
 }
