package ru.samsebemehanik.catalog.domain.favourite;
 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import ru.samsebemehanik.catalog.domain.component.AutoComponent;

 import java.time.LocalDateTime;
 
@Entity
@Table(name = "favourites")
 public class Favourite {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

    @Column(nullable = false)
     private Long userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "auto_component_id", nullable = false)
    private AutoComponent autoComponent;

    @Column(nullable = false)
     private LocalDateTime addedAt;

    @Version
    private Long version;

    protected Favourite() {
    }

    public Favourite(Long userId, AutoComponent autoComponent, LocalDateTime addedAt) {
        this.userId = userId;
        this.autoComponent = autoComponent;
        this.addedAt = addedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public AutoComponent getAutoComponent() {
        return autoComponent;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public Long getVersion() {
        return version;
    }
 }
