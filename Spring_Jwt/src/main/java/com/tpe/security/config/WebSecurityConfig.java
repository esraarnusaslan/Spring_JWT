package com.tpe.security.config;


import com.tpe.security.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//yetkilendirme yapmak icin
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthTokenFilter authTokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.csrf().disable().
              sessionManagement().
              sessionCreationPolicy(SessionCreationPolicy.STATELESS).//oturumlar ile ilgili bir sey yapmasina gerek kalmasin demek.session yok cunku
              and().authorizeRequests().
              antMatchers("/register","/login").permitAll().
              anyRequest().authenticated();

      http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    //provider config etmeden auth manageri config etme

    //password encoder eklicez
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();//defaulltaki zorluk derecesi 10 dur.
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    //parentindaki classtan geliyor authenticationManager(); bu method
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }












}

















