package fi.muni.cz.pa165.travelagency.controllers;


import fi.muni.cz.pa165.travelagency.dto.UserAuthenticateDTO;
import fi.muni.cz.pa165.travelagency.dto.UserDTO;
import fi.muni.cz.pa165.travelagency.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Authentication controller
 * @author Martin Sevcik <422365>
 */
@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private UserFacade userFacade;

    /**
     * Provides loading of logging page
     * @param req request
     * @return login page or main page if user is already logged in
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String authForm(HttpServletRequest req) {
        LOGGER.info("GET request: /auth/login");
        HttpSession session = req.getSession(true);
        if (session.getAttribute("authenticatedUser") != null) {
            return null;
        }

        return "auth/login";
    }

    /**
     * Provides login for user
     * @param email email
     * @param password password
     * @param redirectAttributes redirect attributes
     * @param req request
     * @return main page
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String authenticate(
            @RequestParam String email,
            @RequestParam String password,
            RedirectAttributes redirectAttributes,
            HttpServletRequest req) {
        LOGGER.info("POST request: /auth/login");
        UserAuthenticateDTO userAuthenticateDTO = new UserAuthenticateDTO();
        userAuthenticateDTO.setPasswordHash(password);
        userAuthenticateDTO.setEmail(email);
        UserDTO userDTO = userFacade.authenticate(userAuthenticateDTO);
        if (userDTO == null) {
            LOGGER.warn("POST request: /auth/login invalid login credentials for email {0}", email);
            redirectAttributes.addFlashAttribute("alert_info", "Invalid login email or password.");
            return "redirect:/auth/login";
        }

        LOGGER.info("POST request: /auth/login user logged in for email {0}", email);
        HttpSession session = req.getSession(true);
        userDTO.setIsAdmin(userFacade.isAdmin(userDTO));
        session.setAttribute("authenticatedUser", userDTO);
        redirectAttributes.addFlashAttribute("alert_info", "You have been logged in.");

        return userDTO.getIsAdmin() ? "redirect:/user/list" : "redirect:/user/view/" + userDTO.getId();
    }

    /**
     * Provides logout for user
     * @param req request
     * @param redirectAttributes redirect attributes
     * @return login page
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest req,
                         RedirectAttributes redirectAttributes) {
        LOGGER.info("GET request: /auth/logout");
        HttpSession session = req.getSession(true);
        session.removeAttribute("authenticatedUser");
        redirectAttributes.addFlashAttribute("alert_info", "You have been successfully logged out.");
        return "redirect:auth/login";
    }
}
