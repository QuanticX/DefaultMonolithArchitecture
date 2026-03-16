package pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.business.base.SuperService;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.dto.SuperDtoTest;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.dto.UserDto;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.dto.base.SuperDto;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.entity.SuperEntityTest;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.entity.UserEntity;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.entity.base.SuperEntity;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.mapper.base.SuperMapper;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.repository.base.SuperRepository;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public abstract class SuperServiceTest<E extends SuperEntity, D extends SuperDto,R extends SuperRepository<E>,M extends SuperMapper<D,E>,S extends SuperService<E,D,R,M>> implements SuperEntityTest<E>, SuperDtoTest<D> {

    @InjectMocks
    protected S service;
    @Mock
    protected M mapper;
    @Mock
    protected R repository;
    protected E entity;
    protected D dto;
    protected E modifiedEntity;
    protected D modifiedDto;


    @BeforeEach
    void setUp() {
        entity = generateEntity();
        dto = generateDto();
        modifiedEntity = cloneAndModifyEntity(entity);
        modifiedDto = cloneAndModifyDto(dto);
    }

    @Test
    void injectedComponentIsNotNull() {
        assertThat(repository).isNotNull();
        assertThat(service).isNotNull();
        assertThat(mapper).isNotNull();
    }

    @Test
    void createReadUpdateDeleteBusinessLogic() {
        E entity = generateEntity();
        when(mapper.toRecord(entity)).thenReturn(dto);
        D dto = mapper.toRecord(entity);

        when(repository.save(entity)).thenReturn(entity);
        when(mapper.fromRecord(dto)).thenReturn(entity);
        dto = service.saveOrUpdate(dto);
        entity = mapper.fromRecord(dto);
        assertThat(entity).isEqualTo(service.findById(entity.getId()));

        when(repository.findAll()).thenReturn(List.of(entity));
        when(repository.findAllById(List.of(entity.getId()))).thenReturn(List.of(entity));
        D finalDto = dto;
        assertThat(service.findAll()).matches(list -> list.contains(finalDto));
        assertThat(service.findAllById(List.of(entity.getId()))).matches(list -> list.contains(finalDto));

        when(mapper.toRecord(modifiedEntity)).thenReturn(modifiedDto);
        modifiedDto = mapper.toRecord(modifiedEntity);

        when(repository.save(modifiedEntity)).thenReturn(modifiedEntity);
        when(mapper.fromRecord(modifiedDto)).thenReturn(modifiedEntity);
        modifiedDto = service.saveOrUpdate(modifiedDto);
        modifiedEntity = mapper.fromRecord(modifiedDto);
        assertThat(modifiedDto).isNotEqualTo(dto);

        when(repository.findAllById(List.of(modifiedEntity.getId()))).thenReturn(Collections.emptyList());
        service.deleteById(modifiedEntity.getId());
        assertThat(service.findAllById(List.of(modifiedEntity.getId()))).matches(List::isEmpty);
    }
}
