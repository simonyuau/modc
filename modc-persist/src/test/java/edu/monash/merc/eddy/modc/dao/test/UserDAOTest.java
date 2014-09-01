package edu.monash.merc.eddy.modc.dao.test;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import edu.monash.merc.eddy.modc.dao.UserDAO;
import edu.monash.merc.eddy.modc.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * Created by simonyu on 26/08/2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/test-dao.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@TransactionConfiguration(defaultRollback = false, transactionManager = "transactionManager")
@Transactional
public class UserDAOTest {

    @Autowired
    public UserDAO userDao;

    public void setUserDao(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Test
    @DatabaseSetup(value = "test-user.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "test-user.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetUserById() {
        User user = this.userDao.get(1);
        assertEquals("Phillip", user.getFirstName());
    }

    @Test
    @DatabaseSetup(value = "test-user.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "test-user.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetUserByUniqueId() {
        User user = this.userDao.getUserByUniqueId("phillip.webb@gmail.com");
        assertNotNull(user);
        assertEquals("Phillip", user.getFirstName());
    }

    @Test
    @DatabaseSetup(value = "test-user.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "test-user.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetUserByEmail() {
        User user = this.userDao.getUserByEmail("phillip.webb@gmail.com");
        assertNotNull(user);
        assertEquals("Phillip", user.getFirstName());
    }

    @Test
    @DatabaseSetup(value = "test-user.xml", type = DatabaseOperation.CLEAN_INSERT)
    //don't need database teardown to clean database, it's deleted by test case
    public void testDeleteUser() {
        this.userDao.delete(1);
        User user = this.userDao.get(1);
        assertNull(user);
    }

    @Test
    @DatabaseSetup(value = "test-user.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "test-user.xml", type = DatabaseOperation.DELETE_ALL)
    public void testUserLogin() {
        User user = this.userDao.checkUserLogin("phillip.webb@gmail.com", "12345");
        assertNotNull(user);
        assertEquals("phillip.webb@gmail.com", user.getUniqueId());
    }

    @Test
    @DatabaseSetup(value = "test-user.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "test-user.xml", type = DatabaseOperation.DELETE_ALL)
    public void checkUserName() {
        boolean nameExisted= this.userDao.checkExistedName("phillip webb");
       assertTrue(nameExisted);
    }




}
