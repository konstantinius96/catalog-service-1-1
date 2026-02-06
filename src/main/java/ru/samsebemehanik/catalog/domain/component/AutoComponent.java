 package ru.samsebemehanik.catalog.domain.component;
 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "auto_components")
 public class AutoComponent {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

    @Column(nullable = false, length = 200)
     private String name;

    @Column(nullable = false, length = 2000)
     private String description;

  @Version
    private Long version;

    protected AutoComponent() {
    }

    public AutoComponent(String name, String description) {
         this.name = name;
         this.description = description;
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
