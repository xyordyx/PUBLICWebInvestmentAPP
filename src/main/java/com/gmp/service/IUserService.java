package com.gmp.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import com.gmp.persistence.model.*;
import com.gmp.web.dto.UserDto;
import com.gmp.web.error.UserAlreadyExistException;

public interface IUserService {

    User registerNewUserAccount(UserDto accountDto) throws UserAlreadyExistException;

    User registerNewUserAccount(User user) throws UserAlreadyExistException;

    void updateUserAdmin(User user,boolean change);

    void saveFinsmartProfile(final SmartUser smartUser);

    void saveFacturedoProfile(final FactUser factUser);

    SmartUser getFinsmartProfile(final User user);

    FactUser getFactuProfile(final User user);

    User getUser(String verificationToken);

    void saveRegisteredUser(User user);

    void deleteUser(User user);

    void createVerificationTokenForUser(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);

    VerificationToken generateNewVerificationToken(String token);

    void createPasswordResetTokenForUser(User user, String token);

    User findUserByEmail(String email);

    PasswordResetToken getPasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

//    Optional<User> getUserByID(long id);

    User getUserByID(long id);

    void changeUserPassword(User user, String password);

    boolean checkIfValidOldPassword(User user, String password);

    String validateVerificationToken(String token);

    String generateQRUrl(User user) throws UnsupportedEncodingException;

    User updateUser2FA(boolean use2FA);

    void updateUser(User user);

    void updateUser(User user, Boolean flag);

    List<String> getUsersFromSessionRegistry();

    //NewLocationToken isNewLoginLocation(String username, String ip);

    String isValidNewLocationToken(String token);

    //void addUserLocation(User user, String ip);

    List<User> getAllUsers();

}
