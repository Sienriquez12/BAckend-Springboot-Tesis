package com.especlub.match.services.impl;

import com.especlub.match.dto.request.*;
import com.especlub.match.models.UserInfo;
import com.especlub.match.models.UserPin;
import com.especlub.match.models.UserRole;
import com.especlub.match.repositories.UserInfoRepository;
import com.especlub.match.repositories.UserPinRepository;
import com.especlub.match.repositories.UserRoleRepository;
import com.especlub.match.security.jwt.JwtProvider;
import com.especlub.match.services.interfaces.AuthService;
import com.especlub.match.services.interfaces.EmailService;
import com.especlub.match.services.interfaces.EmailServiceAsync;
import com.especlub.match.services.interfaces.OtpService;
import com.especlub.match.shared.enums.LinkFrontendEmuns;
import com.especlub.match.shared.enums.OtpPurpose;
import com.especlub.match.shared.exceptions.CustomExceptions;
import com.especlub.match.shared.utils.CookieUtils;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String VAR_USERNAME = "username";
    private static final String VAR_NOMBRE = "nombre";
    private static final String VAR_APELLIDOS = "apellidos";
    private static final String VAR_FECHA = "fecha";
    private static final String VAR_HORA = "hora";

    // injected dependencies
    private final UserInfoRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final CookieUtils cookieUtils;
    private final EmailServiceAsync emailServiceAsync;
    private final EmailService emailService;
    private final UserPinRepository userPinRepository;
    private final OtpService otpService;


    // declarate variables
    String message;

    public boolean loginSession(LoginInternalRequest loginRequest, HttpServletRequest request, HttpServletResponse responseHttp) {
        log.info("Login user: {}", loginRequest.getUsername());

        UserInfo user = null;
        if (loginRequest.getUsername() != null && !loginRequest.getUsername().isBlank()) {
            user = userRepository.findByUsernameAndRecordStatusTrue(loginRequest.getUsername());
        } else if (loginRequest.getEmail() != null && !loginRequest.getEmail().isBlank()) {
            user = userRepository.findByEmailAndRecordStatusTrue(loginRequest.getEmail());
        }
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new CustomExceptions("Usuario, correo o contraseña incorrectos", HttpStatus.UNAUTHORIZED.value());
        }

        String jwt = jwtProvider.generateJwtByUsername(user);
        cookieUtils.addTokenCookie(request, responseHttp, jwt);

        Map<String, Object> variables = new java.util.HashMap<>();
        variables.put(VAR_USERNAME, user.getUsername());
        variables.put("loginDate", java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        variables.put("loginTime", java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
        variables.put("ipAddress", request.getRemoteAddr());
        variables.put("accountActivityUrl", LinkFrontendEmuns.LINK_LOGIN.getUrl());
        emailServiceAsync.sendHtmlEmail("Nuevo inicio de sesión detectado",user.getEmail(),"login-notification.html", variables);

        return true;
    }


    @Override
    public UserInfo validateUserJWT(HttpServletRequest request) {
        String token = cookieUtils.extractTokenFromHeaderOrCookie(request);
        if (token == null) {
            message = "Token JWT no encontrado en la solicitud";
            throw new CustomExceptions(message);
        }

        boolean isValid = jwtProvider.validateToken(token);
        if (!isValid) {
            message = "Token JWT no válido o ha expirado";
            throw new JwtException(message);
        }

        String username = jwtProvider.getNombreUsuarioFromToken(token);
        UserInfo user = userRepository.findByUsernameAndRecordStatusTrue(username);

        if (user == null) {
            message = "Usuario no encontrado";
            throw new UsernameNotFoundException(message);
        }
        return (user);
    }

    public boolean logoutSession(HttpServletRequest request, HttpServletResponse responseHttp) {
        cookieUtils.clearTokenCookie(request, responseHttp);
        return true;
    }

    public boolean preRegisterUserWithRoleStudent(UserInfoRequestDto dto) {
        if (userRepository.existsByUsernameAndRecordStatusTrue(dto.getUsername())) {
            throw new CustomExceptions("El nombre de usuario ya está registrado", HttpStatus.BAD_REQUEST.value());
        }
        if (userRepository.existsByEmailAndRecordStatusTrue(dto.getEmail())) {
            throw new CustomExceptions("El correo electrónico ya está registrado", HttpStatus.BAD_REQUEST.value());
        }
        if (userRepository.existByNationalIdAndRecordStatusTrue(dto.getNationalId())) {
            throw new CustomExceptions("La cédula ya está registrada", HttpStatus.BAD_REQUEST.value());
        }

        UserInfo previous = userRepository.findByEmailAndRecordStatusFalse(dto.getEmail());
        if (previous != null) {
            List<UserPin> pins = userPinRepository.findAllByUser(previous);
            if (pins != null && !pins.isEmpty()) {
                userPinRepository.deleteAll(pins);
            }
            userRepository.delete(previous);
        }

        UserInfo user = UserInfo.builder()
            .username(dto.getUsername())
            .email(dto.getEmail())
            .phone(dto.getPhone())
            .names(dto.getNames())
            .surnames(dto.getSurnames())
            .birthDate(dto.getBirthDate())
            .acceptTerms(dto.getAcceptTerms())
            .acceptPrivacy(dto.getAcceptPrivacy())
            .nationalId(dto.getNationalId())
            .password(passwordEncoder.encode(dto.getPassword()))
            .recordStatus(false)
            .firstLogin(false)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        UserRole roleStudent = userRoleRepository.findByName("ROLE_STUDENT")
                .orElseThrow(() -> new RuntimeException("Rol ROLE_STUDENT no encontrado"));
        user.setRoles(List.of(roleStudent));

        userRepository.save(user);

        String pin = otpService.generatePin();
        int expireMinutes = otpService.getPinExpireMinutes();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(expireMinutes);
        UserPin userPin = UserPin.builder()
                .user(user)
                .pin(pin)
                .used(false)
                .createdAt(now)
                .expiresAt(expiresAt)
                .purpose(OtpPurpose.PRE_REGISTER.name())
                .build();
        userPinRepository.save(userPin);

        Map<String, Object> variables = new java.util.HashMap<>();
        variables.put(VAR_NOMBRE, user.getNames());
        variables.put(VAR_APELLIDOS, user.getSurnames());
        variables.put("codigoPin", pin);
        variables.put("email", user.getEmail());
        variables.put(VAR_USERNAME, user.getUsername());

        emailService.sendHtmlEmail(
            "Pre-registro de cuenta ESPE Clubs Match",
            user.getEmail(),
            "pre-register-activation.html",
            variables
        );

        return true;
    }


    @Override
    public boolean sendPasswordRecoveryPin(PasswordRecoveryRequestDto requestDto) {
        UserInfo user = userRepository.findByNationalIdAndEmailAndRecordStatusTrue(
            requestDto.getNationalId(), requestDto.getEmail());
        if (user == null) {
            throw new CustomExceptions("No se encontró un usuario con esa cédula y correo electrónico", org.springframework.http.HttpStatus.NOT_FOUND.value());
        }

        String pin = otpService.generatePin();
        int getPinExpireMinutes = otpService.getPinExpireMinutes();
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime expiresAt = now.plusMinutes(getPinExpireMinutes);

        UserPin userPin = UserPin.builder()
                .user(user)
                .pin(pin)
                .used(false)
                .createdAt(now)
                .expiresAt(expiresAt)
                .purpose(OtpPurpose.RECOVERY.name())
                .build();
        userPinRepository.save(userPin);

        Map<String, Object> variables = new java.util.HashMap<>();
        variables.put(VAR_NOMBRE, user.getNames());
        variables.put(VAR_APELLIDOS, user.getSurnames());
        variables.put("codigoPin", pin);

        emailService.sendHtmlEmail(
            "Recuperación de contraseña",
            user.getEmail(),
            "password-recovery-email.html",
            variables
        );

        return true;
    }

    @Override
    public boolean updatePasswordWithPin(PasswordUpdateWithPinRequestDto dto) {
        UserInfo user = getActiveUserByNationalIdAndEmail(dto.getNationalId(), dto.getEmail());
        findValidRecoveryPinOrThrow(user, dto.getPin(), true);

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        LocalDateTime now = java.time.LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String date = now.format(dateFormatter);
        String time = now.format(timeFormatter);

        Map<String, Object> variables = new java.util.HashMap<>();
        variables.put(VAR_NOMBRE, user.getNames());
        variables.put(VAR_APELLIDOS, user.getSurnames());
        variables.put(VAR_FECHA, date);
        variables.put(VAR_HORA, time);
        emailService.sendHtmlEmail(
            "Contraseña actualizada",
            user.getEmail(),
            "password-updated.html",
            variables
        );
        return true;
    }


    @Override
    public boolean updatePasswordInternal(PasswordUpdateInternalRequestDto dto, String jwtToken) {
        String username = jwtProvider.getNombreUsuarioFromToken(jwtToken);
        UserInfo user = userRepository.findByUsernameAndRecordStatusTrue(username);
        if (user == null) {
            throw new CustomExceptions("Usuario no encontrado o inactivo", org.springframework.http.HttpStatus.NOT_FOUND.value());
        }
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new CustomExceptions("La contraseña actual es incorrecta", org.springframework.http.HttpStatus.BAD_REQUEST.value());
        }
        if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new CustomExceptions("La nueva contraseña no puede ser igual a la actual", org.springframework.http.HttpStatus.BAD_REQUEST.value());
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        Map<String, Object> variables = new java.util.HashMap<>();
        variables.put(VAR_NOMBRE, user.getNames());
        variables.put(VAR_APELLIDOS, user.getSurnames());
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy");
        java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");
        variables.put(VAR_FECHA, now.format(dateFormatter));
        variables.put(VAR_HORA, now.format(timeFormatter));
        emailService.sendHtmlEmail(
            "Contraseña actualizada",
            user.getEmail(),
            "password-updated.html",
            variables
        );
        return true;
    }

    @Override
    public boolean activateUserWithPin(ActivationRequestDto activationRequestDto) {
        UserInfo user = userRepository.findByEmailAndRecordStatusFalse(activationRequestDto.getEmail());
        if (user == null) {
            throw new CustomExceptions("Usuario no encontrado o ya activado", HttpStatus.NOT_FOUND.value());
        }
        UserPin userPin = userPinRepository.findByUserAndPinAndPurpose(user, activationRequestDto.getPin(), OtpPurpose.PRE_REGISTER.name())
                .orElse(null);
        if (userPin == null || userPin.getUsed() || userPin.getExpiresAt().isBefore(java.time.LocalDateTime.now())) {
            throw new CustomExceptions("El PIN es inválido, ya fue usado o ha expirado", HttpStatus.BAD_REQUEST.value());
        }
        userPin.setUsed(true);
        userPin.setUsedAt(java.time.LocalDateTime.now());
        userPinRepository.save(userPin);

        user.setRecordStatus(true);
        user.setUpdatedAt(java.time.LocalDateTime.now());
        userRepository.save(user);

        Map<String, Object> variables = new java.util.HashMap<>();
        variables.put("email", user.getEmail());
        variables.put(VAR_USERNAME, user.getUsername());
        emailService.sendHtmlEmail(
            "¡Bienvenido a ESPE Clubs Match!",
            user.getEmail(),
            "create_account.html",
            variables
        );
        return true;
    }

    @Override
    public boolean validatePasswordRecoveryPin(PinValidationRequestDto pinValidationRequestDto){
        UserInfo user = getActiveUserByNationalIdAndEmail(pinValidationRequestDto.getNationalId(), pinValidationRequestDto.getEmail());

        findValidRecoveryPinOrThrow(user, pinValidationRequestDto.getPin(), false);
        return true;
    }

    private UserInfo getActiveUserByNationalIdAndEmail(String nationalId, String email) {
        UserInfo user = userRepository.findByNationalIdAndEmailAndRecordStatusTrue(nationalId, email);
        if (user == null) {
            throw new CustomExceptions("No se encontró un usuario con esa cédula y correo electrónico", org.springframework.http.HttpStatus.NOT_FOUND.value());
        }
        return user;
    }

    private void findValidRecoveryPinOrThrow(UserInfo user, String pin, boolean markAsUsed) {
        UserPin userPin = userPinRepository.findByUserAndPinAndPurpose(user, pin, OtpPurpose.RECOVERY.name()).orElse(null);
        if (userPin == null || userPin.getUsed() || userPin.getExpiresAt().isBefore(java.time.LocalDateTime.now())) {
            throw new CustomExceptions("El PIN es inválido, ya fue usado o ha expirado", org.springframework.http.HttpStatus.BAD_REQUEST.value());
        }
        if (markAsUsed) {
            userPin.setUsed(true);
            userPin.setUsedAt(java.time.LocalDateTime.now());
            userPinRepository.save(userPin);
        }
    }
}
