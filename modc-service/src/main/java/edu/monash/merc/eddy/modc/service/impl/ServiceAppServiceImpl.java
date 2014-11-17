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

import edu.monash.merc.eddy.modc.dao.ServiceAppDAO;
import edu.monash.merc.eddy.modc.domain.ServiceApp;
import edu.monash.merc.eddy.modc.domain.ServiceAuthIP;
import edu.monash.merc.eddy.modc.service.ServiceAppService;
import edu.monash.merc.eddy.modc.service.ServiceAuthIPService;
import edu.monash.merc.eddy.modc.sql.page.Pager;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Monash University eResearch Center
 * <p/>
 * Created by simonyu - xiaoming.yu@monash.edu
 * Date: 31/10/14
 */
@Service
@Transactional
public class ServiceAppServiceImpl implements ServiceAppService {

    @Autowired
    private ServiceAppDAO serviceAppDao;

    @Autowired
    private ServiceAuthIPService serviceAuthIPService;

    public void setServiceAppDao(ServiceAppDAO serviceAppDao) {
        this.serviceAppDao = serviceAppDao;
    }

    public void setServiceAuthIPService(ServiceAuthIPService serviceAuthIPService) {
        this.serviceAuthIPService = serviceAuthIPService;
    }

    @Override
    public void saveServiceApp(ServiceApp serviceApp) {
        this.serviceAppDao.add(serviceApp);
    }

    @Override
    public ServiceApp getServiceAppById(long id) {
        return this.serviceAppDao.get(id);
    }

    @Override
    public void updateServiceApp(ServiceApp serviceApp) {
        this.serviceAppDao.update(serviceApp);
    }

    @Override
    public void updateServiceApp(ServiceApp serviceApp, List<ServiceAuthIP> authIPs) {
        this.updateServiceApp(serviceApp);
        List<ServiceAuthIP> oldAuthIps = this.serviceAuthIPService.listAuthIPsByServiceAppId(serviceApp.getId());
        updateAuthIps(serviceApp, oldAuthIps, authIPs);
    }

    private void updateAuthIps(ServiceApp serviceApp, List<ServiceAuthIP> oldAuthIps, List<ServiceAuthIP> updatedAuthIps) {

        if (updatedAuthIps != null && updatedAuthIps.size() > 0) {
            for (ServiceAuthIP authIP : updatedAuthIps) {
                long id = authIP.getId();
                if (id <= 0) {
                    authIP.setServiceApp(serviceApp);
                    this.serviceAuthIPService.saveServiceAuthIP(authIP);
                } else {
                    if (oldAuthIps.contains(authIP)) {
                        oldAuthIps.remove(authIP);
                    }
                }
            }
        }

        if (oldAuthIps != null && oldAuthIps.size() > 0) {
            for (ServiceAuthIP deleteIp : oldAuthIps) {
                this.serviceAuthIPService.deleteServiceAuthIP(deleteIp);
            }
        }

    }

    @Override
    public void deleteServiceApp(ServiceApp serviceApp) {
        this.serviceAppDao.remove(serviceApp);
    }

    @Override
    public void deleteServiceAppById(long serviceAppId) {
        this.serviceAppDao.delete(serviceAppId);
    }

    @Override
    public ServiceApp getServiceAppByUniqueId(String uniqueId) {
        return this.serviceAppDao.getServiceAppByUniqueId(uniqueId);
    }

    @Override
    public ServiceApp getServiceAppByName(String serviceAppName) {
        return this.serviceAppDao.getServiceAppByName(serviceAppName);
    }

    @Override
    public ServiceApp getServiceAppByUniqueIdAndIp(String uniqueId, String authIp) {
        return this.serviceAppDao.getServiceAppByUniqueIdAndIp(uniqueId, authIp);
    }

    @Override
    public List<ServiceApp> listServiceApps(String serviceType, Order[] orderParams) {
        return this.serviceAppDao.listServiceApps(serviceType, orderParams);
    }

    @Override
    public Pager<ServiceApp> getPagedServiceApps(String serviceType, int startPageNo, int sizePerPage, Order[] orderParams) {
        return this.serviceAppDao.getPagedServiceApps(serviceType, startPageNo, sizePerPage, orderParams);
    }
}
