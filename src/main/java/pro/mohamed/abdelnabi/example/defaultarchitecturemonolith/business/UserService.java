package pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.business;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.business.base.SuperService;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.security.SecurityConfig;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.dto.UserDto;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.entity.UserEntity;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.mapper.UserMapper;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service providing business logic and CRUD operations for users.
 * Extends the generic SuperService for UserEntity and UserDto.
 * Implements UserDetailsService for Spring Security integration.
 */
@Slf4j
@Service
@Transactional
public class UserService extends SuperService<UserEntity, UserDto, UserRepository, UserMapper> implements UserDetailsService {

    /**
     * The password encoder used for hashing user passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a UserService with the specified repository, mapper, and password encoder.
     *
     * @param repository      the user repository to use
     * @param mapper          the user mapper to use
     * @param passwordEncoder the password encoder to use
     */
    @Autowired
    public UserService(UserRepository repository, UserMapper mapper, PasswordEncoder passwordEncoder) {
        super(repository, mapper);
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Finds a user by username.
     *
     * @param nom the username to search for
     * @return the found UserDto
     */
    public UserDto findByNom(String nom) {
        return mapper.toRecord(repository.findByName(nom));
    }

    /**
     * Verifies the login credentials of a user.
     *
     * @param user the user DTO containing login information
     * @return true if credentials are valid, false otherwise
     */
    public Boolean verifyLogin(UserDto user) {
        Optional<UserEntity> userEntity = Optional.ofNullable(repository.findByName(user.name()));
        return userEntity.isPresent() && passwordEncoder.matches(user.hashedPassword(), userEntity.get().getHashedPassword());
    }

    /**
     * Checks if a user exists by username.
     *
     * @param nom the username to check
     * @return true if the user exists, false otherwise
     */
    public Boolean existByNom(String nom) {
        return repository.existsByName(nom);
    }

    /**
     * Saves or updates a user, encoding the password and checking for unique username.
     *
     * @param dto the user DTO to save or update
     * @return the saved or updated UserDto
     * @throws RuntimeException if the username is not unique or is blank
     */
    @Override
    public UserDto saveOrUpdate(UserDto dto) {
        UserDto newDtoWithMotDePasseHashed = new UserDto(dto.id(), dto.name(), passwordEncoder.encode(dto.hashedPassword()), dto.roles().stream().map(role -> "ROLE_" + role).toList());
        if (dto.id() != null) {
            UserEntity user = repository.findByName(dto.name());
            if (user != null && !user.getId().equals(dto.id())) {
                throw new RuntimeException("User with this name already exists");
            }
        }
        if (dto.name() == null || dto.name().isBlank()) {
            throw new RuntimeException("User name cannot be null or blank");
        }
        return super.saveOrUpdate(newDtoWithMotDePasseHashed);
    }

    /**
     * Loads a user by username for Spring Security authentication.
     *
     * @param username the username to search for
     * @return the UserDetails of the found user
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = repository.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    /**
     * Initializes the default admin user at application startup.
     * Throws a RuntimeException if the admin user cannot be saved.
     */
    @PostConstruct
    public void init() {
        UserDto defaultAdminUser = new UserDto(null, "Admin", "Admin", List.of(SecurityConfig.ADMIN, SecurityConfig.USER));
        UserDto adminUserNotSaved = Optional.ofNullable(saveOrUpdate(defaultAdminUser)).orElseThrow(() -> new RuntimeException("Admin user not saved"));
        log.debug(adminUserNotSaved.name() + " created.");
    }
}