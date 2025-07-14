package com.finance.wallet_mysql_auth_valid.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests()
                .requestMatchers("/api/wallets/**").authenticated()
                .anyRequest().permitAll()
            .and()
            .httpBasic();
        return http.build();
    }

    // @Bean
    // public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    //     InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    //     manager.createUser(User.withUsername("user")
    //         .password(passwordEncoder.encode("password"))
    //         .roles("USER")
    //         .build());
    //     return manager;
    // }

    // @Bean
    // public PasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder();
    // }
}
