package edu.monash.merc.eddy.modc.web.controller;

import edu.monash.merc.eddy.modc.common.config.SystemPropertyConts;
import edu.monash.merc.eddy.modc.common.config.SystemPropertySettings;
import edu.monash.merc.eddy.modc.domain.Avatar;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * Created by simonyu on 21/08/2014.
 */
public class MBaseController {
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
