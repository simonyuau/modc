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

package edu.monash.merc.eddy.modc.doi;

import edu.monash.merc.eddy.modc.common.exception.MWSException;
import edu.monash.merc.eddy.modc.common.util.MDUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Monash University eResearch Center
 * <p/>
 * Created by simonyu - xiaoming.yu@monash.edu
 * Date: 11/09/2014
 */
@Component
public class HttpDOIService {

    private static Logger logger = Logger.getLogger(HttpDOIService.class.getName());

    @Autowired
    private FreeMarkerDoiTempLoader freeMarkerDoiTempLoader;

    @Autowired
    private DOIServiceHelper doiServiceHelper;

    public void setFreeMarkerDoiTempLoader(FreeMarkerDoiTempLoader freeMarkerDoiTempLoader) {
        this.freeMarkerDoiTempLoader = freeMarkerDoiTempLoader;
    }

    public DoiResponse mintDoi(String creatorName, String title, String publisher, Date publicationDate, String url) {
        CloseableHttpClient client = null;
        try {
            String doiServicePoint = this.doiServiceHelper.getDoiServicePoint();
            String doiVersion = this.doiServiceHelper.getDoiVersion();
            String doiMintSuffix = this.doiServiceHelper.getDoiMintSuffix();
            String appId = this.doiServiceHelper.getAppId();

            String mint_service_url = doiServicePoint + "/" + doiVersion + "/" + doiMintSuffix + "/?app_id=" + appId + "&url=" + url;

            System.out.println("==== request mint doi url " + mint_service_url);
            //create a client
            client = this.doiServiceHelper.createClient();
            HttpPost post = new HttpPost(mint_service_url);
            byte[] doiXmls = loadDoiXML(null, creatorName, title, publisher, publicationDate);

            ByteArrayEntity byteArrayEntity = new ByteArrayEntity(doiXmls);
            post.setEntity(byteArrayEntity);

            HttpResponse httpResponse = client.execute(post);

            System.out.println("===> ANDS DOI minting response : " + httpResponse.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            System.out.println("================> response : " + result.toString());
            return DoiResponseParser.parseDOIXML(result.toString());
        } catch (Exception ex) {
            throw new MWSException(ex);
        } finally {
            //close the client if client not null;
            if (client != null) {
                this.doiServiceHelper.close(client);
            }
        }
    }

    public DoiResponse updateDoi(String doi, String creatorName, String title, String publisher, Date publicationDate, String url) {
        CloseableHttpClient client = null;
        try {
            String doiServicePoint = this.doiServiceHelper.getDoiServicePoint();
            String doiVersion = this.doiServiceHelper.getDoiVersion();
            String doiUpdateSuffix = this.doiServiceHelper.getDoiUpdateSuffix();
            String appId = this.doiServiceHelper.getAppId();

            String update_service_url = doiServicePoint + "/" + doiVersion + "/" + doiUpdateSuffix + "/?app_id=" + appId + "&url=" + url + "&doi=" + doi;

            System.out.println("==== post updating doi url " + update_service_url);
            //create a client
            client = this.doiServiceHelper.createClient();
            HttpPost post = new HttpPost(update_service_url);
            byte[] doiBytes = loadDoiXML(doi, creatorName, title, publisher, publicationDate);

            ByteArrayEntity byteArrayEntity = new ByteArrayEntity(doiBytes);
            post.setEntity(byteArrayEntity);

            HttpResponse httpResponse = client.execute(post);

            System.out.println(" ===> ANDS DOI updating response : " + httpResponse.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            System.out.println("================> response : " + result.toString());
            return DoiResponseParser.parseDOIXML(result.toString());
        } catch (Exception ex) {
            throw new MWSException(ex);
        } finally {
            //close the client if client not null;
            if (client != null) {
                this.doiServiceHelper.close(client);
            }
        }
    }

    private byte[] loadDoiXML(String doi, String creatorName, String title, String publisher, Date publicationDate) {
        Map<String, Object> doiTemplateValues = new HashMap<>();
        doiTemplateValues.put("doi", doi);
        doiTemplateValues.put("creatorName", creatorName);
        doiTemplateValues.put("title", title);
        doiTemplateValues.put("publisher", publisher);
        doiTemplateValues.put("publicationYear", MDUtils.yyyyDateFormat(publicationDate));
        return this.freeMarkerDoiTempLoader.loadDoiXML(doiTemplateValues, this.doiServiceHelper.getDoiTemplate());
    }


}
