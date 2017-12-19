package fi.muni.cz.pa165.travelagency.controllers;

import fi.muni.cz.pa165.travelagency.dto.UserDTO;
import fi.muni.cz.pa165.travelagency.facade.UserFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Admin controller
 * @author Martin Sevcik <422365>
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final String AUTH_PAGE_URL = "redirect:/auth/login";
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private UserFacade userFacade;

    /**
     * Displays info about user
     * @param id id
     * @param model model
     * @param redirectAttributes redirect attributes
     * @param req request
     * @return user view page
     */
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable Long id,
                       Model model,
                       RedirectAttributes redirectAttributes,
                       HttpServletRequest req) {
        LOGGER.info("GET request: /user/view/{}", id);
        if (!isAuthenticated(req, null, false)) {
            return AUTH_PAGE_URL;
        }

        UserDTO userDTO = userFacade.findById(id);
        model.addAttribute("user", userDTO);
        if (userDTO == null) {
            LOGGER.warn("GET user/view");
            redirectAttributes.addFlashAttribute("alert_danger", "Cannot display not existing user.");
        }
        model.addAttribute("authenticatedUser", (UserDTO) req.getSession().getAttribute("authenticatedUser"));
        return "user/view";
    }

    /**
     * Gets list of users
     * @param model model
     * @param req request
     * @param redirectAttributes redirect attributes
     * @return list of users page
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model,
                       HttpServletRequest req,
                       RedirectAttributes redirectAttributes) {
        if (!isAuthenticated(req, redirectAttributes, true)) {

            return AUTH_PAGE_URL;
        }
        model.addAttribute("authenticatedUser", (UserDTO) req.getSession().getAttribute("authenticatedUser"));
        LOGGER.info("GET request: user/list");
        model.addAttribute("users", userFacade.findAll());
        return "user/list";
    }

    /**
     * Removes user
     * @param id id of user
     * @param redirectAttributes redirect attributes
     * @param req request
     * @return list of users
     */
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    public String remove(@PathVariable Long id,
                         RedirectAttributes redirectAttributes,
                         HttpServletRequest req) {
        if (!isAuthenticated(req, redirectAttributes, true)) {
            return AUTH_PAGE_URL;
        }

        UserDTO userDTO = userFacade.findById(id);
        if (userDTO == null) {
            LOGGER.error("POST user/remove/{}", id);
            redirectAttributes.addFlashAttribute("alert_danger", "Cannot delete not existing user.");
            return "redirect:/user/list";
        }

        if (userDTO.getReservations().size() > 0) {
            LOGGER.error("POST user/remove/{}", id);
            redirectAttributes.addFlashAttribute("alert_danger", "Cannot delete user with reservations.");
            return "redirect:/user/list";
        }
        
        LOGGER.info("POST request: user/remove/", id);
        userFacade.removeUser(userDTO);
        return "redirect:/user/list";
    }

    /**
     * Checks if string is empty
     * @param str string
     * @return true if empty, false otherwise
     */
    private Boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Checks whether user is authenticated
     * @param req request
     * @param redirectAttributes redirect attributes
     * @param shouldBeAdmin should Be Admin
     * @return user view
     */
    private Boolean isAuthenticated(HttpServletRequest req, RedirectAttributes redirectAttributes,
                                    Boolean shouldBeAdmin) {
        UserDTO authUser = (UserDTO) req.getSession().getAttribute("authenticatedUser");
        if (authUser == null) {
            if (redirectAttributes != null) {
                redirectAttributes.addFlashAttribute("alert_danger", "Admin role required");
            }

            LOGGER.error("user should be authenticated or admin for this operation");
            return false;
        }

        return shouldBeAdmin ? authUser.getIsAdmin() : true;
    }
}
