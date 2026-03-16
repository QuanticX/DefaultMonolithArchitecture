package pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.entity;

import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.entity.base.SuperEntity;

public interface SuperEntityTest<E extends SuperEntity> {

    E generateEntity() ;

    E cloneAndModifyEntity(E entity);
}
