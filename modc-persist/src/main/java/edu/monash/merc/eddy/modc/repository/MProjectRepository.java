package edu.monash.merc.eddy.modc.repository;

import edu.monash.merc.eddy.modc.domain.MProject;
import edu.monash.merc.eddy.modc.sql.page.Pager;
import org.hibernate.criterion.Order;

import java.util.List;

/**
 * Created by simonyu on 27/08/2014.
 */
public interface MProjectRepository {

    MProject getByUniqueId(String uniqueid);

    MProject getByProjectName(String projectName);

    List<MProject> getProjectsByUser(long userId, Order [] orderParams);

    Pager<MProject> getPagedProjectsByUser(long userId, int startPageNo, int sizePerPage, Order[] orderParams);

}


