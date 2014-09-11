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

package edu.monash.merc.eddy.modc.web.multipart;

import edu.monash.merc.eddy.modc.web.context.MDContext;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Monash University eResearch Center
 * <p/>
 * Created by simonyu - xiaoming.yu@monash.edu
 * Date: 6/09/2014
 */
public class MonitoredCommonsMultipartResolver extends CommonsMultipartResolver {

    private ProgressMonitor progressMonitor;


    /**
     * Parse the given servlet request, resolving its multipart elements.
     *
     * @param request the request to parse
     * @return the parsing result
     * @throws MultipartException if multipart resolution failed.
     */
    @Override
    protected MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
        System.out.println("=================== >  multipart request received .....");
        String encoding = determineEncoding(request);
        System.out.println("========== encoding : " + encoding);
        FileUpload fileUpload = prepareFileUpload(encoding);
        progressMonitor = new ProgressMonitor();
        // Assert.notNull(progressMonitor, "progressMonitor must not be null");
        //set the progress monitor
        fileUpload.setProgressListener(progressMonitor);
        //set the ProgressMonitor in to session
       // request.getSession().setAttribute(ProgressMonitor.SESSION_PROGRESS_MONITOR, progressMonitor);
        MDContext.setProgressMonitor(progressMonitor);

        System.out.println("========= finished to set progress monitor");
        try {
            List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
            MultipartParsingResult result = parseFileItems(fileItems, encoding);
            return result;
        } catch (FileUploadBase.SizeLimitExceededException ex) {
            //set progressMonitor aborted
            if (progressMonitor != null) {
                progressMonitor.abort();
                progressMonitor.setAbortedMessage("Maximum upload size of " + fileUpload.getSizeMax() + " bytes exceeded");
            }
            throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
        } catch (FileUploadException ex) {
            //set progressMonitor aborted
            if (progressMonitor != null) {
                progressMonitor.abort();
                progressMonitor.setAbortedMessage("Could not parser multipart servlet request");
            }
            throw new MultipartException("Could not parse multipart servlet request", ex);
        }
    }

    @Override
    public void cleanupMultipart(MultipartHttpServletRequest request) {
        super.cleanupMultipart(request);
        if (progressMonitor != null) {
            progressMonitor.finished();
        }
    }
}
