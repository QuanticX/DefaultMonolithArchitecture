package pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.dto.base;

/**
 * Base interface for all Data Transfer Objects (DTOs) in the GameUp application.
 * Provides a method to retrieve the unique identifier of the DTO.
 */
public interface SuperDto {
    /**
     * Returns the unique identifier of the DTO.
     *
     * @return the identifier as a {@link Long}
     */
    Long getIdentifier();
}