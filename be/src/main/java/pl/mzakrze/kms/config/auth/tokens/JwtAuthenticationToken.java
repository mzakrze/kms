package pl.mzakrze.kms.config.auth.tokens;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import pl.mzakrze.kms.config.auth.UserRoles;

import java.util.Arrays;
import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 2877954820905567501L;
    private String tmpJwToken;

    public JwtAuthenticationToken(String tmpJwToken, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.tmpJwToken = tmpJwToken;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Arrays.asList(UserRoles.USER_AUTHORITY);
    }

    @Override
    public Object getCredentials() {
        return "this is credentials";
    }

    @Override
    public Object getPrincipal() {
        return "this is principal";
    }

    public String getTmpJwToken() {
        return tmpJwToken;
    }

    public void setTmpJwToken(String tmpJwToken) {
        this.tmpJwToken = tmpJwToken;
    }
}
