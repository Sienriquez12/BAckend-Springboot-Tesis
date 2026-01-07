package com.especlub.match.controller.publics;

import com.especlub.match.docs.AuthControllerDoc;
import com.especlub.match.dto.request.*;
import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.services.interfaces.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController implements AuthControllerDoc {

    // injects dependencies
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JsonDtoResponse<Boolean>> login(@RequestBody @Valid LoginInternalRequest loginInternalRequest,
                                                          HttpServletRequest request,
                                                          HttpServletResponse responseHttp) {
        boolean result = authService.loginSession(loginInternalRequest, request, responseHttp);
        return JsonDtoResponse.ok("Inicio de sesión correcto", result).toResponseEntity();
    }

    @GetMapping("/validate")
    public ResponseEntity<JsonDtoResponse<Void>> validateToken(HttpServletRequest request) {
        authService.validateUserJWT(request);
        return JsonDtoResponse.<Void>ok("Token validado",null).toResponseEntity();
    }

    @PostMapping("/logout")
    public ResponseEntity<JsonDtoResponse<Boolean>> logout(HttpServletRequest request, HttpServletResponse responseHttp) {
        boolean result = authService.logoutSession(request, responseHttp);
        return JsonDtoResponse.ok("Cierre de sesión correcto", result).toResponseEntity();
    }

    @PostMapping("/pre-register")
    public ResponseEntity<JsonDtoResponse<Boolean>> register(@RequestBody @Valid UserInfoRequestDto registerRequest) {
        boolean result = authService.preRegisterUserWithRoleStudent(registerRequest);
        return JsonDtoResponse.ok("Pre-Registro de usuario correcto", result).toResponseEntity();
    }

    @PostMapping("/activation")
    public ResponseEntity<JsonDtoResponse<Boolean>> activateUser(@RequestBody @Valid ActivationRequestDto activationRequest) {
        boolean result = authService.activateUserWithPin(activationRequest);
        return JsonDtoResponse.ok("Usuario activado correctamente", result).toResponseEntity();
    }

    @PostMapping("/recovery-password")
    public ResponseEntity<JsonDtoResponse<Boolean>> recoverPassword(@RequestBody @Valid PasswordRecoveryRequestDto passwordRecoveryRequestDto) {
        boolean result = authService.sendPasswordRecoveryPin(passwordRecoveryRequestDto);
        return JsonDtoResponse.ok("Instrucciones de recuperación enviadas si el correo existe", result).toResponseEntity();
    }

    @PostMapping("/validate-pin")
    public ResponseEntity<JsonDtoResponse<Boolean>> validateRecoveryPin(@RequestBody @Valid PinValidationRequestDto dto) {
        boolean result = authService.validatePasswordRecoveryPin(dto);
        return JsonDtoResponse.ok("PIN de recuperación validado correctamente", result).toResponseEntity();
    }

    @PostMapping("/update-password")
    public ResponseEntity<JsonDtoResponse<Boolean>> updatePasswordWithPin(@RequestBody @Valid PasswordUpdateWithPinRequestDto dto) {
        boolean result = authService.updatePasswordWithPin(dto);
        return JsonDtoResponse.ok("Contraseña actualizada correctamente", result).toResponseEntity();
    }
}
