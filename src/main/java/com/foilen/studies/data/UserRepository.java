package com.foilen.studies.data;

import com.foilen.studies.data.user.UserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserDetails, String> {

    UserDetails findByProviderIds(String providerId);

}
