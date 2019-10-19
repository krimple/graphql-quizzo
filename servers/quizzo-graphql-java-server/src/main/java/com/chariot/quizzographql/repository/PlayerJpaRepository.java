package com.chariot.quizzographql.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class PlayerJpaRepository implements PlayerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public String addPlayer(String nickName, String email, String phone) {
        // TODO - cache this key, maybe?
        // TODO - critical - generate join code as password for player
        // get an entity for the player role
        Query query = entityManager.createQuery("select r from RoleEntity r where r.roleName = :roleName")
                .setParameter("roleName", "ROLE_PLAYER");
        RoleEntity playerRole = (RoleEntity) query.getSingleResult();

        Query numUsersWithEmail = entityManager
                .createQuery("select u from UserEntity u where u.email = :email")
                .setParameter("email", email);

        if (numUsersWithEmail.getResultList().size() > 0) {
            // TODO - better error?
            throw new RuntimeException("Player already registered. Sorry.");
        }
        // ok, create a new one
        UserEntity playerUser = new UserEntity(nickName, "abc123");
        playerUser.setEmail(email);
        playerUser.setPhone(phone);
        playerUser.addRole(playerRole);
        entityManager.persist(playerUser);
        entityManager.flush();
        return "abc123";
    }
}
