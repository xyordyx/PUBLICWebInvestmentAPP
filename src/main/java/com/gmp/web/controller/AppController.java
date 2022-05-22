package com.gmp.web.controller;

import com.gmp.investmentAPP.investmentAPPUtil;
import com.gmp.persistence.model.User;
import com.gmp.registration.OnRegistrationCompleteEvent;
import com.gmp.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AppController {
    @Autowired
    private IUserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RegistrationRestController reg;

    @Autowired
    private MessageSource messages;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private String temp;
    private String userTemp;

    @GetMapping({"/login"})
    public String login() {
        return "login";
    }

    @GetMapping({"/adminHome"})
    public String adminHome(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "adminHome";
    }

    @GetMapping("/edit")
    public String editUser(@RequestParam("id") final Long userID, HttpServletRequest request) {
        User user = userService.getUserByID(userID);
        request.setAttribute("user", user);
        temp = user.getPassword();
        return "adminUserUpdate";
    }

    @PostMapping(value = "/edit")
    public String editUser(@ModelAttribute("user") User user) {
        if(user.getPassword().equals(temp)){
            userService.updateUserAdmin(user,false);
        }else userService.updateUserAdmin(user,true);
        return "redirect:/adminHome?updated=true";
    }

    @GetMapping({"/adminUserRegistration"})
    public String adminUserRegistration(Model model) {
        User user = new User();
        Map<String, String> roleMap = new HashMap<>();
        roleMap.put("ROLE_ADMIN", "ADMIN");
        roleMap.put("ROLE_investmentAPP", "investmentAPP");
        roleMap.put("ROLE_USER", "USER");
        roleMap.put("ROLE_FACTUREDO", "FACTUREDO");
        model.addAttribute("roles",roleMap);
        model.addAttribute("user", user);
        return "adminUserRegistration";
    }

    @PostMapping(value = "/adminUserRegistration")
    public String adminUserRegistration(@ModelAttribute("user") User user, final HttpServletRequest request) {
        final User registered = userService.registerNewUserAccount(user);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), investmentAPPUtil.getAppUrl(request)));
        return "redirect:/adminHome?saved=true";
    }

    @GetMapping({"/userUpdate"})
    public String userUpdate(Model model, @AuthenticationPrincipal User user) {
        User actualUser = userService.getUserByID(user.getId());
        model.addAttribute("user", actualUser);
        userTemp = actualUser.getPassword();
        return "userUpdate";
    }

    @PostMapping({"/userUpdate"})
    public String userUpdate(@ModelAttribute("user") User user) {
        if(user.getPassword().equals(userTemp)){
            userService.updateUser(user,false);
        }else userService.updateUser(user,true);
        return "redirect:/userUpdate?saved=true";
    }

    @GetMapping("/unblock")
    public String unblockCustomer(@RequestParam("id") final Long userID) {
        User user = userService.getUserByID(userID);
        user.setBlocked(false);
        userService.updateUser(user);
        return "redirect:/adminHome";
    }

    @GetMapping("/block")
    public String blockCustomer(@RequestParam("id") final Long userID) {
        User user = userService.getUserByID(userID);
        user.setBlocked(true);
        userService.updateUser(user);
        return "redirect:/adminHome";
    }

    @GetMapping("/confirm")
    public String confirmCustomerEmail(@RequestParam("id") final Long userID) {
        User user = userService.getUserByID(userID);
        user.setEnabled(true);
        userService.updateUser(user);
        return "redirect:/adminHome";
    }

    // Reset password ADMIN
    @GetMapping("/resetPassword")
    public String resetPassword(final HttpServletRequest request, Model model, @RequestParam("id") final Long userID) {
        final User user = userService.getUserByID(userID);
        final String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);
        mailSender.send(reg.constructResetTokenEmail(reg.getAppUrl(request), request.getLocale(), token, user));
        model.addAttribute("message", messages.getMessage("message.resetPasswordEmailAdmin", null, request.getLocale()));
        return "redirect:/adminHome";
    }

    @GetMapping({"/landing"})
    public String landingPage(HttpServletRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Set<String> roles = authentication.getAuthorities().stream()
                .map(r -> r.getAuthority()).collect(Collectors.toSet());
        List<String> aList = new ArrayList<String>(roles.size());
        for (String x : roles) {
            aList.add(x);
        }
        request.setAttribute("roles", aList);

        return "landing";
    }

    @GetMapping({"/publicIndex"})
    public String publicIndex(){ return "publicIndex"; }

    @GetMapping({"/investPage"})
    public String invest(){ return "waitForInvoice"; }
}
