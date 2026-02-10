package com.especlub.match.services.impl;

import com.especlub.match.dto.request.CreateUserRequestDto;
import com.especlub.match.dto.request.UpdateUserRequestDto;
import com.especlub.match.dto.request.UserAdminDto;
import com.especlub.match.models.*;
import com.especlub.match.repositories.*;
import com.especlub.match.services.interfaces.AdminUserService;
import com.especlub.match.shared.exceptions.CustomExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserInfoRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    // nuevos repos
    private final ClubRepository clubRepository;
    private final StudentRepository studentRepository;
    private final ClubMemberRepository clubMemberRepository;

    private static final String USER_NOT_FOUND_MSG = "User not found";
    private static final String ROLE_PRESIDENT_NAME = "ROLE_PRESIDENT";

    @Override
    @Transactional(readOnly = true)
    public List<UserAdminDto> listAllActive() {
        return userRepository.findAllByRecordStatusTrue()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserAdminDto getById(Long id) {
        UserInfo u = userRepository.findByIdAndRecordStatusTrue(id)
                .orElseThrow(() -> new CustomExceptions(USER_NOT_FOUND_MSG, 404));
        return toDto(u);
    }

    @Override
    @Transactional
    public UserAdminDto create(CreateUserRequestDto dto) {
        // Validate that the role exists and is active
        UserRole role = userRoleRepository.findByIdAndRecordStatusTrue(dto.getRoleId())
                .orElseThrow(() -> new CustomExceptions("Role not found or inactive", 404));

        // If there's an active user with same email or username -> conflict
        if (userRepository.existsByEmailAndRecordStatusTrue(dto.getEmail())) {
            throw new CustomExceptions("Email already in use", 400);
        }
        if (userRepository.existsByUsernameAndRecordStatusTrue(dto.getUsername())) {
            throw new CustomExceptions("Username already in use", 400);
        }

        // If there is an inactive user with the same email -> reactivate and update
        UserInfo inactiveByEmail = userRepository.findByEmailAndRecordStatusFalse(dto.getEmail());
        if (inactiveByEmail != null) {
            inactiveByEmail.setUsername(dto.getUsername());
            inactiveByEmail.setEmail(dto.getEmail());
            inactiveByEmail.setPhone(dto.getPhone());
            inactiveByEmail.setNames(dto.getFirstName());
            inactiveByEmail.setSurnames(dto.getLastName());
            inactiveByEmail.setBirthDate(dto.getDateOfBirth());
            inactiveByEmail.setPassword(passwordEncoder.encode(dto.getPassword()));
            // guardar nationalId si se proporciona
            if (dto.getNationalId() != null) inactiveByEmail.setNationalId(dto.getNationalId());
            inactiveByEmail.setRecordStatus(true);
            inactiveByEmail.setRoles(List.of(role));
            inactiveByEmail.setUpdatedAt(LocalDateTime.now());
            inactiveByEmail.setCreatedAt(LocalDateTime.now());
            inactiveByEmail.setAcceptPrivacy(true);
            inactiveByEmail.setAcceptTerms(true);
            inactiveByEmail.setFirstLogin(false);
            UserInfo saved = userRepository.save(inactiveByEmail);

            // si se asignó role de presidente y viene clubId, asignar como presidente
            if (ROLE_PRESIDENT_NAME.equals(role.getName()) && dto.getClubId() != null) {
                assignPresidentToClub(saved.getId(), dto.getClubId());
            }

            return toDto(saved);
        }

        // If there is an inactive user with the same username -> reactivate and update
        UserInfo inactiveByUsername = userRepository.findByUsernameAndRecordStatusFalse(dto.getUsername());
        if (inactiveByUsername != null) {
            inactiveByUsername.setUsername(dto.getUsername());
            inactiveByUsername.setEmail(dto.getEmail());
            inactiveByUsername.setPhone(dto.getPhone());
            inactiveByUsername.setNames(dto.getFirstName());
            inactiveByUsername.setSurnames(dto.getLastName());
            inactiveByUsername.setBirthDate(dto.getDateOfBirth());
            inactiveByUsername.setPassword(passwordEncoder.encode(dto.getPassword()));
            // guardar nationalId si se proporciona
            if (dto.getNationalId() != null) inactiveByUsername.setNationalId(dto.getNationalId());
            inactiveByUsername.setRecordStatus(true);
            inactiveByUsername.setRoles(List.of(role));
            inactiveByUsername.setUpdatedAt(LocalDateTime.now());
            inactiveByUsername.setCreatedAt(LocalDateTime.now());
            inactiveByUsername.setAcceptPrivacy(true);
            inactiveByUsername.setAcceptTerms(true);
            inactiveByUsername.setFirstLogin(false);

            UserInfo saved = userRepository.save(inactiveByUsername);

            if (ROLE_PRESIDENT_NAME.equals(role.getName()) && dto.getClubId() != null) {
                assignPresidentToClub(saved.getId(), dto.getClubId());
            }

            return toDto(saved);
        }

        // Otherwise create a fresh user
        UserInfo user = new UserInfo();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setNames(dto.getFirstName());
        user.setSurnames(dto.getLastName());
        user.setBirthDate(dto.getDateOfBirth());
        // nationalId si se proporcionó
        if (dto.getNationalId() != null) user.setNationalId(dto.getNationalId());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRecordStatus(true);
        user.setRoles(List.of(role));
        user.setUpdatedAt(LocalDateTime.now());
        user.setCreatedAt(LocalDateTime.now());
        user.setAcceptPrivacy(true);
        user.setAcceptTerms(true);
        user.setFirstLogin(false);
        UserInfo saved = userRepository.save(user);

        // si se asignó role de presidente y viene clubId, asignar como presidente
        if (ROLE_PRESIDENT_NAME.equals(role.getName()) && dto.getClubId() != null) {
            assignPresidentToClub(saved.getId(), dto.getClubId());
        }

        return toDto(saved);
    }

    @Override
    @Transactional
    public UserAdminDto update(Long id, UpdateUserRequestDto dto) {
        UserInfo user = userRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions(USER_NOT_FOUND_MSG, 404));
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getFirstName() != null) user.setNames(dto.getFirstName());
        if (dto.getLastName() != null) user.setSurnames(dto.getLastName());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        // actualizar nationalId si se proporciona
        if (dto.getNationalId() != null) {
            user.setNationalId(dto.getNationalId());
        }

        // Handle role updates: null -> no change; empty list -> clear roles; otherwise set to provided roles
        boolean assignedPresident = false;
        if (dto.getRoleIds() != null) {
            List<Long> roleIds = dto.getRoleIds();
            if (roleIds.isEmpty()) {
                user.setRoles(new ArrayList<>());
            } else {
                List<UserRole> roles = userRoleRepository.findAllByIdInAndRecordStatusTrue(roleIds);
                if (roles.size() != roleIds.size()) {
                    throw new CustomExceptions("One or more roles not found or inactive", 404);
                }
                user.setRoles(roles);
                // comprobar si entre los roles está ROLE_PRESIDENT
                assignedPresident = roles.stream().anyMatch(r -> ROLE_PRESIDENT_NAME.equals(r.getName()));
            }
        }

        UserInfo saved = userRepository.save(user);

        // Si hubo un cambio de roles y ahora NO tiene ROLE_PRESIDENT -> quitar presidencias previas
        if (dto.getRoleIds() != null && !assignedPresident) {
            // intentar localizar student y limpiar isPresident en sus memberships
            Optional<Student> maybeStudent = studentRepository.findByUserInfo_IdAndRecordStatusTrue(saved.getId());
            if (maybeStudent.isPresent()) {
                Student st = maybeStudent.get();
                List<ClubMember> memberships = clubMemberRepository.findAllByStudentIdAndRecordStatusTrue(st.getId());
                for (ClubMember cm : memberships) {
                    if (Boolean.TRUE.equals(cm.getIsPresident())) {
                        cm.setIsPresident(false);
                        clubMemberRepository.save(cm);
                    }
                }
            }
        }

         // si en la edición se asignó role de presidente y se envió clubId
         // si roleIds fue null (no cambio de roles), mantener roles actuales: si usuario ya tiene ROLE_PRESIDENT y viene clubId -> asignar
         if (!assignedPresident && dto.getRoleIds() == null) {
             assignedPresident = saved.getRoles() != null && saved.getRoles().stream().anyMatch(r -> ROLE_PRESIDENT_NAME.equals(r.getName()));
         }

         if (assignedPresident && dto.getClubId() != null) {
             assignPresidentToClub(saved.getId(), dto.getClubId());
         }

        return toDto(saved);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        UserInfo user = userRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions(USER_NOT_FOUND_MSG, 404));
        user.setRecordStatus(false);
        userRepository.save(user);
        return true;
    }

    private UserAdminDto toDto(UserInfo u) {
        List<String> roleNames = new ArrayList<>();
        if (u.getRoles() != null) {
            roleNames = u.getRoles().stream()
                    .map(UserRole::getName)
                    .toList();
        }

        return UserAdminDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .phone(u.getPhone())
                .firstName(u.getNames())
                .lastName(u.getSurnames())
                .dateOfBirth(u.getBirthDate())
                .roles(roleNames)
                .build();
    }

    // Helper para asignar presidente a un club, garantizando un solo presidente por club
    private void assignPresidentToClub(Long userInfoId, Long clubId) {
        // encontrar el club activo
        var club = clubRepository.findByIdAndRecordStatusTrue(clubId)
                .orElseThrow(() -> new CustomExceptions("Club not found or inactive", 404));

        // obtener (o crear) el student asociado al userInfo
        Student student = studentRepository.findByUserInfo_IdAndRecordStatusTrue(userInfoId)
                .orElseGet(() -> {
                    Student s = Student.builder().userInfo(userRepository.findById(userInfoId).orElseThrow(() -> new CustomExceptions("User not found", 404))).recordStatus(true).build();
                    return studentRepository.save(s);
                });

        // buscar presidente actual
        Optional<ClubMember> currentPres = clubMemberRepository.findPresidentByClubId(clubId);
        if (currentPres.isPresent()) {
            ClubMember prev = currentPres.get();
            // Si ya existe un presidente distinto, no permitir la asignación -> lanzar excepción personalizada
            if (!prev.getStudent().getId().equals(student.getId())) {
                throw new CustomExceptions("No puede ser presidente de ese club porque ya existe uno", 400);
            }
        }

        // buscar o crear membresía del student en el club
        Optional<ClubMember> membershipOpt = clubMemberRepository.findByClubIdAndStudentIdAndRecordStatusTrue(clubId, student.getId());
        ClubMember membership;
        if (membershipOpt.isPresent()) {
            membership = membershipOpt.get();
            membership.setIsPresident(true);
            membership.setRecordStatus(true);
        } else {
            membership = ClubMember.builder()
                    .club(club)
                    .student(student)
                    .recordStatus(true)
                    .isPresident(true)
                    .build();
        }
        clubMemberRepository.save(membership);
    }
}
