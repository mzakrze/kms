package pl.mzakrze.kms.config;

public enum ApplicationEnvironment {
    PRODUCTION("production"),
    DEVELOPER("developer");

    ApplicationEnvironment(String code) {
        this.code = code;
    }

    private String code;

    public String getCode() {
        return code;
    }

    public static ApplicationEnvironment fromString(String code){
        if(PRODUCTION.code.equals(code)){
            return PRODUCTION;
        } else if (DEVELOPER.code.equals(code)) {
            return DEVELOPER;
        }
        throw new IllegalStateException("Invalid application environment");
    }
}
