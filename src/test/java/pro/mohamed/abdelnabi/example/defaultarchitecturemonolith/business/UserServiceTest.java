package pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.dto.UserDto;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.entity.UserEntity;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.mapper.UserMapper;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.repository.UserRepository;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserEntity userEntity;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("testuser");
        userEntity.setHashedPassword("hashedPassword");

        userDto = new UserDto(1L, "testuser", "password", Collections.emptyList());
    }

    @Test
    void findByNom() {
        when(userRepository.findByName("testuser")).thenReturn(userEntity);
        when(userMapper.toRecord(userEntity)).thenReturn(userDto);

        UserDto found = userService.findByNom("testuser");

        assertThat(found).isEqualTo(userDto);
        verify(userRepository).findByName("testuser");
        verify(userMapper).toRecord(userEntity);
    }

    @Test
    void verifyLoginSuccess() {
        UserDto loginDto = new UserDto(null, "testuser", "password", null);
        when(userRepository.findByName("testuser")).thenReturn(userEntity);
        when(passwordEncoder.matches("password", "hashedPassword")).thenReturn(true);

        Boolean result = userService.verifyLogin(loginDto);

        assertThat(result).isTrue();
    }

    @Test
    void verifyLoginFailureWrongPassword() {
        UserDto loginDto = new UserDto(null, "testuser", "wrongpassword", null);
        when(userRepository.findByName("testuser")).thenReturn(userEntity);
        when(passwordEncoder.matches("wrongpassword", "hashedPassword")).thenReturn(false);

        Boolean result = userService.verifyLogin(loginDto);

        assertThat(result).isFalse();
    }

    @Test
    void existByNom() {
        when(userRepository.existsByName("testuser")).thenReturn(true);

        Boolean result = userService.existByNom("testuser");

        assertThat(result).isTrue();
        verify(userRepository).existsByName("testuser");
    }

    @Test
    void saveOrUpdateCreateUser() {
        UserDto dtoToSave = new UserDto(null, "newuser", "password", Collections.emptyList());
        UserEntity entityToSave = new UserEntity();
        UserEntity savedEntity = new UserEntity();
        UserDto savedDto = new UserDto(1L, "newuser", "encodedPassword", Collections.emptyList());

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        UserDto dtoWithEncodedPassword = new UserDto(null, "newuser", "encodedPassword", Collections.emptyList());

        when(userMapper.fromRecord(dtoWithEncodedPassword)).thenReturn(entityToSave);
        when(userRepository.save(entityToSave)).thenReturn(savedEntity);
        when(userMapper.toRecord(savedEntity)).thenReturn(savedDto);

        UserDto result = userService.saveOrUpdate(dtoToSave);

        assertThat(result).isEqualTo(savedDto);
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void saveOrUpdateThrowsExceptionWhenNameExistsForDifferentUser() {
        UserDto dtoForUpdate = new UserDto(1L, "existinguser", "password", Collections.emptyList());
        UserEntity existingUser = new UserEntity();
        existingUser.setId(2L);
        existingUser.setName("existinguser");

        when(userRepository.findByName("existinguser")).thenReturn(existingUser);

        assertThatThrownBy(() -> userService.saveOrUpdate(dtoForUpdate))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User with this name already exists");

        verify(userRepository, never()).save(any());
    }

    @Test
    void loadUserByUsernameSuccess() {
        when(userRepository.findByName("testuser")).thenReturn(userEntity);

        UserDetails userDetails = userService.loadUserByUsername("testuser");

        assertThat(userDetails).isEqualTo(userEntity);
    }

    @Test
    void loadUserByUsernameThrowsUsernameNotFoundException() {
        when(userRepository.findByName("nonexistent")).thenReturn(null);

        assertThatThrownBy(() -> userService.loadUserByUsername("nonexistent"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");
    }
}
