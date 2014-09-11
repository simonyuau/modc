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

package edu.monash.merc.eddy.modc.ws.endpoint;

import edu.monash.merc.eddy.modc.doi.DoiResponse;
import edu.monash.merc.eddy.modc.doi.HttpDOIService;
import edu.monash.merc.eddy.modc.ws.exception.DoiValidateException;
import edu.monash.merc.eddy.modc.ws.model.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.Date;

/**
 * Created by simonyu on 1/08/2014.
 */
@Endpoint
public class DOIServiceEndpoint {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private final String SERVICE_NS = "http://merc.monash.edu/ws/schema/doi";

    private final ObjectFactory JAXB_OBJECT_FACTORY = new ObjectFactory();

    @Autowired
    @Qualifier("doiService")
    private HttpDOIService doiService;

    @PayloadRoot(localPart = "MintDoiRequest", namespace = SERVICE_NS)
    @ResponsePayload
    public MintDoiResponse mintDoi(@RequestPayload MintDoiRequest request) {
        String serviceId = request.getServiceId();
        if (StringUtils.isBlank(serviceId)) {
            throw new DoiValidateException("serviceId must be provided");
        }
        System.out.println("==== publication year : " + request.getResource().getPublicationYear());
        //TODO Call ANDS DOI Service


        String title = request.getResource().getTitle().get(0).getValue();
        String creatorName = request.getResource().getCreator().get(0).getCreatorName();
        String publisher = request.getResource().getPublisher();
        Date publicationYear = request.getResource().getPublicationYear();
        String url = request.getUrl();
        DoiResponse response = doiService.mintDoi(creatorName, title, publisher, publicationYear, url);
        //return MintDoiResponse
        return generateMintResponse(response, request.getServiceId());
    }

    @PayloadRoot(localPart = "UpdateDoiRequest", namespace = SERVICE_NS)
    @ResponsePayload
    public UpdateDoiResponse updateDoi(@RequestPayload UpdateDoiRequest request) {
        //TODO Call ANDS DOI Service

        String doi = request.getDoi();
        String title = request.getResource().getTitle().get(0).getValue();
        String creatorName = request.getResource().getCreator().get(0).getCreatorName();
        String publisher = request.getResource().getPublisher();
        Date publicationYear = request.getResource().getPublicationYear();
        String url = request.getUrl();
        DoiResponse response = doiService.updateDoi(doi, creatorName, title, publisher, publicationYear, url);
        return generateUpdateResponse(response, request.getServiceId());
    }

    private MintDoiResponse generateMintResponse(DoiResponse response, String serviceId) {
        MintDoiResponse mintDoiResponse = JAXB_OBJECT_FACTORY.createMintDoiResponse();
        mintDoiResponse.setResponsecode(DResponseCode.fromValue(response.getResponseCode()));
        mintDoiResponse.setType(DResponseType.fromValue(response.getType()));
        mintDoiResponse.setServiceId(serviceId);
        mintDoiResponse.setDoi(response.getDoi());
        mintDoiResponse.setMessage(response.getMessage());
        mintDoiResponse.setVerbosemessage(response.getVerboseMessage());
        return mintDoiResponse;
    }

    private UpdateDoiResponse generateUpdateResponse(DoiResponse response, String serviceId) {
        UpdateDoiResponse updateDoiResponse = JAXB_OBJECT_FACTORY.createUpdateDoiResponse();
        updateDoiResponse.setResponsecode(DResponseCode.fromValue(response.getResponseCode()));
        updateDoiResponse.setType(DResponseType.fromValue(response.getType()));
        updateDoiResponse.setServiceId(serviceId);
        updateDoiResponse.setDoi(response.getDoi());
        updateDoiResponse.setMessage(response.getMessage());
        updateDoiResponse.setVerbosemessage(response.getVerboseMessage());
        return updateDoiResponse;
    }
}
