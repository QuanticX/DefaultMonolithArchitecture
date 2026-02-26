/**
 * Package containing base entities for the GameUp application.
 */
package pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.entity.base;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Base entity class that provides common properties and methods for all entities.
 * It includes an auto-generated identifier, timestamps for creation and updates,
 * and abstract methods for getting the identifier.
 */
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class SuperEntity {

    /**
     * Unique identifier for the entity.
     * This field is auto-generated and serves as the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    /**
     * Timestamp indicating when the entity was created.
     * This field is automatically set when the entity is persisted.
     */
    private LocalDateTime createdAt;
    /**
     * Timestamp indicating when the entity was last updated.
     * This field is automatically set when the entity is updated.
     */
    private LocalDateTime updatedAt;

    /**
     * Abstract method to get the unique identifier of the entity.
     * This method should be implemented by subclasses to return their specific identifier.
     *
     * @return the unique identifier of the entity
     */

    @Transient
    public abstract Long getIdentifier();


    /**
     * Initializes the entity with the current timestamp when it is persisted.
     */
    @PrePersist
    private void init() {
        this.createdAt = LocalDateTime.now();
        update();
    }

    /**
     * Updates the last updated timestamp of the entity when it is modified.
     */
    @PreUpdate
    private void update() {
        this.updatedAt = LocalDateTime.now();
    }


}