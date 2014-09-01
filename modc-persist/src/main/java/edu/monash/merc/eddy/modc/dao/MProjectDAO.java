/*
 * Copyright (c) 2014, Monash e-Research Centre
 *  (Monash University, Australia)
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *  	* Redistributions of source code must retain the above copyright
 *  	  notice, this list of conditions and the following disclaimer.
 *  	* Redistributions in binary form must reproduce the above copyright
 *  	  notice, this list of conditions and the following disclaimer in the
 *  	  documentation and/or other materials provided with the distribution.
 *  	* Neither the name of the Monash University nor the names of its
 *  	  contributors may be used to endorse or promote products derived from
 *  	  this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 *  EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package edu.monash.merc.eddy.modc.dao;

import edu.monash.merc.eddy.modc.domain.MProject;
import edu.monash.merc.eddy.modc.repository.MPartyRepository;
import edu.monash.merc.eddy.modc.repository.MProjectRepository;
import edu.monash.merc.eddy.modc.sql.page.Pager;
import edu.monash.merc.eddy.modc.support.QueryHelper;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.criterion.Order;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by simonyu on 27/08/2014.
 */
@Scope("prototype")
@Repository
public class MProjectDAO extends HibernateGenericDAO<MProject> implements MProjectRepository {

    @Override
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "fixedRegion")
    public MProject getByUniqueId(String uniqueId) {
        String hql = "FROM " + this.persistClass.getSimpleName() + " AS p WHERE p.uniqueId =:uniqueId";
        Map<String, Object> namedParams = QueryHelper.createNamedParam("uniqueId", uniqueId);
        return this.find(hql, namedParams);
    }

    @Override
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "fixedRegion")
    public MProject getByProjectName(String projectName) {
        String hql = "FROM " + this.persistClass.getSimpleName() + " AS p WHERE lower(p.name) =:name";
        Map<String, Object> namedParams = QueryHelper.createNamedParam("name", StringUtils.lowerCase(projectName));
        return this.find(hql, namedParams);
    }

    @Override
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "fixedRegion")
    public List<MProject> getProjectsByUser(long userId, Order[] orderParams) {
        String hql = "FROM " + this.persistClass.getSimpleName() + " AS p WHERE p.owner.id =:id";

        //create named params
        Map<String, Object> namedParams = QueryHelper.createNamedParam("id", userId);
        //set order by if any
        hql = QueryHelper.setOrderByParams(hql, "p", orderParams);
        return this.list(hql, namedParams);
    }

    @Override
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "fixedRegion")
    public Pager<MProject> getPagedProjectsByUser(long userId, int startPageNo, int sizePerPage, Order[] orderParams) {
        String hql = "FROM " + this.persistClass.getSimpleName() + " AS p WHERE p.owner.id= :id";
        //create named params
        Map<String, Object> namedParams = QueryHelper.createNamedParam("id", userId);
        //set order by if any
        hql = QueryHelper.setOrderByParams(hql, "p", orderParams);
        return this.find(hql, namedParams, startPageNo, sizePerPage);

    }
}
