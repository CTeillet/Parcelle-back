package com.teillet.parcelle.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
	private final AuthenticationErrorHandler authenticationErrorHandler;


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /*
        This is where we configure the security required for our endpoints and setup our app to serve as
        an OAuth2 Resource Server, using JWT validation.
        */
		return http
				.addFilterBefore(new RequestLoggingFilter(), UsernamePasswordAuthenticationFilter.class)
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers("/api/public").permitAll()
						.requestMatchers("/api/private/**").authenticated()
				)
				.cors(withDefaults())
				.oauth2ResourceServer(oauth2 -> oauth2
						.jwt(jwt -> jwt.jwtAuthenticationConverter(makePermissionsConverter()))
						.authenticationEntryPoint(authenticationErrorHandler)
				)
				.build();
	}

	private JwtAuthenticationConverter makePermissionsConverter() {
		final var jwtAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		jwtAuthoritiesConverter.setAuthoritiesClaimName("permissions");
		jwtAuthoritiesConverter.setAuthorityPrefix("");

		final var jwtAuthConverter = new JwtAuthenticationConverter();
		jwtAuthConverter.setJwtGrantedAuthoritiesConverter(jwtAuthoritiesConverter);

		return jwtAuthConverter;
	}

	@Slf4j
	public static class RequestLoggingFilter extends OncePerRequestFilter {
		@Override
		protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, FilterChain filterChain)
				throws ServletException, IOException {
			// Affiche la requête entrante
			log.info("Requête reçue: {} - {}", request.getRemoteAddr(), request.getRequestURI());

			// Continue le traitement de la requête
			filterChain.doFilter(request, response);
		}

	}
}
