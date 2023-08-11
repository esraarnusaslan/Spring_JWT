package com.tpe.security;


import com.tpe.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtils {

    //hash(abc)--> ahdajhsjd--> abc ye donusturulemez yani tek yonlu sifreleme
    //jwt token: header + payload(userla ilgili bilgiler) + signature(secret key ile imzalaniyor geriye donusturulemez)




    private long jwtExpirationTime=86400000;//24saat*60dk*60sn*1000ms
    private String secretKey="techpro";

    //   ***************  1-JWT token generate   ***************

    public String generateToken(Authentication authentication){
        UserDetailsImpl userDetails =(UserDetailsImpl) authentication.getPrincipal();//login olmus user a ulasmak icin(authenticated)

        //login olan user i aldik. username i ni token icine koymak icin
        return Jwts.builder().//token uretiyor.jwt olusturucu
                setSubject(userDetails.getUsername()).//userin username ini getir.
                setIssuedAt(new Date()).//token ne zaman baslamis hangi an da.Date()parametre girmezsen currentTimeMillies() getirir
                setExpiration(new Date(new Date().getTime()+jwtExpirationTime)).//ne zaman sona erecek.suresi ne kadar olmali
                signWith(SignatureAlgorithm.HS512,secretKey).//hashleme ile tek yonlu sifreleme kullanilmis oluyor.karsilastirilmalarda kullanilir
                compact();//ayarlari tamamla ve tokeni olustur.
    }



    //   ***************  2-JWT token validate   ***************

    public boolean validateToken(String token){
        try {
            Jwts.parser().//ayristirici. token i ayristirir
            setSigningKey(secretKey).//tek yonlu olusturulan sifreyle karsilastir
            parseClaimsJws(token);//anahtar ile karsilastiriyor. token nin icindeki imza-anahtar
                                 // ile benim veridgim imza-anahtar uyumlu mu diye bakiyor uyumlu ise
                                // JWT gecerli oluyor.
            return true;
        } catch (ExpiredJwtException e) {
            e.getStackTrace();
        } catch (UnsupportedJwtException e) {
            e.getStackTrace();
        } catch (MalformedJwtException e) {
            e.getStackTrace();
        } catch (SignatureException e) {
            e.getStackTrace();
        } catch (IllegalArgumentException e) {
            e.getStackTrace();
        }
        return false;
    }





//   ***************  3-JWT tokendan username i almamiz gerek   ***************
    public String getUsernameFromJwtToken(String token){
        return Jwts.parser().
                setSigningKey(secretKey).
                parseClaimsJws(token).//dogrulanmis tokenin icerisindeki claimslerin donduruyor.
                getBody().
                getSubject();//username var icinde
    }








}
