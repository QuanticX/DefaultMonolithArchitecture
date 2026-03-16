package pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.dto;

import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.dto.base.SuperDto;

public interface SuperDtoTest<D extends SuperDto> {

    D generateDto();

    D cloneAndModifyDto(D dto);
}
