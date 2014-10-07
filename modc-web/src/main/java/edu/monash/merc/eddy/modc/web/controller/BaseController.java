package edu.monash.merc.eddy.modc.web.controller;

import edu.monash.merc.eddy.modc.common.config.SystemPropertyConts;
import edu.monash.merc.eddy.modc.common.config.SystemPropertySettings;
import edu.monash.merc.eddy.modc.web.validation.ActionSupport;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by simonyu on 21/08/2014.
 */
public class BaseController {

    protected Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    protected SystemPropertySettings systemPropertySettings;

    private ActionSupport actionSupport;

    public void setSystemPropertySettings(SystemPropertySettings systemPropertySettings) {
        this.systemPropertySettings = systemPropertySettings;
    }

    @PostConstruct
    public void init() {
        String applicationName = this.systemPropertySettings.getPropValue(SystemPropertyConts.APPLICATION_NAME);
        System.out.println("======== application name : " + applicationName);
    }

    public void messageSupport(HttpServletRequest request, Model model) {
        actionSupport = ActionSupport.actionSupport(request, model);
    }

    public void addActionError(String code) {
        actionSupport.addActionError(code);
    }

    public void addActionError(String code, String[] errorMessages) {
        actionSupport.addActionError(code, errorMessages);
    }

    public void addActionError(String code, String[] errorMessages, String defaultErrorMessage) {
        actionSupport.addActionError(code, errorMessages, defaultErrorMessage);
    }

    public void addActionMessage(String code) {
        actionSupport.addActionMessage(code);
    }

    public void addActionMessage(String code, String[] actionMessages) {
        actionSupport.addActionMessage(code, actionMessages);
    }

    public void addActionMessage(String code, String[] actionMessages, String defaultActionMessage) {
        actionSupport.addActionMessage(code, actionMessages, defaultActionMessage);
    }

    public boolean hasActionMessages() {
        return actionSupport.hasActionMessages();
    }

    public void makeMessageAware() {
        actionSupport.makeMessageAware();
    }

    public boolean hasActionErrors() {
        return actionSupport.hasActionErrors();
    }

    public void makeErrorAware() {
        actionSupport.makeErrorAware();
    }
}
