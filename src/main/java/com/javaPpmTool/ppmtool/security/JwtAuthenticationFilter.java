package com.javaPpmTool.ppmtool.security;

import com.javaPpmTool.ppmtool.domain.User;
import com.javaPpmTool.ppmtool.services.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

import static com.javaPpmTool.ppmtool.security.SecurityConstants.HEADER_STRING;
import static com.javaPpmTool.ppmtool.security.SecurityConstants.TOKEN_PREFIX;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Use to validate token & extract the userId from the token
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            // Extract token from request
            String jwt = getJWTFromRequest(httpServletRequest);

            // jwt cannot be null and need to be valid
            if(StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)){
                Long userId = jwtTokenProvider.getUserIdFromJWT(jwt);
                User userDetails = customUserDetailsService.loadUserById(userId);

                // set up authentication
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // not going to pass credentials cause not doing anything with passwords now, we're using token
                        Collections.emptyList() // param takes a list of roles, this project doesn't have roles
                );

                // Add contextual information about the request (like IP address, session ID) to the authenticationToken
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                // make sure the token is valid
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }catch (Exception ex){
            logger.error("Could not set user authentication in security context", ex);
        }

        // It passes the request and response to the next filter in the filter chain.
        // It ensures that after JWT validation,
        // other filters (like authentication or authorization filters) and the main servlet can still run.
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }



    private String getJWTFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader(HEADER_STRING);

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)){
            // Return the token without the bearer string
            return bearerToken.substring(7, bearerToken.length());
        }

        return null;
    }






}
