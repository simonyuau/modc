package edu.monash.merc.eddy.modc.ws.endpoint;

import edu.monash.merc.eddy.modc.ws.model.MDPublishRequest;
import edu.monash.merc.eddy.modc.ws.model.MDPublishResponse;
import edu.monash.merc.eddy.modc.ws.model.ObjectFactory;
import org.apache.log4j.Logger;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * Created by simonyu on 1/08/2014.
 */
@Endpoint
public class MDServiceEndpoint {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    private final String SERVICE_NS = "http://merc.monash.edu/ws/schema/mds";

    private final ObjectFactory JAXB_OBJECT_FACTORY = new ObjectFactory();

    @PayloadRoot(localPart = "MDPublishRequest", namespace = SERVICE_NS)
    @ResponsePayload
    public MDPublishResponse publishMd(@RequestPayload MDPublishRequest mdPublishRequest) {

        MDPublishResponse response = JAXB_OBJECT_FACTORY.createMDPublishResponse();
        logger.info("=== Received MDService request");
        response.setRefNumber("12346");
        return response;
    }
}
