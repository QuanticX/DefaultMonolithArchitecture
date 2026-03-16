package pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.entity;

import java.util.List;

public interface UserEntityTest {

    default UserEntity cloneAndModifyUser(UserEntity entity) {
        UserEntity user = new UserEntity();
        user.setId(entity.getId());
        user.setName("testuser2");
        user.setHashedPassword("testpassword2");
        user.setRoles(List.of(new String[]{"ROLE_ADMIN"}));
        user.setEmail("test2@test.com");

        return user;
    }

    default UserEntity generateUser() {
        UserEntity user = new UserEntity();
        user.setName("testuser");
        user.setHashedPassword("testpassword");
        user.setRoles(List.of(new String[]{"ROLE_USER"}));
        user.setEmail("test@test.com");

        return user;
    }
}
