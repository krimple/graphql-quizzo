package com.chariot.quizzographql.security;

import com.chariot.quizzographql.models.Role;
import com.chariot.quizzographql.models.User;
import com.chariot.quizzographql.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QuizzoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private PasswordEncoder encoder;

    @Autowired
    public QuizzoAuthenticationProvider(PasswordEncoder encoder, UserDetailsRepository userDetailsRepository) {
        this.encoder = encoder;
        this.userDetailsRepository = userDetailsRepository;
    }

    private UserDetailsRepository userDetailsRepository;

    @Override
    public boolean supports(Class<?> authentication) {
        logger.debug("supports...");
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        logger.debug("retrieving user");
        User user = userDetailsRepository.getUser(authentication.getName());
        List<String> roleNames = new ArrayList<>();

        for (Role role : user.getRoles()) {
            roleNames.add(role.name());
        }

        return org.springframework.security.core.userdetails.
                User
                .withUsername(user.getUserName())
                .password(user.getPassword())
                .authorities(roleNames.toArray(new String[0]))
                .accountLocked(false)
                .accountExpired(false)
                .disabled(false)
                .build();

    }
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // HACK? When we come back through with a JWT token, it contains the encoded password
        // so it will 100% match the password in the credentials of the user principal
        // so we do an equals check on that. IF that is false, we go on to check the encoder matches.
        if (!userDetails.getPassword().equals(authentication.getCredentials().toString()) &&
                !encoder.matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
           throw new BadCredentialsException("Passwords do not match");
        }
    }

//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        logger.debug("authenticate here...");
//        Object o = authentication.getPrincipal();
//        return super.authenticate(authentication);
//    }
}
