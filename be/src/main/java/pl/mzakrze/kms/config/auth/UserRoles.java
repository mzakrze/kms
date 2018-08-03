package pl.mzakrze.kms.config.auth;

import org.springframework.security.core.GrantedAuthority;

public class UserRoles {
    public final static String USER = "USER";
    public final static String ADMIN = "ADMIN";

    public final static GrantedAuthority USER_AUTHORITY = new GrantedAuthority() {
        @Override
        public String getAuthority() {
            return USER;
        }
    };

    public final static GrantedAuthority ADMIN_AUTHORITY = new GrantedAuthority() {
        @Override
        public String getAuthority() {
            return ADMIN;
        }
    };
}
