package kz.assan.movieapp.config;


import kz.assan.movieapp.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final PersonDetailsService personDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService, PasswordEncoder passwordEncoder) {
        this.personDetailsService = personDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests((authorizer) -> authorizer
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/auth/login", "/auth/registration", "/error").permitAll()
                        .anyRequest().hasAnyRole("ADMIN", "USER")
                )
                .formLogin(form -> form
                    .loginPage("/auth/login")
                    .loginProcessingUrl("/process_login")
                    .defaultSuccessUrl("/", true)
                )
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/auth/login")
                    .permitAll();
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(personDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }
}