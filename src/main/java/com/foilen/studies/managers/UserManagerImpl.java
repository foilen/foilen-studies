package com.foilen.studies.managers;

import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.studies.data.UserRepository;
import com.foilen.studies.data.user.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class UserManagerImpl extends AbstractBasics implements UserManager {

    private static final long TEN_MINUTES_MILLIS = 10L * 60L * 1000L;

    @Autowired
    private UserRepository userRepository;

    @Value("${app.auth.local.enabled:false}")
    private boolean localAuthEnabled;
    @Value("${app.auth.local.userId:local-user}")
    private String localUserId;

    @Override
    public UserDetails getOrCreateUser(Authentication authentication) {
        String providerId;
        if (authentication instanceof OAuth2AuthenticationToken authenticationToken) {
            var providerName = authenticationToken.getAuthorizedClientRegistrationId();
            var providerUserId = authenticationToken.getName();
            providerId = providerName + "-" + providerUserId;
        } else if (localAuthEnabled) {
            // Use fixed user ID for local development
            providerId = "local-" + localUserId;
            logger.debug("Using local authentication with user ID: {}", localUserId);
        } else {
            throw new RuntimeException("Not OAuth2");
        }

        // Get or create
        UserDetails userDetails = userRepository.findByProviderIds(providerId);
        Date now = new Date();
        if (userDetails == null) {
            userDetails = new UserDetails();
            userDetails.getProviderIds().add(providerId);
            userDetails.setCreationDate(now);
            userDetails.setLastLoginDate(now);
            userDetails = userRepository.save(userDetails);
        } else {
            Date last = userDetails.getLastLoginDate();
            if (last == null || (now.getTime() - last.getTime()) > TEN_MINUTES_MILLIS) {
                userDetails.setLastLoginDate(now);
                userDetails = userRepository.save(userDetails);
            }
        }

        return userDetails;
    }
}
