package test.vn.dao;

import test.vn.entities.User;

public interface IUserDAO {

    void insert(User user);

    void update(User user);

    User findByEmail(String email);
}
