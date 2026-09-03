package com.aeropelican.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private static final String SECRET_KEY = "dfcd39e6f6b95128c5c344ff0b5a3e6f51f11f29af552261bd655de4136d5574";

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKey key = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(SECRET_KEY)
        );

        return NimbusJwtDecoder.withSecretKey(key)
                .macAlgorithm(org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS384)
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**", "/api/v1/users/auth/email/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        // Default converter maps "scope" claim to SCOPE_ prefixed authorities.
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles"); // default is "scope", change to match your token

        converter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        // If roles are nested (e.g., Keycloak: "realm_access" -> "roles"), use a custom converter:
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = grantedAuthoritiesConverter.convert(jwt).stream()
                    .map(authority -> {
                        String role = authority.getAuthority();
                        return new SimpleGrantedAuthority(
                                role.startsWith("ROLE_") ? role : "ROLE_" + role
                        );
                    })
                    .collect(Collectors.toSet());
            // add roles from realm_access.roles (Keycloak) if present
            Object realmAccess = jwt.getClaim("realm_access");
            if (realmAccess instanceof Map) {
                Object roles = ((Map<?,?>)realmAccess).get("roles");
                if (roles instanceof Collection) {
                    authorities = Stream.concat(authorities.stream(),
                            ((Collection<?>)roles).stream()
                                    .map(Object::toString)
                                    .map(r -> new SimpleGrantedAuthority(
                                            r.startsWith("ROLE_") ? r : "ROLE_" + r
                                    ))
                    ).collect(Collectors.toSet());
                }
            }
            return authorities;
        });

        return converter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
