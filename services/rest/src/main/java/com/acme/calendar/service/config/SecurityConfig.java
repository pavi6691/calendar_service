package com.acme.calendar.service.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(t -> t.disable());
        final String[] AUTH_WHITELIST = {
                "/api/v1/auth/**",
                "/v3/api-docs/**",
                "/v3/api-docs.yaml",
                "/swagger-ui/**",
                "/swagger-ui.html"
        };
        http.authorizeRequests(authorizeRequests ->
            authorizeRequests
                    .requestMatchers(AUTH_WHITELIST).permitAll()
                .requestMatchers("/api/v1/auth/login").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.POST,"/api/v1/auth/createUser").permitAll()
                .anyRequest().authenticated() // require authentication for all other endpoints
        ).oauth2ResourceServer(oauth2 ->
            oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )

        );
        http.sessionManagement(
            t -> t.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        return http.build();
    }
    @Bean
    public JwtDecoder jwtDecoder() {
        // Use NimbusJwtDecoder for example, you should provide proper configuration for your Keycloak server
        return JwtDecoders.fromIssuerLocation("http://88.99.248.68:8080/realms/master");
    }
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtGrantedAuthoritiesConverter());
        return converter;
    }

    private static class KeycloakJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            Map<String, Object> claims = jwt.getClaims();
            if (claims.containsKey("realm_access")) {
                Map< String, Object> realmAccess = jwt.getClaim("realm_access");
                ObjectMapper mapper = new ObjectMapper();
                List<String> keycloakRoles = mapper.convertValue(realmAccess.get("roles"), new TypeReference<List<String>>() {});
                List< GrantedAuthority> roles = new ArrayList<>();
                for(String keycloakRole: keycloakRoles){
                    roles.add(new SimpleGrantedAuthority("ROLE_"+keycloakRole));
                }
                return roles;
            }
            return Collections.emptyList();
        }
    }
}
