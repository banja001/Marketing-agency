package com.example.marketingagencymanagement.model;

import com.example.marketingagencymanagement.dto.UserCreationDto;
import com.example.marketingagencymanagement.dto.UserProfileDto;
import com.example.marketingagencymanagement.listener.SensitiveDataListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static jakarta.persistence.InheritanceType.JOINED;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EntityListeners(SensitiveDataListener.class)
@Inheritance(strategy=JOINED)
@Table(name = "users")
public class User implements UserDetails, Serializable  {

    @Id
    @SequenceGenerator(name = "userSeq", sequenceName = "userSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSeq")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "phone")
    private String phone;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "activation_pending")
    private boolean activationPending;

    @Column(name = "blocked")
    private LocalDateTime blocked;

    @Column(name = "using2FA")
    private boolean using2FA;

    @Column(name = "secret2FA")
    private String secret2FA;

    @Column(name = "isBlockedUser")
    private Boolean isBlockedUser;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
    public boolean hasPermission(String permissionName) {
        for (Role role : roles) {
            for (Permission permission : role.getPermissions()) {
                if (permission.getName().equals(permissionName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public User(UserProfileDto dto) {
        this.id = dto.getId();
        this.username = dto.getUsername();
        this.password = dto.getPassword();
        this.name = dto.getName();
        this.surname = dto.getSurname();
        this.address = dto.getAddress();
        this.city = dto.getCity();
        this.country = dto.getCountry();
        this.phone = dto.getPhone();
        this.isBlockedUser = dto.getIsBlockedUser();
    }

    public User(UserCreationDto dto) {
        this.username = dto.getUsername();
        this.password = dto.getPassword();
        this.name = dto.getName();
        this.surname = dto.getSurname();
        this.address = dto.getAddress();
        this.city = dto.getCity();
        this.country = dto.getCountry();
        this.phone = dto.getPhone();
    }}
