package com.tpe.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTUtils jwtUtils;


    //token filtreleyecegiz. requestten token i almamiz lazim

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token=parseJwt(request);

        try {
            if(token!=null && jwtUtils.validateToken(token)) {
                String username=jwtUtils.getUsernameFromJwtToken(token);//login olan userin usernamei ni aliyor
                UserDetails userDetails =userDetailsService.loadUserByUsername(username);//login olan user

                //login olan useri security contexte koymak icin authentication objesi olustur;
                UsernamePasswordAuthenticationToken authenticated=new
                        UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());//credentials: password

                //login olan user i security contexte koyma;
                SecurityContextHolder.getContext().setAuthentication(authenticated);

            }
        } catch (UsernameNotFoundException e) {
            throw new RuntimeException(e);
        }
        filterChain.doFilter(request,response);
    }



    //requestten token i almamiz lazim
    //bearer dan sonra bosluk ve ahshahshahetrtghyjrw--> token
    private String parseJwt(HttpServletRequest request){

        String header=request.getHeader("Authorization");
        if(StringUtils.hasText(header) && header.startsWith("Bearer ")) {//dolu mu diye kontrol ediyor. string header dolu mu
        return  header.substring(7);//bearer tan sonra 7. karakterten sonrakilerini al
        }
        return null;
    }


}
