package com.javaPpmTool.ppmtool.security;

import com.javaPpmTool.ppmtool.services.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// we use EnableMethodSecurity to enable method level security, and also to use PreAuthorize (ProjectController)
@Configuration
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // when unauthenticated user tries to access the resource then spring security throws authentication exception
    // JwtAuthenticationEntryPoint class will catch that exception and return the error response to the client
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    private JwtAuthenticationFilter authenticationFilter;


    // add @Bean so that spring container can manage the object of DefaultSecurityFilterChain object
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll() // Allows public access to /
                        .requestMatchers("/api/users/**").permitAll() // Allows public access to /
                        .anyRequest().authenticated() // Other requests need authentication
                ).httpBasic(Customizer.withDefaults());

        // use addFilterBefore to add JWT authentication filter before spring security filters
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

//                .authorizeHttpRequests((authorize) -> {
//                    // all http POST & DELETE request starts with /api should be accessible by all users have admin role
//                    // all http PATCH request starts with /api should be accessible by all users have admin & user role
//                    // all http GET request starts with /api should be accessible by all users
////                    authorize.requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN");
////                    authorize.requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN");
////                    authorize.requestMatchers(HttpMethod.PATCH, "/api/**").hasAnyRole("ADMIN", "USER");
////                    authorize.requestMatchers(HttpMethod.GET, "/api/**").permitAll();
//
//                    // all incoming http request is authenticated by using anyRequest.authenticated method
//                    authorize.anyRequest().authenticated();
////                });
//                }).httpBasic(Customizer.withDefaults());

        return http.build();

    }

    // @Bean: Spring container maintain the object of this AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }



}
