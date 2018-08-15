package pl.mzakrze.kms.user;

import pl.mzakrze.kms.user.exceptions.IllegalPasswordException;

class PasswordValidator {
    public static Boolean validatePassword(String password) throws IllegalPasswordException {
        // TODO - uzupełnić
        if(password.length() < 1) {
            throw new IllegalPasswordException("Password is too short");
        }

        return true;
    }
}
