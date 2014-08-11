package edu.monash.merc.eddy.modc.service;

import edu.monash.merc.eddy.modc.domain.User;

/**
 * Created by simonyu on 8/08/2014.
 */
public interface UserService {

    void saveUser(User user);

    User getUserById(long id);

    void updateUser(User user);

    void deleteUser(User user);

    void deleteUser(long userId);

    User getUserByEmail(String email);

    User getUserByUniqueId(String uniqueId);

    boolean checkUserExistedName(String displayName);

    boolean checkUserExistedUniqueId(String uniqueId);

    boolean checkExistedEmail(String email);

    User login(String uniqueId, String password);
}
