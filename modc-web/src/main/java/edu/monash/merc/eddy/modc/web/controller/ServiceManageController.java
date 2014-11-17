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

package edu.monash.merc.eddy.modc.web.controller;

import edu.monash.merc.eddy.modc.common.util.MDUtils;
import edu.monash.merc.eddy.modc.domain.ServiceApp;
import edu.monash.merc.eddy.modc.domain.ServiceAuthIP;
import edu.monash.merc.eddy.modc.service.ServiceAppService;
import edu.monash.merc.eddy.modc.service.ServiceAuthIPService;
import edu.monash.merc.eddy.modc.sql.condition.SqlOrderBy;
import edu.monash.merc.eddy.modc.sql.page.Pager;
import edu.monash.merc.eddy.modc.web.conts.MConts;
import edu.monash.merc.eddy.modc.web.form.ServiceAppBean;
import edu.monash.merc.eddy.modc.web.validation.MDValidator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Monash University eResearch Center
 * <p/>
 * Created by simonyu - xiaoming.yu@monash.edu
 * Date: 3/11/14
 */
@Controller
@RequestMapping("/service")
public class ServiceManageController extends BaseController {

    @Autowired
    private ServiceAppService serviceAppService;

    @Autowired
    private ServiceAuthIPService serviceAuthIPService;

    public void setServiceAppService(ServiceAppService serviceAppService) {
        this.serviceAppService = serviceAppService;
    }

    public void setServiceAuthIPService(ServiceAuthIPService serviceAuthIPService) {
        this.serviceAuthIPService = serviceAuthIPService;
    }

    @RequestMapping("/ws_app_list")
    public String wsAppList(String serviceType, Integer pageNo, HttpServletRequest request, Model model) {

        if (pageNo == null || pageNo.intValue() == 0) {
            pageNo = MConts.DEFAULT_START_PAGE_NO;
        }

        if (StringUtils.isBlank(serviceType)) {
            serviceType = "none";
        }
        model.addAttribute("serviceType", serviceType);
        model.addAttribute("pageLink", "service/ws_app_list.htm");
        //set order by
        SqlOrderBy myOrderBy = new SqlOrderBy().asc("serviceType").desc("createdDate").asc("name");
        try {
            String stype = serviceType;
            if (stype.equals("none")) {
                stype = null;
            }
            Pager<ServiceApp> paginationObjs = this.serviceAppService.getPagedServiceApps(stype, pageNo, MConts.DEFAULT_SIZE_PER_PAGE, myOrderBy.orders());
            if (paginationObjs == null) {
                paginationObjs = new Pager<>();
            }
            //set the pagination users
            model.addAttribute("paginationObjs", paginationObjs);
        } catch (Exception ex) {
            logger.error(ex);
            actionSupport(request, model);
            addActionError("webservice.ws.app.list.error");
            makeErrorAware();
            return "service/ws_app_list_error";
        }
        return "service/ws_app_list";
    }

    @RequestMapping(value = "/new_ws_app", method = RequestMethod.GET)
    public String wsApp(Model model) {
        ServiceAppBean serviceAppBean = new ServiceAppBean();
        ServiceApp serviceApp = new ServiceApp();
        String appId = MDUtils.uuid();
        serviceApp.setUniqueId(appId);
        serviceAppBean.setServiceApp(serviceApp);

        model.addAttribute("serviceAppBean", serviceAppBean);

        return "service/new_ws_app";
    }

    @RequestMapping(value = "/new_ws_app", method = RequestMethod.POST)
    public String wsApp(@ModelAttribute("serviceAppBean") ServiceAppBean serviceAppBean, HttpServletRequest request, Model model) {
        try {
            //add action support
            actionSupport(request, model);
            //validate the request
            validateWSApp(serviceAppBean, true);

            if (hasActionErrors()) {
                logger.error("Validation failed in webservice app creation");
                makeErrorAware();
                return "service/new_ws_app";
            }

            ServiceApp serviceApp = serviceAppBean.getServiceApp();
            List<ServiceAuthIP> authIPs = serviceAppBean.getServiceAuthIPs();
            Date createdDate = GregorianCalendar.getInstance().getTime();

            serviceApp.setCreatedDate(createdDate);
            serviceApp.setLastModified(createdDate);
            serviceApp.setPath("default");
            authIPs = setServiceAppForAuthIp(serviceApp, authIPs);
            serviceApp.setServiceAuthIPs(authIPs);
            this.serviceAppService.saveServiceApp(serviceApp);
        } catch (Exception ex) {
            logger.error(ex);
            addActionError("webservice.add.app.error");
            makeErrorAware();
            return "service/new_ws_app";
        }
        return "service/ws_app";
    }

    private List<ServiceAuthIP> setServiceAppForAuthIp(ServiceApp serviceApp, List<ServiceAuthIP> serviceAuthIPs) {
        for (ServiceAuthIP authIP : serviceAuthIPs) {
            authIP.setServiceApp(serviceApp);
        }
        return serviceAuthIPs;
    }


    @RequestMapping(value = "/ws_app", method = RequestMethod.GET)
    public String wsApp(@RequestParam("id") long id, HttpServletRequest request, Model model) {
        //add action support
        actionSupport(request, model);
        ServiceAppBean serviceAppBean = new ServiceAppBean();
        model.addAttribute("serviceAppBean", serviceAppBean);
        try {
            ServiceApp serviceApp = this.serviceAppService.getServiceAppById(id);
            if (serviceApp == null) {
                addActionError("webservice.ws.app.not.found");
                makeErrorAware();
                return "service/ws_app_error";
            } else {
                serviceAppBean.setServiceApp(serviceApp);
            }
            List<ServiceAuthIP> serviceAuthIPs = this.serviceAuthIPService.listAuthIPsByServiceAppId(id);
            if (serviceAuthIPs == null) {
                serviceAuthIPs = new ArrayList<>();
            }
            serviceAppBean.setServiceAuthIPs(serviceAuthIPs);
        } catch (Exception ex) {
            logger.error(ex);
            addActionError("webservice.ws.app.details.error");
            makeErrorAware();
            return "service/ws_app_error";
        }
        return "service/ws_app";
    }

    @RequestMapping(value = "/update_ws_app", method = RequestMethod.GET)
    public String updateWsApp(@RequestParam("id") long id, HttpServletRequest request, Model model) {
        //add action support
        actionSupport(request, model);
        ServiceAppBean serviceAppBean = new ServiceAppBean();
        model.addAttribute("serviceAppBean", serviceAppBean);
        try {
            ServiceApp serviceApp = this.serviceAppService.getServiceAppById(id);
            if (serviceApp == null) {
                addActionError("webservice.ws.app.not.found");
                makeErrorAware();
                return "service/update_ws_app_error";
            } else {
                serviceAppBean.setServiceApp(serviceApp);
            }
            List<ServiceAuthIP> serviceAuthIPs = this.serviceAuthIPService.listAuthIPsByServiceAppId(id);
            if (serviceAuthIPs == null) {
                serviceAuthIPs = new ArrayList<>();
            }
            serviceAppBean.setServiceAuthIPs(serviceAuthIPs);
        } catch (Exception ex) {
            logger.error(ex);
            addActionError("webservice.ws.app.details.error");
            makeErrorAware();
            return "service/update_ws_app_error";
        }
        return "service/update_ws_app";
    }

    @RequestMapping(value = "/update_ws_app", method = RequestMethod.POST)
    public String updateWsApp(@ModelAttribute("serviceAppBean") ServiceAppBean serviceAppBean, HttpServletRequest request, Model model) {
        try {
            //add action support
            actionSupport(request, model);
            //validate the request
            validateWSApp(serviceAppBean, false);
            if (hasActionErrors()) {
                logger.error("Validation failed in updating webservice app");
                makeErrorAware();
                return "service/update_ws_app";
            }

            ServiceApp serviceApp = serviceAppBean.getServiceApp();
            ServiceApp foundServiceApp = this.serviceAppService.getServiceAppById(serviceApp.getId());
            if (foundServiceApp == null) {
                addActionError("webservice.ws.app.not.found");
                makeErrorAware();
                return "service/update_ws_app_error";
            }
            foundServiceApp.setDescription(serviceApp.getDescription());
            foundServiceApp.setName(serviceApp.getName());
            foundServiceApp.setServiceType(serviceApp.getServiceType());
            foundServiceApp.setLastModified(GregorianCalendar.getInstance().getTime());

            List<ServiceAuthIP> serviceAuthIPs = serviceAppBean.getServiceAuthIPs();
            this.serviceAppService.updateServiceApp(foundServiceApp, serviceAuthIPs);

            serviceAppBean.setServiceApp(foundServiceApp);
            //addActionMessage("webservice.ws.app.update.success", new String[]{serviceApp.getName()});
            //makeMessageAware();
            return "service/ws_app";
        } catch (Exception ex) {
            logger.error(ex);
            addActionError("webservice.ws.app.update.error");
            makeErrorAware();
            return "service/update_ws_app_error";
        }
    }

    @RequestMapping(value = "/delete_ws_app", method = RequestMethod.GET)
    public String deleteWsApp(@RequestParam("id") long id, HttpServletRequest request, Model model) {
        try {
            //add action support
            actionSupport(request, model);
            ServiceApp serviceApp = this.serviceAppService.getServiceAppById(id);
            if (serviceApp == null) {
                addActionError("webservice.ws.app.not.found");
                makeErrorAware();
                return "service/delete_ws_app_error";
            }
            this.serviceAppService.deleteServiceApp(serviceApp);
            addActionMessage("webservice.ws.app.delete.success", new String[]{serviceApp.getName()});
            makeMessageAware();
            return "service/delete_ws_app";
        } catch (Exception ex) {
            logger.error(ex);
            logger.error(ex);
            addActionError("webservice.ws.app.delete.error");
            makeErrorAware();
            return "service/delete_ws_app_error";
        }
    }

    private void validateWSApp(ServiceAppBean serviceAppBean, boolean isCreated) {

        if (serviceAppBean == null) {
            addActionError("webservice.ws.app.empty.param");
            return;
        }

        ServiceApp serviceApp = serviceAppBean.getServiceApp();

        if (serviceApp == null) {
            addActionError("webservice.ws.app.empty.param");
            return;
        }
        String name = serviceApp.getName();
        if (StringUtils.isBlank(name)) {
            addActionError("webservice.ws.app.name.empty");
        } else {
            ServiceApp foundServiceApp = this.serviceAppService.getServiceAppByName(name);
            if (foundServiceApp != null) {
                if (isCreated) {
                    addActionError("webservice.ws.app.name.already.existed");
                } else {
                    long currentServiceAppId = serviceApp.getId();
                    long foundServiceAppId = foundServiceApp.getId();
                    if (currentServiceAppId != foundServiceAppId) {
                        addActionError("webservice.ws.app.name.already.existed");
                    }
                }
            }
        }

        String serviceType = serviceApp.getServiceType();
        if (StringUtils.isBlank(serviceType) || StringUtils.equalsIgnoreCase("none", serviceType)) {
            addActionError("webservice.ws.app.service.type.empty");
        }

        String desc = serviceApp.getDescription();
        if (StringUtils.isBlank(desc)) {
            addActionError("webservice.ws.app.desc.empty");
        }

        List<ServiceAuthIP> serviceAuthIPs = serviceAppBean.getServiceAuthIPs();
        if (serviceAuthIPs == null || serviceAuthIPs.size() == 0) {
            addActionError("webservice.ws.app.ip.required");
        } else {
            if (findDuplicate(serviceAuthIPs)) {
                addActionError("webservice.ws.app.ip.duplicated");
            }
            if (findEmptyIP(serviceAuthIPs)) {
                addActionError("webservice.ws.app.ip.empty");
            }
            if (!validIps(serviceAuthIPs)) {
                addActionError("webservice.ws.app.ip.invalid");
            }
        }
    }


    private boolean findDuplicate(List<ServiceAuthIP> serviceAuthIPs) {
        List<String> uniqueList = new ArrayList<>();
        for (ServiceAuthIP saIp : serviceAuthIPs) {
            String ip = saIp.getIpAddress();
            if (StringUtils.isNotBlank(ip)) {
                if (!uniqueList.contains(ip)) {
                    uniqueList.add(ip);
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean findEmptyIP(List<ServiceAuthIP> serviceAuthIPs) {
        for (ServiceAuthIP saIp : serviceAuthIPs) {
            String ip = saIp.getIpAddress();
            if (StringUtils.isBlank(ip)) {
                return true;
            }
        }
        return false;
    }

    private boolean validIps(List<ServiceAuthIP> serviceAuthIPs) {
        for (ServiceAuthIP saIp : serviceAuthIPs) {
            String ip = saIp.getIpAddress();
            if (!MDValidator.validateIp(ip)) {
                return false;
            }
        }
        return true;
    }


}
