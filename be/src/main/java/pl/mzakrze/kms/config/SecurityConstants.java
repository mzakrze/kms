package pl.mzakrze.kms.config;

import pl.mzakrze.kms.api.ApiConstants;

public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs"; // FIXME - produkcyjne wstrzykiwać do kontenera
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTHENTICATION_HEADER = "Authentication";
    public static final String SIGN_UP_URL = ApiConstants.API_V1 + "/user/sign_up";
    public static final String LOGIN_URL = ApiConstants.API_V1 + "/user/login";
}