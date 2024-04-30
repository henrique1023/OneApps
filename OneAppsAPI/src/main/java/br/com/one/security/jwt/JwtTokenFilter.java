package br.com.one.security.jwt;

import br.com.one.exceptions.ResponseEntityExceptionHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
public class JwtTokenFilter extends GenericFilterBean {

    @Autowired
    private JwtTokenProvider tokenProvider;

    public JwtTokenFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // recupera o token
        String token = tokenProvider.resolveToken((HttpServletRequest) request);

        //valida o token
        if(token != null && tokenProvider.validateToken(token)){
            //depois cria uma authentication
            Authentication auth = null;
            try {
                auth = tokenProvider.getAuthentication(token);
            } catch (Exception e) {
                throw new ResponseEntityExceptionHandler("Token invalido!");
            }
            if(auth != null){
                //depois seta a authenticação no spring
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(request, response);
    }
}
