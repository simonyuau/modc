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

import edu.monash.merc.eddy.modc.dao.ServiceAuthIPDAO;
import edu.monash.merc.eddy.modc.domain.ServiceAuthIP;
import edu.monash.merc.eddy.modc.service.ServiceAuthIPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Monash University eResearch Center
 * <p/>
 * Created by simonyu - xiaoming.yu@monash.edu
 * Date: 3/11/14
 */
@Service
@Transactional
public class ServiceAuthIPServiceImpl implements ServiceAuthIPService {

    @Autowired
    private ServiceAuthIPDAO serviceAuthIPDao;

    public void setServiceAuthIPDao(ServiceAuthIPDAO serviceAuthIPDao) {
        this.serviceAuthIPDao = serviceAuthIPDao;
    }

    @Override
    public void saveServiceAuthIP(ServiceAuthIP serviceAuthIP) {
        this.serviceAuthIPDao.add(serviceAuthIP);
    }

    @Override
    public ServiceAuthIP getServiceAuthIPById(long id) {
        return this.serviceAuthIPDao.get(id);
    }

    @Override
    public void updateServiceAuthIP(ServiceAuthIP serviceAuthIP) {
        this.serviceAuthIPDao.update(serviceAuthIP);
    }

    @Override
    public void deleteServiceAuthIP(ServiceAuthIP serviceAuthIP) {
        this.serviceAuthIPDao.remove(serviceAuthIP);
    }

    @Override
    public void deleteServiceAuthIPById(long id) {
        this.serviceAuthIPDao.delete(id);
    }

    @Override
    public List<ServiceAuthIP> listAuthIPsByServiceAppId(long serviceAppId) {
        return this.serviceAuthIPDao.listAuthIPsByServiceAppId(serviceAppId);
    }
}
