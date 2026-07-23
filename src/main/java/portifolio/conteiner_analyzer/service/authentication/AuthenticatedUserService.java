package portifolio.conteiner_analyzer.service.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import portifolio.conteiner_analyzer.entities.User;
import portifolio.conteiner_analyzer.exception.InvalidAuthenticationException;

@Service
public class AuthenticatedUserService {

    public User getAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null ||
        !authentication.isAuthenticated() ||
        !(authentication.getPrincipal() instanceof User user)) {

            throw  new InvalidAuthenticationException(
                    "Authenticated user not found"
            );

        }
        return user;
    }
}
