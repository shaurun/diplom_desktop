package fx.dao;

import fx.model.User;

public interface UserDao {
    User findByUsername(String username);
}
