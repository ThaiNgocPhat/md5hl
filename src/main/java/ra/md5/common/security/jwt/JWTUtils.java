package ra.md5.common.security.jwt;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import ra.md5.common.security.principal.UserDetailsCustom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JWTUtils {
    @Value("${jwt.secret_key}")
    private String SECRET_KEY;
    @Value("${jwt.expired}")
    private Long EXPIRED;

    public String generateAccessToken(UserDetailsCustom detailCustom){
        return Jwts.builder()
                .setSubject(detailCustom.getUsername())
                .claim("roles", detailCustom.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(EXPIRED, ChronoUnit.MILLIS)))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String generateRefreshToken(UserDetailsCustom detailCustom){
        return Jwts.builder().setSubject(detailCustom.getUsername()).claim("role",detailCustom.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(EXPIRED * 10, ChronoUnit.MILLIS)))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }
    // validate token
    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }
    // giải mã token trả về username
    public String getUserNameFromToken(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }
}