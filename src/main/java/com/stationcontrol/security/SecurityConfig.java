package com.stationcontrol.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
		
	@Bean
	@SuppressWarnings("removal")
    AuthenticationManager authenticationManager(HttpSecurity http, UserDetailsService userDetailsService, PasswordEncoder encoder) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
      .userDetailsService(userDetailsService)
      .passwordEncoder(encoder)
      .and()
      .build();
	}
	
	@Bean
    SecurityFilterChain filterChain(HttpSecurity http, AuthenticationFilter authenticationFilter, AuthorizationFilter authorizationFilter) throws Exception {
		http.cors(AbstractHttpConfigurer::disable).csrf(AbstractHttpConfigurer::disable)
		.authorizeHttpRequests(authorize -> authorize
			.requestMatchers("/error").permitAll()
			.requestMatchers(HttpMethod.POST, "/login", "/authentication/**").permitAll()
			.anyRequest().authenticated()
		)
		.addFilter(authenticationFilter)
		.addFilter(authorizationFilter)
		.sessionManagement(sessionManagementConfig -> sessionManagementConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
	
	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().requestMatchers("/resources/**");
	}
	
	@Bean
	CorsConfigurationSource corsConfiguration() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedOrigin("*");
		corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE"));
		source.registerCorsConfiguration("/**", corsConfiguration);
		return source;
	}
	
	
	@Bean
	PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
