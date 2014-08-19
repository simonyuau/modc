package edu.monash.merc.eddy.modc.ws.endpoint;

import edu.monash.merc.eddy.modc.ws.model.*;
import org.apache.log4j.Logger;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

/**
 * Created by simonyu on 1/08/2014.
 */
@Endpoint
public class MDServiceEndpoint {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    private final String SERVICE_NS = "http://merc.monash.edu/ws/schema/mds";

    private final ObjectFactory JAXB_OBJECT_FACTORY = new ObjectFactory();

    @PayloadRoot(localPart = "WPublishRequest", namespace = SERVICE_NS)
    @ResponsePayload
    public WPublishResponse publishMd(@RequestPayload WPublishRequest publishRequest) {

        WPublishResponse response = JAXB_OBJECT_FACTORY.createWPublishResponse();
        WCollection collection = publishRequest.getCollection();
        logger.info("=== created date : " + collection.getCreatedDate());
        logger.info("=== end date : " + collection.getEndDate());
        logger.info("=== Received MDService request");
        response.setRefNumber("12346");
        List<WParty> parties = collection.getParty();

        for (WParty p : parties) {
            WGroup group = p.getGroup();
            WPerson person = p.getPerson();
            if (group != null) {
                logger.info("Party -- group : " + group.getName());
            }
            if (person != null) {
                logger.info("Party -- person : " + person.getFirstName() + " " + person.getLastName());
            }

        }
        return response;
    }
}
