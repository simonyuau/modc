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

import edu.monash.merc.eddy.modc.web.context.MDContext;
import edu.monash.merc.eddy.modc.web.multipart.FileUploadResponse;
import edu.monash.merc.eddy.modc.web.multipart.ProgressMonitor;
import edu.monash.merc.eddy.modc.web.multipart.Progressor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Monash University eResearch Center
 * <p/>
 * Created by simonyu - xiaoming.yu@monash.edu
 * Date: 6/09/2014
 */
@Controller
@RequestMapping("/file")
public class FileUploadController {


    //file upload
    @RequestMapping(value = "/fileupload")
    public String fileUpload() {
        return "file/fileupload";
    }

    //json response
    @RequestMapping(value = "/upload")
    public
    @ResponseBody
    FileUploadResponse uploadFile(@RequestParam("name") String name, @RequestParam("file") MultipartFile file, HttpServletRequest request, Model model) {
        FileUploadResponse response = new FileUploadResponse();
        try {
            if (!file.isEmpty()) {
                String path = request.getSession().getServletContext().getRealPath("/");
                System.out.println("========= path : " + path);

                //file.transferTo(new File(path + "upload/" + file.getOriginalFilename()));
                FileOutputStream fos = new FileOutputStream(new File(path + "upload/" + file.getOriginalFilename()));
                fos.write(file.getBytes());
                fos.flush();
                fos.close();
                model.addAttribute("fileName", file.getOriginalFilename());
                response.setSucceed(true);
                response.setMessage("File has been uploaded successfully");
                //return "file/uploadsuccess";
            } else {
//            return "file/failed";
                System.out.println("==== file must be provided");
                response.setSucceed(false);
                response.setMessage("File must be provided");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setSucceed(false);
            response.setMessage("Failed to upload file");
        }
        return response;
    }

    //json response
    @RequestMapping(value = "/progress")
    public
    @ResponseBody
    Progressor progress(HttpServletRequest request, @RequestParam("rnd") String rnd) {

        System.out.println("========== progress rnd : " + rnd);
//        ProgressMonitor monitor = (ProgressMonitor) request.getSession().getAttribute(ProgressMonitor.SESSION_PROGRESS_MONITOR);
        ProgressMonitor monitor = MDContext.getProgressMonitor();
        Progressor progressor = new Progressor();
        if (monitor != null) {
            System.out.println("=== found progress monitor in session....");
            progressor.setSentBytes(monitor.getBytesRead());
            progressor.setTotalBytes(monitor.getBytesLength());
            progressor.setCompletePercent(monitor.percentComplete());
            if (!monitor.isStillProcessing() || monitor.isAborted()) {
                progressor.setAborted(true);
                System.out.println("=====> progress monitor is aborted");
                progressor.setAbortedMessage(monitor.getAbortedMessage());
               // request.getSession().removeAttribute(ProgressMonitor.SESSION_PROGRESS_MONITOR);
                MDContext.removeProgressMonitor();
            } else {
                progressor.setAborted(false);
            }
            if (monitor.isFinished()) {
                System.out.println("===== progress monitor is finished");
               // request.getSession().removeAttribute(ProgressMonitor.SESSION_PROGRESS_MONITOR);
                MDContext.removeProgressMonitor();
            }
        } else {
            System.out.println("=========== ProgressMonitor is not found in the session.....");
            progressor.setSentBytes(0);
            progressor.setTotalBytes(0);
            progressor.setCompletePercent("0");
            progressor.setAborted(false);

        }
        return progressor;
    }
}
