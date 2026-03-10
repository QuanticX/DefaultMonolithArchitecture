package pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.entity.base.SuperEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends SuperEntity implements UserDetails {
    @Override
    public Long getIdentifier() {
        return getId();
    }

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @Size(min = 3, max = 255)
    @NotBlank
    @Column(unique = true)
    private String name;

    @NotBlank
    private String hashedPassword;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;


    private String firstName;
    private String lastName;

    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    public UserEntity(Long id, String name, String hashedPassword, List<String> roles) {
        this.setId(id);
        this.roles = roles;
        this.name = name;
        this.hashedPassword = hashedPassword;
    }

    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    @Transient
    public @Nullable String getPassword() {
        return hashedPassword;
    }

    @Override
    @Transient
    public String getUsername() {
        return name;
    }

}
