package edu.monash.merc.eddy.modc.web.controller;

import edu.monash.merc.eddy.modc.domain.Avatar;
import edu.monash.merc.eddy.modc.domain.Profile;
import edu.monash.merc.eddy.modc.domain.User;
import edu.monash.merc.eddy.modc.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.io.File;

/**
 * Created by simonyu on 6/08/2014.
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

//    @Autowired
//    private UserValidator userValidator;
////
//    @Autowired
//    private ProfileValidator profileValidator;

    @Autowired
    private UserService userService;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

//    @InitBinder("user")
//    private void initUserBinder(WebDataBinder binder) {
//        binder.setValidator(userValidator);
//
//    }

//    @InitBinder("profile")
//    private void initProfileBinder(WebDataBinder binder) {
//        binder.setValidator(profileValidator);
//
//    }

    @RequestMapping("/showRegister")
    public String register(ModelMap model) {
        User user = new User();
        model.addAttribute("user", user);
        Profile profile = new Profile();
        profile.setOrganization("Monash");
        model.addAttribute("profile", profile);
        return "user/register";
    }
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@ModelAttribute("user") User user, Errors uErrors, @ModelAttribute("profile") Profile profile, Errors pErrors) {
        boolean hasError = false;
        String firstName = user.getFirstName();

        if (StringUtils.isBlank(firstName)) {
            uErrors.rejectValue("firstName", "user.first.name.empty");
            hasError = true;
        }
        String email = user.getEmail();
        if (StringUtils.isBlank(email)) {
            uErrors.rejectValue("email", "user.email.empty");
            hasError = true;
        }
        String organization = profile.getOrganization();
        if (StringUtils.isBlank(organization)) {
            pErrors.rejectValue("organization", "user.organization.required");
            hasError = true;
        }
        if (hasError) {
            return "user/register";
        } else {
            User foundUser = userService.getUserByEmail(user.getEmail());

            if (foundUser != null) {
                uErrors.rejectValue("email", "user.email.already.existed");
                return "user/register";

            }
            Profile p = genProfile(organization);
            p.setUser(user);
            user.setProfile(p);

            Avatar avatar = genAvatar(p.getGender());
            avatar.setUser(user);
            user.setAvatar(avatar);
            //save the user
            userService.saveUser(user);


            return "user/regsuccess";

        }
    }

//    @RequestMapping(value = "/register", method = RequestMethod.POST)
//    public String register(@ModelAttribute("user") @Valid User user, BindingResult result, @ModelAttribute("profile") Profile profile, BindingResult pResult) {
//        logger.info("registered");
//        if (result.hasErrors()) {
//            return "user/register";
//        }
////        if (pResult.hasErrors()) {
////            return "user/register";
////        }
//
//        String organization = profile.getOrganization();
//        if (StringUtils.isBlank(organization)) {
//            pResult.rejectValue("organization", "user.organization.required");
//            return "user/register";
//        }
//
//        logger.info("User info: " + user);
//
//        User foundUser = userService.getUserByEmail(user.getEmail());
//
//        if (foundUser != null) {
//            result.rejectValue("email", "user.email.already.existed");
//            return "user/register";
//        }
//
//
//        Profile p = genProfile(organization);
//        p.setUser(user);
//        user.setProfile(p);
//
//        Avatar avatar = genAvatar(p.getGender());
//        avatar.setUser(user);
//        user.setAvatar(avatar);
//        //save the user
//        userService.saveUser(user);
//
//
//        return "user/regsuccess";
//    }

    private Profile genProfile(String organization) {
        Profile profile = new Profile();
        profile.setGender("Male");
        profile.setOrganization(organization);
        return profile;
    }

    private Avatar genAvatar(String gender) {
        Avatar avatar = new Avatar();
        String avatarFile = null;
        if (StringUtils.isBlank(gender)) {
            avatarFile = "avatar" + File.separator + "male.png";
        } else {
            if (StringUtils.equalsIgnoreCase(gender, "male")) {
                avatarFile = "avatar" + File.separator + "male.png";
            } else if (StringUtils.equalsIgnoreCase(gender, "female")) {
                avatarFile = "avatar" + File.separator + "female.png";
            } else {
                avatarFile = "avatar" + File.separator + "male.png";
            }
        }
        avatar.setFileName(avatarFile);
        avatar.setFileType("png");
        return avatar;
    }

}
