package com.javaPpmTool.ppmtool.security;

import com.javaPpmTool.ppmtool.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static com.javaPpmTool.ppmtool.security.SecurityConstants.H2_URL;
import static com.javaPpmTool.ppmtool.security.SecurityConstants.SIGN_UP_URLS;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);

        return daoAuthenticationProvider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                // Disable CSRF for simplicity
                .csrf(csrf -> csrf.disable())
                // this is a rest API, want to keep server stateless, don't want to save sessions or cookies, the server doesn't have to hold a session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // To enable H2 Database
                .headers(headers -> headers.frameOptions().sameOrigin())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/favicon.ico"
                        ).permitAll()
                        .requestMatchers(SIGN_UP_URLS).permitAll()
                        .requestMatchers(H2_URL).permitAll()
                        .anyRequest().authenticated() //anything other than that we need to have authentication
                ).httpBasic(Customizer.withDefaults());

        return http.build();
    };

}
