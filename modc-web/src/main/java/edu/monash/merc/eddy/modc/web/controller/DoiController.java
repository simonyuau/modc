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

import edu.monash.merc.eddy.modc.domain.doi.DoiCreator;
import edu.monash.merc.eddy.modc.domain.doi.DoiResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Monash University eResearch Center
 * <p/>
 * Created by simonyu - xiaoming.yu@monash.edu
 * Date: 23/09/2014
 */
@Controller
@RequestMapping("/doi")
public class DoiController extends MBaseController {


    @RequestMapping(value = "/mint", method = RequestMethod.GET)
    public String mintDoi(HttpServletRequest request, Model model) {
        DoiResource doiResource = new DoiResource();
        List<DoiCreator> creators = new ArrayList<>();
        DoiCreator creator = new DoiCreator();
        creator.setCreatorName("Yu,Simon");
        creators.add(creator);
        creator = new DoiCreator();
        creator.setCreatorName("Beitz,Anthony");
        creators.add(creator);
        doiResource.setDoiCreators(creators);

        doiResource.setUrl("http://www.ozflux.org.au");
        model.addAttribute("doiResource", doiResource);


        //add message support
        messageSupport(request, model);

        addActionError("doi.show.mint.service.success");
        addActionError("doi.show.mint.service.page.success");
        makeErrorAware();
        model.addAttribute("pageTitle", "Doi Minting Service");
        return "doi/doi_mint";
    }

    @RequestMapping(value = "/mint", method = RequestMethod.POST)
    public String mint(@ModelAttribute("doiResource") DoiResource doiResource) {
        System.out.println("========= url : " + doiResource.getUrl());
        List<DoiCreator> creators = doiResource.getDoiCreators();
        for (DoiCreator creator : creators) {
            System.out.println("====== creatorName: " + creator.getCreatorName());
        }
        System.out.println("======== ok!");

        return "doi/doi_success";
    }

}
