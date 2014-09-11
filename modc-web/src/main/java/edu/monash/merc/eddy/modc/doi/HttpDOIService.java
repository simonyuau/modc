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
import edu.monash.merc.eddy.modc.http.HttpConnectionAliveStrategy;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

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
public class HttpDOIService {

    private static int DEFAULT_MAX_TOTAL_CONNECTIONS = 10;

    private static int DEFAULT_MAX_PER_ROUTE = 5;

    private static int DEFAULT_KEEP_ALIVE = 10;

    private int maxTotalConnections;

    private int maxPerRouteConnections;

    private int keepAliveInSeconds;

    @Autowired
    private FreeMarkerDoiTempLoader doiTempLoader;

    private static String DOI_SERVICE_POINT = "https://services.ands.org.au/doi/";

    private static String APP_ID = "4fb08353943adf1f733c528c14293db711a5b03d";

    private static String DOI_MINT_SUFFIX = "1.1/mint.xml/?app_id=";

    private static String DOI_UPDATE_SUFFIX = "1.1/update.xml/?app_id=";

    private static HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

    private static Logger logger = Logger.getLogger(HttpDOIService.class.getName());

    public void init() {

        logger.info("===== start to init HttpDOIClient");

        if (maxTotalConnections <= 0) {
            maxTotalConnections = DEFAULT_MAX_TOTAL_CONNECTIONS;
        }
        httpClientBuilder.setMaxConnTotal(maxTotalConnections);
        if (maxPerRouteConnections <= 0) {
            maxPerRouteConnections = DEFAULT_MAX_PER_ROUTE;
        }
        httpClientBuilder.setMaxConnPerRoute(maxPerRouteConnections);

        logger.info("===== maxTotalConnections : " + maxTotalConnections);
        logger.info("===== maxPerRouteConnections : " + maxPerRouteConnections);
        HttpConnectionAliveStrategy connectionAliveStrategy = new HttpConnectionAliveStrategy();
        //set keep alive in seconds
        if(keepAliveInSeconds <= 0){
            keepAliveInSeconds = DEFAULT_KEEP_ALIVE;
        }

        logger.info("===== keepAliveInSeconds : " + keepAliveInSeconds);
        connectionAliveStrategy.setKeepAliveInSeconds(keepAliveInSeconds);

        httpClientBuilder.setKeepAliveStrategy(connectionAliveStrategy);

        logger.info("===== Finished to init HttpDOIClient");

    }

    public CloseableHttpClient build() {
        return httpClientBuilder.build();
    }

    public void close(CloseableHttpClient httpClient) {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (Exception ex) {
                logger.info("Failed to close the http client.");
            }
        }
    }

    public DoiResponse mintDoi(String creatorName, String title, String publisher, Date publicationDate, String url) {
        try {
            String mint_service_url = DOI_SERVICE_POINT + DOI_MINT_SUFFIX + APP_ID;
            mint_service_url = mint_service_url + "&url=" + url;

            CloseableHttpClient client = httpClientBuilder.build();
            HttpPost post = new HttpPost(mint_service_url);
            byte[] doiXmls = loadDoiXML(null, creatorName, title, publisher, publicationDate);

            ByteArrayEntity byteArrayEntity = new ByteArrayEntity(doiXmls);
            post.setEntity(byteArrayEntity);

            HttpResponse httpResponse = client.execute(post);

            System.out.println("Response Code : " + httpResponse.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            System.out.println("====== result : " + result.toString());
            return DoiResponseParser.parseDOIXML(result.toString());
        } catch (Exception ex) {
            throw new MWSException(ex);
        }
    }

    public DoiResponse updateDoi(String doi, String creatorName, String title, String publisher, Date publicationDate, String url) {
        try {
            String mint_service_url = DOI_SERVICE_POINT + DOI_UPDATE_SUFFIX + APP_ID;
            mint_service_url = mint_service_url + "&url=" + url + "&doi=" + doi;
            CloseableHttpClient client = httpClientBuilder.build();
            HttpPost post = new HttpPost(mint_service_url);
            byte[] doiBytes = loadDoiXML(doi, creatorName, title, publisher, publicationDate);

            ByteArrayEntity byteArrayEntity = new ByteArrayEntity(doiBytes);
            post.setEntity(byteArrayEntity);

            HttpResponse httpResponse = client.execute(post);

            System.out.println("Response Code : " + httpResponse.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            System.out.println("====== result : " + result.toString());
            return DoiResponseParser.parseDOIXML(result.toString());
        } catch (Exception ex) {
            throw new MWSException(ex);
        }
    }


    private byte[] loadDoiXML(String doi, String creatorName, String title, String publisher, Date publicationDate) {

        Map<String, Object> doiTemplateValues = new HashMap<>();
        if (StringUtils.isNotBlank(doi)) {
            doiTemplateValues.put("doi", doi);
        }
        doiTemplateValues.put("creatorName", creatorName);
        doiTemplateValues.put("title", title);
        doiTemplateValues.put("publisher", publisher);
        doiTemplateValues.put("publicationYear", MDUtils.yyyyDateFormat(publicationDate));
        return doiTempLoader.loadDoiXML(doiTemplateValues, "doi.ftl");
    }

    public int getMaxTotalConnections() {
        return maxTotalConnections;
    }

    public void setMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }

    public int getMaxPerRouteConnections() {
        return maxPerRouteConnections;
    }

    public void setMaxPerRouteConnections(int maxPerRouteConnections) {
        this.maxPerRouteConnections = maxPerRouteConnections;
    }

    public int getKeepAliveInSeconds() {
        return keepAliveInSeconds;
    }

    public void setKeepAliveInSeconds(int keepAliveInSeconds) {
        this.keepAliveInSeconds = keepAliveInSeconds;
    }
}
