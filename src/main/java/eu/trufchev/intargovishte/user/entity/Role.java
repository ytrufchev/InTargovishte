package eu.trufchev.intargovishte.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    public static final String USER = "ROLE_USER";
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String SUPERADMIN = "ROLE_SUPERADMIN";

    public static Set<GrantedAuthority> getUserAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(USER));
    }

    public static Set<GrantedAuthority> getAdminAuthorities() {
        return Set.of(new SimpleGrantedAuthority(USER), new SimpleGrantedAuthority(ADMIN));
    }

    public static Set<GrantedAuthority> getSuperAdminAuthorities() {
        return Set.of(new SimpleGrantedAuthority(USER), new SimpleGrantedAuthority(ADMIN), new SimpleGrantedAuthority(SUPERADMIN));
    }
}
