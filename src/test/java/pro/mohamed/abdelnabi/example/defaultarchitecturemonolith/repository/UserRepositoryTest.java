package pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.entity.UserEntity;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.entity.UserEntityTest;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryTest extends SuperRepositoryTest<UserEntity, UserRepository> implements UserEntityTest {


    @Autowired
    public UserRepositoryTest(UserRepository repository) {
        super(repository);
    }

    @Override
    public UserEntity generateEntity() {
        return generateUser();
    }



    @Override
    public UserEntity cloneAndModifyEntity(UserEntity entity) {
        return cloneAndModifyUser(entity);
    }



    @Test
    void whenSavedThenFindsByName() {
        UserEntity user = generateEntity();
        user.setName("testuser");
        repository.save(user);
        UserEntity found = repository.findByName("testuser");
        assertThat(found.getName()).isEqualTo(user.getName());
    }

    @Test
    void whenExistsByNameThenReturnsTrue() {
        UserEntity user = generateEntity();
        user.setName("testuser");
        repository.save(user);
        assertThat(repository.existsByName("testuser")).isTrue();
    }

    @Test
    void whenGetByNameThenReturnsUser() {
        UserEntity user = generateEntity();
        user.setName("testuser");
        repository.save(user);
        UserEntity found = repository.getByName("testuser");
        assertThat(found.getName()).isEqualTo(user.getName());
    }
}
