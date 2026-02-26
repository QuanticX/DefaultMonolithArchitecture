package pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.controller.rest.base;


import org.springframework.web.bind.annotation.*;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.business.base.SuperService;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.dto.base.SuperDto;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.mapper.base.SuperMapper;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.repository.base.SuperRepository;

import java.util.List;

/**
 * This class is an abstract base class for REST controllers
 * that handle common CRUD operations for entities.
 * It provides default implementations for saving, retrieving, updating,
 * and deleting entities using a corresponding {@link SuperService}.
 *
 * <p>This class is designed to be extended by concrete REST controllers,
 * which should specify the DTO type and the service type.
 *
 * @param <DTO> The Data Transfer Object type, extending {@link SuperDto}.
 *              This represents the data structure used for transferring
 *              data between the client and the server.
 * @param <S>   The Service type, extending {@link SuperService}.
 *              This provides CRUD operations for entities.
 *              It provides default implementations for saving, retrieving, updating,
 *              and deleting entities using a corresponding {@link SuperService}.
 *
 *              <p>This class is designed to be extended by concrete REST controllers,
 *              which should specify the DTO type and the service type.
 */
public abstract class SuperRestController<DTO extends SuperDto, S extends SuperService<?, DTO, ? extends SuperRepository<?>, ? extends SuperMapper<DTO, ?>>> {

    /**
     * The service used to perform CRUD operations on the entity.
     */
    protected S service;

    /**
     * Constructor for the SuperRestController.
     *
     * @param service The service instance to be used for CRUD operations.
     */
    protected SuperRestController(S service) {
        this.service = service;
    }

    /**
     * Saves a new entity.
     *
     * @param dto The DTO representing the entity to save.
     * @return The saved DTO.
     * @throws RuntimeException if the ID is not null.
     */
    @PostMapping
    public DTO save(@RequestBody DTO dto) {
        if (dto.getIdentifier() != null) {
            throw new RuntimeException("ID must be null");
        }
        return service.saveOrUpdate(dto);
    }

    /**
     * Retrieves an entity by its ID.
     *
     * @param id The ID of the entity to retrieve.
     * @return The DTO representing the found entity.
     */
    @GetMapping
    public DTO findById(@RequestParam Long id) {
        return service.findById(id);
    }

    /**
     * Retrieves all entities.
     *
     * @return A list of DTOs representing all entities.
     */
    @GetMapping("/all")
    public List<DTO> findAll() {
        return service.findAll();
    }

    /**
     * Updates an entity using the HTTP PUT method.
     *
     * @param dto The DTO representing the entity to update.
     * @return The updated DTO.
     */
    @PutMapping
    public DTO updateByPut(@RequestBody DTO dto) {
        return saveAndReturnModifiedDTO(dto);
    }

    /**
     * Updates an entity using the HTTP PATCH method.
     *
     * @param dto The DTO representing the entity to update.
     * @return The updated DTO.
     */
    @PatchMapping
    public DTO updateByPatch(@RequestBody DTO dto) {
        return saveAndReturnModifiedDTO(dto);
    }

    /**
     * Saves and returns the modified DTO.
     *
     * @param dto The DTO to save.
     * @return The saved DTO.
     * @throws RuntimeException if the ID is null.
     */
    private DTO saveAndReturnModifiedDTO(DTO dto) {
        if (dto.getIdentifier() != null) {
            return service.saveOrUpdate(dto);
        } else {
            throw new RuntimeException("ID must not be null");
        }
    }

    /**
     * Deletes an entity by its ID.
     *
     * @param id The ID of the entity to delete.
     * @return Always returns null.
     */
    @DeleteMapping
    public DTO deleteByDTO(@RequestParam Long id) {
        service.deleteById(id);
        return null;
    }

}