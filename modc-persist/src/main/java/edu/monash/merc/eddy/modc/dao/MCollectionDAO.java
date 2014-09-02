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

import edu.monash.merc.eddy.modc.domain.MCollection;
import edu.monash.merc.eddy.modc.repository.MCollectionRepository;
import edu.monash.merc.eddy.modc.sql.page.Pager;
import edu.monash.merc.eddy.modc.support.QueryHelper;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.criterion.Order;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by simonyu on 1/09/2014.
 */
@Scope("prototype")
@Repository
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "freqRegion")
public class MCollectionDAO extends HibernateGenericDAO<MCollection> implements MCollectionRepository {

    @Override
    public MCollection getCollectionByRefKeyAndProject(String refKey, long projectId) {
        String hql = "FROM " + this.persistClass.getSimpleName() + " AS c WHERE c.refKey = :refKey AND c.project.id = :projectId";
        Map<String, Object> namedParams = QueryHelper.createNamedParam("refKey", refKey);
        namedParams = QueryHelper.addNamedParam(namedParams, "projectId", projectId);
        return this.find(hql, namedParams);
    }

    @Override
    public MCollection getCollectionByNameAndProject(String name, long projectId) {
        String hql = "FROM " + this.persistClass.getSimpleName() + " AS c WHERE c.name = :name AND c.project.id = :projectId";
        Map<String, Object> namedParams = QueryHelper.createNamedParam("name", name);
        namedParams = QueryHelper.addNamedParam(namedParams, "projectId", projectId);
        return this.find(hql, namedParams);
    }

    @Override
    public List<MCollection> listCollectionsByUser(long userId) {
        return this.listCollectionsByUser(userId, null);
    }

    @Override
    public List<MCollection> listCollectionsByUser(long userId, Order[] orderParams) {
        String hql = "FROM " + this.persistClass.getSimpleName() + " AS c WHERE c.project.owner.id = :userId";
        Map<String, Object> namedParam = QueryHelper.createNamedParam("userId", userId);
        QueryHelper.setOrderByParams(hql, "c", orderParams);
        return this.list(hql, namedParam);
    }

    @Override
    public Pager<MCollection> getCollectionsByUser(long userId, int startPageNo, int sizePerPage, Order[] orderParams) {
        String hql = "FROM " + this.persistClass.getSimpleName() + " AS c WHERE c.project.owner.id = :userId";
        Map<String, Object> namedParam = QueryHelper.createNamedParam("userId", userId);
        QueryHelper.setOrderByParams(hql, "c", orderParams);
        return this.find(hql, namedParam, startPageNo, sizePerPage);
    }

    @Override
    public List<MCollection> listCollectionsByProject(long projectId) {
        return this.listCollectionsByProject(projectId, null);
    }

    @Override
    public List<MCollection> listCollectionsByProject(long projectId, Order[] orderParams) {
        String hql = "FROM " + this.persistClass.getSimpleName() + " AS c WHERE c.project.id = :projectId";
        Map<String, Object> namedParam = QueryHelper.createNamedParam("projectId", projectId);
        QueryHelper.setOrderByParams(hql, "c", orderParams);
        return this.list(hql, namedParam);
    }

    @Override
    public Pager<MCollection> getCollectionsByProject(long projectId, int startPageNo, int sizePerPage, Order[] orderParams) {
        String hql = "FROM " + this.persistClass.getSimpleName() + " AS c WHERE c.project.id = :projectId";
        Map<String, Object> namedParam = QueryHelper.createNamedParam("projectId", projectId);
        QueryHelper.setOrderByParams(hql, "c", orderParams);
        return this.find(hql, namedParam, startPageNo, sizePerPage);
    }

    @Override
    public List<MCollection> listCollectionByParty(long partyId) {
        //query the collection_party table
        String hql = "SELECT c FROM " + this.persistClass.getSimpleName() + " AS c INNER JOIN c.parties AS p WHERE p.id = :partyId";
        Map<String, Object> namedParam = QueryHelper.createNamedParam("partyId", partyId);
        return this.list(hql, namedParam);
    }


}
