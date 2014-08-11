package edu.monash.merc.eddy.modc.service.impl;

import edu.monash.merc.eddy.modc.dao.UserDAO;
import edu.monash.merc.eddy.modc.domain.User;
import edu.monash.merc.eddy.modc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by simonyu on 8/08/2014.
 */
@Scope("prototype")
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDao;

    public void setUserDao(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Override
    public void saveUser(User user) {
        this.userDao.add(user);
    }

    @Override
    public User getUserById(long id) {
        return this.userDao.get(id);
    }

    @Override
    public void updateUser(User user) {
        this.userDao.update(user);
    }

    @Override
    public void deleteUser(User user) {
        this.userDao.remove(user);
    }

    @Override
    public void deleteUser(long userId) {
        this.userDao.delete(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return this.userDao.getUserByEmail(email);
    }

    @Override
    public User getUserByUniqueId(String uniqueId) {
        return this.userDao.getUserByUniqueId(uniqueId);
    }

    @Override
    public boolean checkUserExistedName(String displayName) {
        return this.userDao.checkUserExistedName(displayName);
    }

    @Override
    public boolean checkUserExistedUniqueId(String uniqueId) {
        return this.userDao.checkUserExistedUniqueId(uniqueId);
    }

    @Override
    public boolean checkExistedEmail(String email) {
        return this.userDao.checkExistedEmail(email);
    }

    @Override
    public User login(String uniqueId, String password) {
        return this.userDao.checkUserLogin(uniqueId, password);
    }
}
