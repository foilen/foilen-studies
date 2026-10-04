package com.foilen.studies.managers;

import com.foilen.smalltools.email.EmailBuilder;
import com.foilen.smalltools.email.EmailService;
import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tools.StringTools;
import com.foilen.studies.controllers.models.ChangePasswordForm;
import com.foilen.studies.controllers.models.LoginForm;
import com.foilen.studies.controllers.models.LoginWithCodeForm;
import com.foilen.studies.controllers.models.LoginWithCodeRequestForm;
import com.foilen.studies.data.UserRepository;
import com.foilen.studies.data.user.UserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

@Service
public class UserManagerImpl extends AbstractBasics implements UserManager {

    private static final long TEN_MINUTES_MILLIS = 10L * 60L * 1000L;
    private static final long CODE_RATE_LIMIT_MILLIS = 60_000L; // 1 minute
    private static final long CODE_VALIDITY_MILLIS = TEN_MINUTES_MILLIS;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SecurityContextRepository securityContextRepository;
    @Autowired
    private UserRepository userRepository;

    @Value("${app.mailFrom}")
    private String mailFrom;

    private void authenticate(UserDetails user, HttpServletRequest request, HttpServletResponse response) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(user.getEmail(), null, List.of()));
        SecurityContextHolder.setContext(securityContext);
        securityContextRepository.saveContext(securityContext, request, response);
    }

    @Override
    public FormResult changePassword(Authentication authentication, ChangePasswordForm form) {

        FormResult formResult = new FormResult();
        UserDetails user = getOrCreateUser(authentication);

        if (user.getPasswordHash() != null) {
            if (isEmpty(form.getCurrentPassword())) {
                addValidationError(formResult, "currentPassword", "Obligatoire");
            } else if (!passwordEncoder.matches(form.getCurrentPassword(), user.getPasswordHash())) {
                addValidationError(formResult, "currentPassword", "Le mot de passe actuel est incorrect");
            }
        }
        if (isEmpty(form.getNewPassword())) {
            addValidationError(formResult, "newPassword", "Obligatoire");
        }
        if (isEmpty(form.getNewPasswordConfirmation())) {
            addValidationError(formResult, "newPasswordConfirmation", "Obligatoire");
        }
        if (!Objects.equals(form.getNewPassword(), form.getNewPasswordConfirmation())) {
            addValidationError(formResult, "newPasswordConfirmation", "Les mots de passe ne sont pas identiques");
        }
        if (!formResult.isSuccess()) {
            return formResult;
        }

        user.setPasswordHash(passwordEncoder.encode(form.getNewPassword()));
        user.setPasswordLastChange(new Date());
        userRepository.save(user);

        return formResult;
    }

    private void addValidationError(FormResult formResult, String fieldName, String error) {
        formResult.getValidationErrorsByField().computeIfAbsent(fieldName, _ -> new java.util.ArrayList<>()).add(error);
    }

    private String generateCode() {
        return String.format("%06d", ThreadLocalRandom.current().nextInt(1_000_000));
    }

    @Override
    public UserDetails getOrCreateUser(Authentication authentication) {
        if (authentication == null) {
            throw new RuntimeException("Not logged in");
        }

        var user = userRepository.findByEmail(authentication.getName());
        if (user == null || user.isDisabled()) {
            throw new RuntimeException("Unknown user");
        }
        return user;
    }

    private boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    @Override
    public FormResult login(LoginForm form, HttpServletRequest request, HttpServletResponse response) {

        FormResult formResult = new FormResult();

        String email = normalizeEmail(form.getEmail());
        validateEmail(formResult, email);
        if (isEmpty(form.getPassword())) {
            addValidationError(formResult, "password", "Obligatoire");
        }
        if (!formResult.isSuccess()) {
            return formResult;
        }

        UserDetails user = userRepository.findByEmail(email);
        if (user == null || user.getPasswordHash() == null || !passwordEncoder.matches(form.getPassword(), user.getPasswordHash())) {
            formResult.getGlobalErrors().add("Courriel ou mot de passe invalide");
            return formResult;
        }
        if (user.isDisabled()) {
            formResult.getGlobalErrors().add("Ce compte est désactivé");
            return formResult;
        }

        logger.info("User {} logged in with a password", email);
        authenticate(user, request, response);

        user.setLastLoginDate(new Date());
        userRepository.save(user);

        return formResult;
    }

    @Override
    public FormResult loginWithCode(LoginWithCodeForm form, HttpServletRequest request, HttpServletResponse response) {

        FormResult formResult = new FormResult();

        String email = normalizeEmail(form.getEmail());
        String code = form.getCode() == null ? null : form.getCode().trim();
        validateEmail(formResult, email);
        if (isEmpty(code)) {
            addValidationError(formResult, "code", "Obligatoire");
        }
        if (!formResult.isSuccess()) {
            return formResult;
        }

        UserDetails user = userRepository.findByEmail(email);
        if (user == null || !StringTools.safeEquals(user.getLoginCode(), code)) {
            formResult.getGlobalErrors().add("Courriel ou code invalide");
            return formResult;
        }
        if (user.isDisabled()) {
            formResult.getGlobalErrors().add("Ce compte est désactivé");
            return formResult;
        }
        if (user.getLoginCodeExpiration() == null || user.getLoginCodeExpiration().before(new Date())) {
            formResult.getGlobalErrors().add("Le code a expiré. Demandez-en un nouveau");
            return formResult;
        }

        logger.info("User {} logged in with a code", email);
        authenticate(user, request, response);

        // Single use
        user.setLastLoginDate(new Date());
        user.setLoginCode(null);
        user.setLoginCodeExpiration(null);
        userRepository.save(user);

        return formResult;
    }

    @Override
    public FormResult loginWithCodeRequest(LoginWithCodeRequestForm form) {

        FormResult formResult = new FormResult();

        String email = normalizeEmail(form.getEmail());
        validateEmail(formResult, email);
        if (!formResult.isSuccess()) {
            return formResult;
        }

        // Get or create
        UserDetails user = userRepository.findByEmail(email);
        if (user == null) {
            logger.info("Creating user {}", email);
            user = new UserDetails();
            user.setEmail(email);
            user.setCreationDate(new Date());
        }
        if (user.isDisabled()) {
            formResult.getGlobalErrors().add("Ce compte est désactivé");
            return formResult;
        }

        // Rate limit
        if (user.getLoginCodeLastGenerated() != null && System.currentTimeMillis() - user.getLoginCodeLastGenerated().getTime() < CODE_RATE_LIMIT_MILLIS) {
            formResult.getGlobalErrors().add("Un code a déjà été envoyé. Attendez une minute avant d'en demander un nouveau");
            return formResult;
        }

        String code = generateCode();
        user.setLoginCode(code);
        user.setLoginCodeExpiration(new Date(System.currentTimeMillis() + CODE_VALIDITY_MILLIS));
        user.setLoginCodeLastGenerated(new Date());
        userRepository.save(user);

        logger.info("User {} generated a login code", email);
        sendLoginCodeEmail(email, code);

        return formResult;
    }

    @Override
    public FormResult logout(HttpServletRequest request, HttpServletResponse response) {

        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        securityContextRepository.saveContext(SecurityContextHolder.createEmptyContext(), request, response);

        return new FormResult();
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    private void sendLoginCodeEmail(String email, String code) {

        String subject = "Votre code de connexion";
        String body = "Votre code de connexion est : " + code + "\n\nIl expire dans 10 minutes. Si vous n'avez pas fait cette demande, vous pouvez ignorer ce courriel.";

        EmailBuilder emailBuilder = new EmailBuilder();
        emailBuilder.setFrom(mailFrom);
        emailBuilder.addTo(email);
        emailBuilder.setSubject(subject);
        emailBuilder.setBodyTextFromString(body);

        emailService.sendEmail(emailBuilder);
    }

    private void validateEmail(FormResult formResult, String email) {
        if (isEmpty(email)) {
            addValidationError(formResult, "email", "Obligatoire");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            addValidationError(formResult, "email", "Courriel invalide");
        }
    }

}
