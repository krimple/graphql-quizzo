package com.chariot.quizzographql.repository;

import com.chariot.quizzographql.models.User;

public interface UserDetailsRepository {
    User getUser(String userName);
}
