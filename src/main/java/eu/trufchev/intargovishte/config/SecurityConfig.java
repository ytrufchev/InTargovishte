package eu.trufchev.intargovishte.config;

import eu.trufchev.intargovishte.WebConfig;
import eu.trufchev.intargovishte.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private UserDetailsService userDetailsService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource())).csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/swagger-ui/**").permitAll();
                    authorize.requestMatchers("/v3/api-docs/**").permitAll();
                    authorize.requestMatchers("/swagger-resources/**").permitAll();
                    authorize.requestMatchers("/webjars/**").permitAll();
                    authorize.requestMatchers("/auth/**").permitAll();
                    authorize.requestMatchers("/stats/**").permitAll();
                    authorize.requestMatchers("/guest/**").permitAll();
                    authorize.requestMatchers("/content/inapp/**").authenticated();
                    authorize.requestMatchers("/information/news/update").permitAll();
                    authorize.requestMatchers("/content/**").authenticated();
                    authorize.requestMatchers("/information/**").authenticated();
                    authorize.requestMatchers("/user/**").authenticated();
                    authorize.requestMatchers("/superadmin/**").hasAnyAuthority("ROLE_SUPERADMIN");
                    authorize.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));  // Allow all origins, or specify allowed origins
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}