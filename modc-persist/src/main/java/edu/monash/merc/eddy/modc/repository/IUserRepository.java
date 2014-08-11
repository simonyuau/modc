package edu.monash.merc.eddy.modc.repository;

import edu.monash.merc.eddy.modc.domain.User;

/**
 * Created by simonyu on 8/08/2014.
 */
public interface IUserRepository {

    User getUserByEmail(String email);

    User getUserByUniqueId(String uniqueId);

    boolean checkUserExistedName(String displayName);

    boolean checkUserExistedUniqueId(String uniqueId);

    boolean checkExistedEmail(String email);

    User checkUserLogin(String uniqueId, String password);
}
