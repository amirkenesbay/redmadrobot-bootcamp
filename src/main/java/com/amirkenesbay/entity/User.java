package com.amirkenesbay.entity;

import com.amirkenesbay.listeners.UserListener;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.persistence.*;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@EntityListeners(UserListener.class)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private boolean isActive;

    @OneToMany(mappedBy = "adOwner", cascade = CascadeType.ALL, targetEntity = Advertisement.class)
    private Collection<Advertisement> advertisements;

    @OneToMany(mappedBy = "messageSender", cascade = CascadeType.ALL, targetEntity = Message.class)
    private Collection<Message> sentMessages;

    @OneToMany(mappedBy = "messageReceiver", cascade = CascadeType.ALL, targetEntity = Message.class)
    private Collection<Message> receivedMessages;

    @OneToMany(mappedBy = "imageOwner", cascade = CascadeType.ALL, targetEntity = Image.class)
    private Collection<Image> uploadedImages;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> roles;

    @DateTimeFormat
    private Date accountCreatedAt = Date.from(Instant.now());

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
