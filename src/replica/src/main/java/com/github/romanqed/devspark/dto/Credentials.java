package com.github.romanqed.devspark.dto;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class Credentials implements Validated {
    private static final Predicate<String> EMAIL_PATTERN = Pattern.compile("\\S+@\\S+").asMatchPredicate();

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void validate() throws ValidateException {
        if (email == null) {
            throw new ValidateException("Missing email");
        }
        if (password == null) {
            throw new ValidateException("Missing password");
        }
        if (!EMAIL_PATTERN.test(email)) {
            throw new ValidateException("Invalid email");
        }
    }
}
