package edu.monash.merc.eddy.modc.web.controller;

import edu.monash.merc.eddy.modc.common.config.SystemPropertyConts;
import edu.monash.merc.eddy.modc.common.config.SystemPropertySettings;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Created by simonyu on 4/08/2014.
 */
public class MDBaseController {

    protected Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    protected SystemPropertySettings systemPropertySettings;

    public void setSystemPropertySettings(SystemPropertySettings systemPropertySettings) {
        this.systemPropertySettings = systemPropertySettings;
    }

    @PostConstruct
    public void init() {

        String applicationName = this.systemPropertySettings.getPropValue(SystemPropertyConts.APPLICATION_NAME);
        System.out.println("======== application name : " + applicationName);
    }
}
