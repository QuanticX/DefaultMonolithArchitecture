package pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.mapper.base;


import org.mapstruct.InheritInverseConfiguration;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.dto.base.SuperDto;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.entity.base.SuperEntity;

/**
 * Generic interface for mapping between DTOs and Entities.
 *
 * @param <Dto>    the type of the Data Transfer Object, extending {@link SuperDto}
 * @param <Entity> the type of the Entity, extending {@link SuperEntity}
 */
public interface SuperMapper<Dto extends SuperDto, Entity extends SuperEntity> {

    /**
     * Converts a DTO to its corresponding Entity.
     *
     * @param dto the Data Transfer Object to convert
     * @return the corresponding Entity
     */
    Entity fromRecord(Dto dto);

    /**
     * Converts an Entity to its corresponding DTO.
     *
     * @param entity the Entity to convert
     * @return the corresponding Data Transfer Object
     */
    @InheritInverseConfiguration
    Dto toRecord(Entity entity);

}