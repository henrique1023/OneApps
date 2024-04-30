package br.com.one.services;

import br.com.one.data.vo.v1.security.AccountCredentialsVO;
import br.com.one.data.vo.v1.security.TokenVO;
import br.com.one.exceptions.ResponseEntityExceptionHandler;
import br.com.one.repositories.UserRepository;
import br.com.one.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServices {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository repository;

    @SuppressWarnings("rawtypes")
    public ResponseEntity  signin(AccountCredentialsVO data)  throws Exception {
        try {
            var username = data.getUsername();
            var password = data.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            var user = repository.findByUsername(username);

            var tokenResponse = new TokenVO();
            if(user != null){
                tokenResponse = tokenProvider.createAccessToken(username, user.getRoles());
            }else{
                throw new UsernameNotFoundException("Username " + username + " not found!");
            }
            return ResponseEntity.ok(tokenResponse);
        }catch (Exception e){
            throw new ResponseEntityExceptionHandler("Invalid username/password supplied!");
        }
    }

    @SuppressWarnings("rawtypes")
    public ResponseEntity refleshToken(String username, String refreshToken){
        try {
            var user = repository.findByUsername(username);

            var tokenResponse = new TokenVO();
            if(user != null){
                tokenResponse = tokenProvider.refreshAccessToken(refreshToken);
            }else{
                throw new UsernameNotFoundException("Username " + username + " not found!");
            }
            return ResponseEntity.ok(tokenResponse);
        }catch (Exception e){
            throw new ResponseEntityExceptionHandler("Invalid username/password supplied!");
        }
    }
}
