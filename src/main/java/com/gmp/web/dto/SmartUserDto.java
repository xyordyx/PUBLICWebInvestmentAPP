package com.gmp.web.dto;

import com.gmp.validation.ValidEmail;
import com.gmp.validation.ValidPassword;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SmartUserDto {
    @ValidPassword
    private String password;

    @ValidEmail
    @NotNull
    @Size(min = 1, message = "{Size.userDto.email}")
    private String email;

    @Override
    public String toString() {
        return "SmartUserDto{" +
                "password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
