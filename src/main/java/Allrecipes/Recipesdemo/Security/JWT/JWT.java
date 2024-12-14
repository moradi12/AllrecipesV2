package Allrecipes.Recipesdemo.Security.JWT;

import Allrecipes.Recipesdemo.Entities.UserDetails;
import Allrecipes.Recipesdemo.Entities.Enums.UserType;
import io.jsonwebtoken.*;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.login.LoginException;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWT {

    private String signatureAlgorithm = SignatureAlgorithm.HS256.getJcaName();
    //our private key - מפתח הצפנה שקיים רק אצלנו
    private String encodedSecretKey = "jwt+secret+key+is+used+for+signing+and+validating+tokens+in+the+app";
    //create our private key = יצירה של מפתח ההצפנה לשימוש ביצירה של הטוקנים שלנו - VERIFY SIGNATURE
    private Key decodedSecretKey = new SecretKeySpec(Base64.getDecoder().decode(encodedSecretKey), this.signatureAlgorithm);

    public String generateToken(UserDetails userData) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userType", userData.getUserType());
        claims.put("userName", userData.getUserName());
        claims.put("id", userData.getUserId());
        return createToken(claims, userData.getEmail());
    }

    public String generateToken(String token) {
        Map<String, Object> claims = new HashMap<>();
        Claims ourClaims = extractAllClaims(token);
        claims.put("userName", ourClaims.get("userName"));
        claims.put("userType", ourClaims.get("userType"));
        claims.put("id", ourClaims.get("id"));
        return createToken(claims, ourClaims.getSubject());
    }

    private String createToken(Map<String, Object> claims, String email) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setClaims(claims) //get claims
                .setSubject(email) //get subject
                .setIssuedAt(Date.from(now)) //get current time
                .setExpiration(Date.from(now.plus(30, ChronoUnit.MINUTES)))  //expiration date
                .signWith(this.decodedSecretKey)
                .compact();
    }

    public Claims extractAllClaims(String token) throws ExpiredJwtException, SignatureException {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(decodedSecretKey) //provide our secret key :o)
                .build();
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public String extractEmail(String token) throws SignatureException {
        return extractAllClaims(token).getSubject();
    }

    public java.util.Date extractExpirationDate(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        try {
            extractAllClaims(token);
            return false;
        } catch (ExpiredJwtException err) {
            return true;
        }
    }

    public String getUserType(String token) {
        Claims claims = extractAllClaims(token);
        return (String) claims.get("userType");
    }

    public boolean validateToken(String token, UserDetails userDetails) throws MalformedJwtException, SignatureException {
        final String userEmail = extractEmail(token);
        return (userEmail.equals(userDetails.getEmail()) && !isTokenExpired(token));
    }

    public boolean validateToken(String token) throws MalformedJwtException, SignatureException {
        final Claims claims = extractAllClaims(token);
        return true;
    }

    public boolean checkUser(String token, UserType userType) throws LoginException {
        String newToken = token.replace("Bearer ", "");
        if (validateToken(newToken)) {
            if (!getUserType(newToken).equals(userType)) {
                throw new LoginException("User not allowed");
            }
        }
        return true;
    }


    public HttpHeaders CheckTheJWT(String jwt) throws Exception {
        String myJWT = jwt.split(" ")[1];
        if (validateToken(myJWT)) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + generateToken(myJWT));
            return headers;
        }
        throw new Exception("Invalid token");
    }
    public HttpHeaders getHeaders(String jwt) {
        HttpHeaders headers = new HttpHeaders();
        String userJWT = jwt.split(" ")[1];
        if(validateToken(userJWT)){
            headers.set("Authorization","Bearer " + generateToken((userJWT)));
        }
        return  headers;
    }


    public UserDetails getUserDetails(String authHeader) throws LoginException {
        String token = authHeader.replace("Bearer ", "");
        if (!validateToken(token)) {
            throw new LoginException("Invalid or expired token.");
        }

        Claims claims = extractAllClaims(token);
        Long userId = claims.get("id", Long.class);
        String userName = claims.get("userName", String.class);
        String userTypeString = claims.get("userType", String.class);

        UserType userType;
        try {
            userType = UserType.valueOf(userTypeString);
        } catch (IllegalArgumentException e) {
            throw new LoginException("Invalid user type in token.");
        }

        return UserDetails.builder()
                .userId(userId)
                .userName(userName)
                .email(claims.getSubject())
                .userType(userType)
                .build();
    }
}