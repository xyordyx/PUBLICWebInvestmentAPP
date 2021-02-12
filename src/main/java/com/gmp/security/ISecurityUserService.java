package com.gmp.security;

public interface ISecurityUserService {

    String validatePasswordResetToken(String token);

}
