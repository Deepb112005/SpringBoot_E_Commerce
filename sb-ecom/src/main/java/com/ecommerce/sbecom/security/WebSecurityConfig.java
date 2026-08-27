package com.ecommerce.sbecom.security;

import com.ecommerce.sbecom.security.jwt.AuthEntryPointJwt;
import com.ecommerce.sbecom.security.jwt.AuthTokenFilter;
import com.ecommerce.sbecom.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private AuthEntryPointJwt authEntryPointJwt;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) {

        http
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .csrf(csrf -> csrf.disable()
                )
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/api/auth/**")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/v3/api-docs/**")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/swagger-ui/**")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/api/public/**")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/api/admin/**")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/api/test/**")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/**")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/signin")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/h2-console/**")).permitAll()
                        //.requestMatchers("/api/admin/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(exception -> {
                    exception.authenticationEntryPoint(authEntryPointJwt);
                });

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"));

    }
}
