package org.mehmetcc.credit.commons.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Slf4j
@Service
public class UserClient {
    private final RestClient client;

    public UserClient(final RestClient.Builder builder) {
        this.client = builder.baseUrl("http://localhost:8081/api/v1/users").build();
    }

    public User getById(final Integer id) {
        var user = maybeGetById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.getIsActive()) {
            var exception = new UserNotActiveException("User is not active");
            log.error(exception.getMessage());
            throw exception;
        }

        return user;
    }

    public Optional<User> maybeGetById(final Integer id) {
        try {
            return Optional.ofNullable(client.get().uri("/{id}", id).retrieve().body(User.class));
        } catch (Throwable t) {
            log.error(t.getMessage());
            return Optional.empty();
        }
    }

    public Boolean isActive(final Integer id) {
        try {
            return client.get().uri("/{id}", id).retrieve().body(User.class).getIsActive();
        } catch (Throwable t) {
            log.error(t.getMessage());
            return false;
        }
    }
}
