 package ru.samsebemehanik.catalog.dto;
 
 public class AutoComponentDto {
 
     private Long id;
     private String name;
     private String description;
    private Long version;

    public AutoComponentDto(Long id, String name, String description, Long version) {
         this.id = id;
         this.name = name;
         this.description = description;
        this.version = version;
     }
 
     public Long getId() {
         return id;
     }
 
     public String getName() {
         return name;
     }
 
     public String getDescription() {
         return description;
     }

    public Long getVersion() {
        return version;
    }
 }
