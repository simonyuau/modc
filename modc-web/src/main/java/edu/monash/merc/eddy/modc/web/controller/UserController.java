package edu.monash.merc.eddy.modc.web.controller;

import edu.monash.merc.eddy.modc.domain.User;
import edu.monash.merc.eddy.modc.service.UserService;
import edu.monash.merc.eddy.modc.web.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by simonyu on 6/08/2014.
 */
@Controller
@RequestMapping("/user")
public class UserController extends MDBaseController {

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(userValidator);
    }

    @RequestMapping("/showRegister")
    public String register(ModelMap model) {
        User user = new User();
        model.addAttribute("user", user);
        return "user/register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@ModelAttribute("user") @Valid User user, BindingResult result) {
        logger.info("registered");
        if (result.hasErrors()) {
            return "user/register";
        } else {
            logger.info("User info: " + user);

            User foundUser = userService.getUserByEmail(user.getEmail());

            if (foundUser != null) {
                result.rejectValue("email", "user.email.already.existed");
                return "user/register";
            } else {
                userService.saveUser(user);
            }

        }

        return "user/regsuccess";
    }

}
