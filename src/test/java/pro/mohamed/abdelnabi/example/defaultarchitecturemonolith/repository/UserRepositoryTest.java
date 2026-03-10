package pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.entity.UserEntity;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.repository.base.SuperRepositoryTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryTest extends SuperRepositoryTest<UserEntity, UserRepository> {


    @Autowired
    public UserRepositoryTest(UserRepository repository) {
        super(repository);
    }

    @Override
    protected UserEntity generateEntity() {
        UserEntity user = new UserEntity();
        user.setName("testuser");
        user.setHashedPassword("testpassword");
        user.setRoles(List.of(new String[]{"ROLE_USER"}));
        user.setEmail("test@test.com");

        return user;
    }

    @Override
    protected UserEntity cloneAndModifyEntity(UserEntity entity) {
        UserEntity user = new UserEntity();
        user.setId(entity.getId());
        user.setName("testuser2");
        user.setHashedPassword("testpassword2");
        user.setEmail("test2@test.com");

        return user;
    }

    @Test
    void whenSaved_thenFindsByName() {
        UserEntity user = generateEntity();
        user.setName("testuser");
        repository.save(user);
        UserEntity found = repository.findByName("testuser");
        assertThat(found.getName()).isEqualTo(user.getName());
    }

    @Test
    void whenExistsByName_thenReturnsTrue() {
        UserEntity user = generateEntity();
        user.setName("testuser");
        repository.save(user);
        assertThat(repository.existsByName("testuser")).isTrue();
    }

    @Test
    void whenGetByName_thenReturnsUser() {
        UserEntity user = generateEntity();
        user.setName("testuser");
        repository.save(user);
        UserEntity found = repository.getByName("testuser");
        assertThat(found.getName()).isEqualTo(user.getName());
    }
}
