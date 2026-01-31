package com.orderprocessing.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableMethodSecurity
public class SpringSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails adminUser = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();
        
        UserDetails user1 = User.builder()
                .username("user1")
                .password(passwordEncoder().encode("user123"))
                .roles("USER")
                .build();
        
        UserDetails cust1001 = User.builder()
                .username("CUST1001")
                .password(passwordEncoder().encode("cust123"))
                .roles("USER")
                .build();
        
        UserDetails cust1002 = User.builder()
                .username("CUST1002")
                .password(passwordEncoder().encode("cust123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(adminUser, user1, cust1001, cust1002);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}