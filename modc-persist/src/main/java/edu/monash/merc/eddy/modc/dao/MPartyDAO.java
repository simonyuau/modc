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

import edu.monash.merc.eddy.modc.domain.MParty;
import edu.monash.merc.eddy.modc.repository.MPartyRepository;
import edu.monash.merc.eddy.modc.support.QueryHelper;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
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
public class MPartyDAO extends HibernateGenericDAO<MParty> implements MPartyRepository {

    @Override
    public MParty getPartyByRefKey(String refKey) {
        String hql = "FROM " + this.persistClass.getSimpleName() + " AS p WHERE p.refKey = :refKey";
        Map<String, Object> namedParam = QueryHelper.createNamedParam("refKey", refKey);
        return this.find(hql, namedParam);
    }

    @Override
    public MParty getPartyByIdentifier(String identifier) {
        String hql = "FROM " + this.persistClass.getSimpleName() + " AS p WHERE p.identifier = :identifier";
        Map<String, Object> namedParam = QueryHelper.createNamedParam("identifier", identifier);
        return this.find(hql, namedParam);
    }

    @Override
    public MParty getPartyByEmail(String email) {
        String hql = "FROM " + this.persistClass.getSimpleName() + " AS p WHERE lower(p.email) = :email";
        Map<String, Object> namedParam = QueryHelper.createNamedParam("email", StringUtils.lowerCase(email));
        return this.find(hql, namedParam);
    }

    @Override
    public MParty getPartyByName(String name) {
        String hql = "FROM " + this.persistClass.getSimpleName() + " AS p WHERE lower(p.name) = :pGroupName";
        Map<String, Object> namedParam = QueryHelper.createNamedParam("pGroupName", StringUtils.lowerCase(name));
        return this.find(hql, namedParam);
    }

    @Override
    public List<MParty> listPartiesByUserName(String firstName, String lastName) {
        String hql = "FROM " + this.persistClass.getSimpleName() + " AS p WHERE lower(p.firstName) = :firstName AND lower(p.lastName) = :lastName";
        Map<String, Object> namedParams = QueryHelper.createNamedParam("firstName", StringUtils.lowerCase(firstName));
        namedParams = QueryHelper.addNamedParam(namedParams, "lastName", StringUtils.lowerCase(lastName));
        return this.list(hql, namedParams);
    }

    @Override
    public List<MParty> listPartiesByCollection(long collectionId) {
        String hql = "SELECT p FROM " + this.persistClass.getSimpleName() + " AS p INNER JOIN p.collections AS c WHERE c.id = :collectionId";
        Map<String, Object> namedParam = QueryHelper.createNamedParam("collectionId", collectionId);
        return this.list(hql, namedParam);
    }
}
