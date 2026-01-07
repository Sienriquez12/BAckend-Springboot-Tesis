package com.especlub.match.services.interfaces;

import com.especlub.match.dto.request.*;
import com.especlub.match.models.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public interface AuthService {
    boolean loginSession(LoginInternalRequest loginRequest, HttpServletRequest request, HttpServletResponse responseHttp);
    UserInfo validateUserJWT(HttpServletRequest request);
    boolean logoutSession(HttpServletRequest request, HttpServletResponse responseHttp);
    boolean preRegisterUserWithRoleStudent(UserInfoRequestDto dto);
    boolean sendPasswordRecoveryPin(PasswordRecoveryRequestDto requestDto);
    boolean validatePasswordRecoveryPin(PinValidationRequestDto pinValidationRequestDto);
    boolean updatePasswordWithPin(PasswordUpdateWithPinRequestDto dto);
    boolean updatePasswordInternal(PasswordUpdateInternalRequestDto dto, String jwtToken);
    boolean activateUserWithPin(ActivationRequestDto activationRequestDto);
}
