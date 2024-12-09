package Allrecipes.Recipesdemo.Security.JWT;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String JWT_SECRET = "jkfksdjfkjsdf92398JKASKJDKJsdh1h3jhdhd";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                var claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
                String username = claims.getSubject();
                String role = claims.get("role", String.class);

                // Load user details if needed and set the authentication in the context
                // For simplicity, assume we have a CustomUserDetails object
                // In reality, you'd implement UserDetailsService and load user details from DB.

                // SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // Invalid token
            }
        }
        filterChain.doFilter(request, response);
    }
}
