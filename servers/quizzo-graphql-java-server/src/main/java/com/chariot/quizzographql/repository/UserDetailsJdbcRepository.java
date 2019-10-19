package com.chariot.quizzographql.repository;

import com.chariot.quizzographql.models.Role;
import com.chariot.quizzographql.models.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class UserDetailsJdbcRepository implements UserDetailsRepository {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private NamedParameterJdbcTemplate jdbcTemplate;


    private final String SQL_FETCH_USER_BY_USERNAME = new StringBuilder()
            .append("select u.id as user_id, user_name, password, email, phone, r.id as role_id, r.role_name as role_name\n")
            .append(" from quizzo.user u\n")
            .append(" inner join quizzo.user_role ur on (ur.user_id = u.id)\n")
            .append(" inner join quizzo.role r on (ur.role_id = r.id)\n")
            .append(" where user_name = :userName\n")
            .append(" order by u.id")
            .toString();

    @Override
    public User getUser(String userName) {
        SqlParameterSource params = new MapSqlParameterSource().addValue("userName", userName);
        return this.jdbcTemplate.query(SQL_FETCH_USER_BY_USERNAME, params, new ResultSetExtractor<User>() {


            @Override
            public User extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("user_id"));
                    user.setUserName(rs.getString("user_name"));
                    user.setPassword(rs.getString("password"));

                    do {
                        final String roleName = rs.getString("role_name");
                        final Integer roleId = rs.getInt("role_id");
                        if (!rs.wasNull()) {
                            switch(roleName) {
                                case "ROLE_ADMIN":
                                    user.addRole(Role.ROLE_ADMIN);
                                    break;
                                case "ROLE_PLAYER":
                                    user.addRole(Role.ROLE_PLAYER);
                                    break;
                                case "ROLE_MODERATOR":
                                    user.addRole(Role.ROLE_MODERATOR);
                                    break;
                                default:
                                    System.out.println("Role invalid " + roleName);
                            }
                        }
                    } while (rs.next());

                    if (user.getRoles().size() == 1 && user.getRoles().get(0).name().equals("ROLE_PLAYER")) {

                    }
                    return user;
                } else {
                    return null;
                }
            }
        });
    }
}
