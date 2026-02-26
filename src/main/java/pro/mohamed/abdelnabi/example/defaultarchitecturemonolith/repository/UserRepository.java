package pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.repository;

import org.springframework.stereotype.Repository;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.entity.UserEntity;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.repository.base.SuperRepository;

@Repository
public interface UserRepository extends SuperRepository<UserEntity> {
    UserEntity getByName(String username);

    Boolean existsByName(String nom);

    UserEntity findByName(String nom);
}
