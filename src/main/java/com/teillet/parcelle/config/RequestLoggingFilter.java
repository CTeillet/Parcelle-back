package com.teillet.parcelle.config;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class RequestLoggingFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// Affiche la requête entrante
		log.info("Requête reçue: {} - {}",request.getRemoteAddr(), ((HttpServletRequest) request).getRequestURI());

		// Continue le traitement de la requête
		chain.doFilter(request, response);
	}

}
