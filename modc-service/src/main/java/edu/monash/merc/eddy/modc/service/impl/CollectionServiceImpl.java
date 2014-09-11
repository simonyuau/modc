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

package edu.monash.merc.eddy.modc.service.impl;

import edu.monash.merc.eddy.modc.dao.MCollectionDAO;
import edu.monash.merc.eddy.modc.domain.MCollection;
import edu.monash.merc.eddy.modc.service.CollectionService;
import edu.monash.merc.eddy.modc.sql.page.Pager;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Monash University eResearch Center
 * <p/>
 * Created by simonyu - xiaoming.yu@monash.edu
 * Date: 5/09/2014
 */
@Service
@Transactional
public class CollectionServiceImpl implements CollectionService {

    @Autowired
    private MCollectionDAO collectionDao;

    public void setCollectionDao(MCollectionDAO collectionDao) {
        this.collectionDao = collectionDao;
    }

    @Override
    public void saveCollection(MCollection collection) {
        this.collectionDao.add(collection);
    }

    @Override
    public MCollection getCollectionById(long id) {
        return this.collectionDao.get(id);
    }

    @Override
    public void updateCollection(MCollection collection) {
        this.collectionDao.update(collection);
    }

    @Override
    public void deleteCollection(MCollection collection) {
        this.collectionDao.remove(collection);
    }

    @Override
    public void deleteCollection(long collectionId) {
        this.collectionDao.delete(collectionId);
    }

    @Override
    public MCollection getCollectionByRefKeyAndProject(String refKey, long projectId) {
        return this.collectionDao.getCollectionByRefKeyAndProject(refKey, projectId);
    }

    @Override
    public MCollection getCollectionByNameAndProject(String name, long projectId) {
        return this.collectionDao.getCollectionByNameAndProject(name, projectId);
    }

    @Override
    public List<MCollection> listCollectionsByUser(long userId) {
        return this.collectionDao.listCollectionsByUser(userId);
    }

    @Override
    public List<MCollection> listCollectionsByUser(long userId, Order[] orderParams) {
        return this.collectionDao.listCollectionsByUser(userId, orderParams);
    }

    @Override
    public Pager<MCollection> getCollectionsByUser(long userId, int startPageNo, int sizePerPage, Order[] orderParams) {
        return this.collectionDao.getCollectionsByUser(userId, startPageNo, sizePerPage, orderParams);
    }

    @Override
    public List<MCollection> listCollectionsByProject(long projectId) {
        return this.collectionDao.listCollectionsByProject(projectId);
    }

    @Override
    public List<MCollection> listCollectionsByProject(long projectId, Order[] orderParams) {
        return this.collectionDao.listCollectionsByProject(projectId, orderParams);
    }

    @Override
    public Pager<MCollection> getCollectionsByProject(long projectId, int startPageNo, int sizePerPage, Order[] orderParams) {
        return this.collectionDao.getCollectionsByProject(projectId, startPageNo, sizePerPage, orderParams);
    }

    @Override
    public List<MCollection> listCollectionByParty(long partyId) {
        return this.collectionDao.listCollectionByParty(partyId);
    }

    @Override
    public Pager<MCollection> getCollectionsByParty(long partyId, int startPageNo, int sizePerPage, Order[] orderParams) {
        return this.collectionDao.getCollectionsByParty(partyId, startPageNo, sizePerPage, orderParams);
    }
}
