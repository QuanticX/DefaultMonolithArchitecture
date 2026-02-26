package pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.dto.UserDto;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.entity.UserEntity;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.mapper.base.SuperMapper;

/**
 * Mapper interface for converting between UserDto and UserEntity.
 * Uses MapStruct for automatic implementation generation.
 * <p>
 * This interface extends the generic SuperMapper to inherit basic mapping methods.
 */
@Mapper(componentModel = "spring")
public interface UserMapper extends SuperMapper<UserDto, UserEntity> {

}