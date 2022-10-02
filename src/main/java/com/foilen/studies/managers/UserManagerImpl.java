package com.foilen.studies.managers;

import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.studies.data.UserRepository;
import com.foilen.studies.data.user.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class UserManagerImpl extends AbstractBasics implements UserManager {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails getOrCreateUser(Authentication authentication) {
        String providerId;
        if (authentication instanceof OAuth2AuthenticationToken authenticationToken) {
            var providerName = authenticationToken.getAuthorizedClientRegistrationId();
            var providerUserId = authenticationToken.getName();
            providerId = providerName + "-" + providerUserId;
        } else {
            throw new RuntimeException("Not OAuth2");
        }

        // Get or create
        UserDetails userDetails = userRepository.findByProviderIds(providerId);
        if (userDetails == null) {
            userDetails = new UserDetails();
            userDetails.getProviderIds().add(providerId);
            userDetails.setCreationDate(new Date());
            userDetails = userRepository.save(userDetails);
        }
        return userDetails;
    }
}
