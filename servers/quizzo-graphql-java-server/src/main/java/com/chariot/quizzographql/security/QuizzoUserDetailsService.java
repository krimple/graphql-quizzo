package com.chariot.quizzographql.security;

import com.chariot.quizzographql.models.Role;
import com.chariot.quizzographql.models.User;
import com.chariot.quizzographql.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizzoUserDetailsService implements UserDetailsService {

    private UserDetailsRepository repository;

    @Autowired
    public void setRepository(UserDetailsRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = this.repository.getUser(username);

        if (user == null) {
            throw new UsernameNotFoundException("Cannot find " + username + ".");
        }

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
}

