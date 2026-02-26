package pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.dto;


import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.dto.base.SuperDto;

import java.util.List;


/**
 * Data Transfer Object representing a user.
 * Contains the user's ID, name, hashed password, and assigned roles.
 */
public record UserDto(Long id, String name, String hashedPassword, List<String> roles) implements SuperDto {

    /**
     * Constructs a new UserDto with the specified details.
     *
     * @param id             the unique identifier of the user
     * @param name       the name of the user
     * @param hashedPassword the hashed password of the user
     * @param roles          the list of roles assigned to the user
     */
    public UserDto {
    }

    /**
     * Returns the unique identifier of the user.
     *
     * @return the ID of the user
     */
    @Override
    public Long getIdentifier() {
        return id;
    }
}
