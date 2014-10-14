package edu.monash.merc.eddy.modc.web.controller;

import edu.monash.merc.eddy.modc.common.config.SystemPropertyConts;
import edu.monash.merc.eddy.modc.common.ldap.LdapUser;
import edu.monash.merc.eddy.modc.common.util.MD5;
import edu.monash.merc.eddy.modc.domain.Avatar;
import edu.monash.merc.eddy.modc.domain.Profile;
import edu.monash.merc.eddy.modc.domain.User;
import edu.monash.merc.eddy.modc.domain.UserType;
import edu.monash.merc.eddy.modc.service.UserService;
import edu.monash.merc.eddy.modc.service.ldap.LdapService;
import edu.monash.merc.eddy.modc.service.mail.MailService;
import edu.monash.merc.eddy.modc.web.conts.MConts;
import edu.monash.merc.eddy.modc.web.form.LoginBean;
import edu.monash.merc.eddy.modc.web.form.RegistrationBean;
import edu.monash.merc.eddy.modc.web.form.ResetPasswordBean;
import edu.monash.merc.eddy.modc.web.validation.MDValidator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private MailService mailService;

    @Autowired
    private LdapService ldapService;


    private Logger logger = Logger.getLogger(this.getClass().getName());

    @RequestMapping("/registration_options")
    public String regoptions() {

        return "user/registration_options";
    }

    @RequestMapping("/showRegister")
    public String register(ModelMap model) {
        User user = new User();
        model.addAttribute("user", user);
        Profile profile = new Profile();
        profile.setOrganization("Monash");
        model.addAttribute("profile", profile);
        return "user/register";
    }

    @RequestMapping(value = "/ldap_register", method = RequestMethod.GET)
    public String ldapRegister(Model model) {
        RegistrationBean registration = new RegistrationBean();
        model.addAttribute("registration", registration);
        return "user/ldap_register";
    }


    @RequestMapping(value = "/ldap_register", method = RequestMethod.POST)
    public String ldapRegister(@ModelAttribute("registration") RegistrationBean registration, HttpServletRequest request, Model model) {
        actionSupport(request, model);
        User user = registration.getUser();
        validateLdapReg(registration, request);
        if (hasActionErrors()) {
            makeErrorAware();
            return "user/ldap_register";
        } else {
            try {
                //check ldap user
                LdapUser ldapUser = this.ldapService.verifyLdapUser(user.getUniqueId(), user.getPassword());
                if (ldapUser == null) {
                    addActionError("user.invalid.authcateId.or.password");
                    makeErrorAware();
                    return "user/ldap_register";
                }

                //check authcate id
                boolean idExisted = this.userService.checkExistedUniqueId(user.getUniqueId());
                if (idExisted) {
                    addActionError("user.authcate.id.already.registed");
                    makeErrorAware();
                    return "user/ldap_register";
                }

                //check email
                boolean emailRegistered = this.userService.checkExistedEmail(ldapUser.getMail());
                if (emailRegistered) {
                    addActionError("user.email.already.registered");
                    makeErrorAware();
                    return "user/ldap_register";
                }

                // try to register ldap user in the database
                user.setDisplayName(ldapUser.getDisplayName());
                user.setFirstName(ldapUser.getFirstName());
                user.setLastName(ldapUser.getLastName());
                // set ldap user password as ldap
                user.setPassword("ldap");
                user.setRegisteredDate(GregorianCalendar.getInstance().getTime());
                user.setUidHashCode(generateSecurityHash(user.getUniqueId()));
                // set user email which get from ldap server
                user.setEmail(ldapUser.getMail());
                user.setActivateHashCode(generateSecurityHash(user.getUniqueId()));

                user.setActivated(false);
                user.setUserType(UserType.USER.code());

                // create a default user profile.
                Profile p = registration.getProfile();
                if (p == null) {
                    p = new Profile();
                }
                p.setOrganization("Monash University");

                p.setGender(ldapUser.getGender());
                user.setProfile(p);
                p.setUser(user);
                // create an avatar
                Avatar avatar = genAvatar(p.getGender());
                avatar.setUser(user);
                user.setAvatar(avatar);

                this.userService.saveUser(user);
                // site name
                String serverQName = getServerQName(request);
                String appContext = getAppContextPath(request);

                // start to send register email to admin for approval
                String activateURL = constructActivationURL(serverQName, appContext, user.getId(), user.getActivateHashCode());
                sendRegMailToAdmin(serverQName, user.getDisplayName(), user.getEmail(), p.getOrganization(), activateURL);
            } catch (Exception ex) {
                logger.error(ex);
                addActionError("user.registration.failed");
                makeErrorAware();
                return "user/ldap_register";
            }
        }
        //set success message
        addActionMessage("user.registration.finished.msg", new String[]{user.getDisplayName()});
        makeMessageAware();
        return "user/user_action_finished";
    }


    @RequestMapping(value = "/self_register", method = RequestMethod.GET)
    public String selfRegister(Model model) {
        RegistrationBean registration = new RegistrationBean();
        model.addAttribute("registration", registration);
        return "user/self_register";
    }

    @RequestMapping(value = "/self_register", method = RequestMethod.POST)
    public String selfRegister(@ModelAttribute("registration") RegistrationBean registration, HttpServletRequest request, Model model) {
        actionSupport(request, model);
        validateReg(registration, request);
        if (hasActionErrors()) {
            makeErrorAware();
            return "user/self_register";
        }
        User user = registration.getUser();
        try {
            // encrypt the user password
            user.setPassword(MD5.hash(user.getPassword()));
            user.setRegisteredDate(GregorianCalendar.getInstance().getTime());
            // set the user email as a unique id
            user.setUniqueId(user.getEmail());
            // set the unique id hash code.
            user.setUidHashCode(generateSecurityHash(user.getEmail()));
            // set the activate hash code
            user.setActivateHashCode(generateSecurityHash(user.getEmail()));
            // set the user active into false
            user.setActivated(false);
            // set user type as a registered user.
            user.setUserType(UserType.USER.code());

            // create a default user profile.
            Profile p = registration.getProfile();
            p.setGender("Male");
            user.setProfile(p);
            p.setUser(user);

            // create an avatar
            Avatar avatar = genAvatar(p.getGender());
            user.setAvatar(avatar);
            avatar.setUser(user);

            // save user
            this.userService.saveUser(user);

            // site name
            String serverQName = getServerQName(request);
            String appContext = getAppContextPath(request);

            // start to send register email to admin for approval
            String activateURL = constructActivationURL(serverQName, appContext, user.getId(), user.getActivateHashCode());
            sendRegMailToAdmin(serverQName, user.getDisplayName(), user.getEmail(), p.getOrganization(), activateURL);
        } catch (Exception ex) {
            logger.error(ex);
            addActionError("user.registration.failed");
            makeErrorAware();
            return "user/self_register";
        }
        //set success message
        addActionMessage("user.registration.finished.msg", new String[]{user.getDisplayName()});
        makeMessageAware();
        return "user/user_action_finished";
    }

    @RequestMapping(value = "/user_login", method = RequestMethod.GET)
    public String userLogin(Model model) {
        LoginBean loginBean = new LoginBean();
        model.addAttribute("userLogin", loginBean);
        return "user/user_login";
    }

    @RequestMapping(value = "/user_login", method = RequestMethod.POST)
    public String userLogin(@ModelAttribute("userLogin") LoginBean login, HttpServletRequest request, Model model) {

        return "user/user_login";
    }

    @RequestMapping(value = "/password_reset", method = RequestMethod.GET)
    public String resetPassword(Model model) {
        ResetPasswordBean passwordReset = new ResetPasswordBean();
        model.addAttribute("passwordReset", passwordReset);
        return "user/password_reset";
    }

    @RequestMapping(value = "/password_reset", method = RequestMethod.POST)
    public String resetPassword(@ModelAttribute("passwordReset") ResetPasswordBean passwordreset, HttpServletRequest request, Model model) {

        return "user/password_reset";
    }

    private void validateLdapReg(RegistrationBean registration, HttpServletRequest request) {
        try {
            User user = registration.getUser();

            //uniqueId
            String uniqueId = user.getUniqueId();
            if (StringUtils.isBlank(uniqueId)) {
                addActionError("user.authcate.id.required");
            }
            //password
            String password = user.getPassword();
            if (StringUtils.isBlank(password)) {
                addActionError("user.authcate.password.required");
            }

            //security code
            String securityCode = registration.getSecurityCode();
            if (StringUtils.isBlank(securityCode)) {
                addActionError("security.code.required");
            } else {
                String captchaCode = (String) getFromSession(request, MConts.CAPTCHA_CODE_KEY);
                if (!StringUtils.equalsIgnoreCase(securityCode, captchaCode)) {
                    addActionError("security.code.invalid");
                }
            }
        } catch (Exception ex) {
            logger.error(ex);
            addActionError("user.check.user.ldap.account.failed");
        }
    }

    private void validateReg(RegistrationBean registration, HttpServletRequest request) {
        try {
            User user = registration.getUser();
            //first name
            String firstName = user.getFirstName();
            if (StringUtils.isBlank(firstName)) {
                addActionError("user.first.name.empty");
            } else {
                if (!MDValidator.validateName(firstName, 10)) {
                    addActionError("user.first.name.invalid");
                }
            }

            //last name
            String lastName = user.getLastName();
            if (StringUtils.isBlank(lastName)) {
                addActionError("user.last.name.empty");
            } else {
                if (!MDValidator.validateName(lastName, 10)) {
                    addActionError("user.last.name.invalid");
                }
            }

            //email
            String email = user.getEmail();
            if (StringUtils.isBlank(email)) {
                addActionError("user.email.empty");
            } else {
                if (!MDValidator.validateEmail(email)) {
                    addActionError("user.email.invalid");
                }
            }

            //password
            String password = user.getPassword();
            if (StringUtils.isBlank(password)) {
                addActionError("user.password.required");
            } else {
                if (!MDValidator.validatePassword(password, 6, 10)) {
                    addActionError("user.password.invalid");
                }
            }

            //organization
            Profile p = registration.getProfile();
            String organization = p.getOrganization();
            if (StringUtils.isBlank(organization)) {
                addActionError("user.organization.required");
            }

            //security code
            String securityCode = registration.getSecurityCode();
            if (StringUtils.isBlank(securityCode)) {
                addActionError("security.code.required");
            } else {
                String captchaCode = (String) getFromSession(request, MConts.CAPTCHA_CODE_KEY);
                if (!StringUtils.equalsIgnoreCase(securityCode, captchaCode)) {
                    addActionError("security.code.invalid");
                }
            }

            //if has inputs errors, just return
            if (hasActionErrors()) {
                return;
            }

            boolean emailExisted = this.userService.checkExistedEmail(user.getEmail());
            if (emailExisted) {
                addActionError("user.email.already.registered");
            }
            boolean displayNameExisted = this.userService.checkExistedName(user.getDisplayName());
            if (displayNameExisted) {
                addActionError("user.display.name.already.registered");
            }
        } catch (Exception ex) {
            addActionError("user.check.registration.failed");
        }
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

    private String constructActivationURL(String serverQName, String appContext, long userId, String activationCode) {
        String pkName = "admin";
        String actionName = "verify.htm?";
        String regUidPair = "id=" + userId;

        String hashCodePair = "&code=" + activationCode;

        StringBuffer activationURL = new StringBuffer();
        // application root url
        activationURL.append(serverQName).append(appContext).append(MConts.URL_PATH_DEIM);
        // action name
        activationURL.append(pkName).append(MConts.URL_PATH_DEIM).append(actionName);
        // actId, idcode, act name and hash code
        activationURL.append(regUidPair).append(hashCodePair);

        return new String(activationURL).trim();
    }

    private void sendRegMailToAdmin(String serverQName, String userName, String userEmail, String organization, String activationURL) {

        String activateEmailTemplateFile = "activateAccountEmailTemplate.ftl";
        String appName = systemPropertySettings.getPropValue(SystemPropertyConts.APPLICATION_NAME);
        // prepare to send email.
        String adminEmail = systemPropertySettings.getPropValue(SystemPropertyConts.SYSTEM_SERVICE_EMAIL);

        String subject = getText("user.account.activation.request.title");

        Map<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("RegisteredUser", userName);
        templateMap.put("UserEmail", userEmail);
        templateMap.put("Organization", organization);
        templateMap.put("ActivationURL", activationURL);
        templateMap.put("SiteName", serverQName);
        templateMap.put("AppName", appName);

        this.mailService.sendMail(adminEmail, adminEmail, subject, templateMap, activateEmailTemplateFile, true);
    }

}
