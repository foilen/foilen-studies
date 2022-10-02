package com.foilen.studies.managers;

import com.foilen.studies.data.user.UserDetails;
import org.springframework.security.core.Authentication;

public interface UserManager {
    UserDetails getOrCreateUser(Authentication authentication);
}
