package fi.muni.cz.pa165.travelagency.controllers;

import fi.muni.cz.pa165.travelagency.dto.UserDTO;
import fi.muni.cz.pa165.travelagency.facade.UserFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Admin controller
 * @author Martin Sevcik <422365>
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final String AUTH_PAGE_URL = "redirect:/auth/login";
    private static final String USER_EDIT_PAGE_URL = "redirect:/user/edit/";
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
     * Get method for user edit page
     * @param id id
     * @param redirectAttributes redirect attributes
     * @param req request
     * @param model model
     * @return user edit page
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editView(@PathVariable Long id,
                           RedirectAttributes redirectAttributes,
                           HttpServletRequest req,
                           Model model) {
        if (!isAuthenticated(req, null, false)) {
            return AUTH_PAGE_URL;
        }

        UserDTO userDTO = userFacade.findById(id);
        if (userDTO == null) {
            LOGGER.warn("GET user/view");
            redirectAttributes.addFlashAttribute("alert_danger", "Cannot display not existing user.");
        }

        LOGGER.info("GET request: user/edit/", id);
        model.addAttribute("user", userDTO);
        model.addAttribute("userEdit", new UserDTO());
        return "user/edit";
    }

    /**
     * Saves data to database
     * @param userUpdated user
     * @param id id
     * @param req req
     * @param redirectAttributes redirect attributes
     * @return next page
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String editUpdate(@Valid @ModelAttribute("userEdit") UserDTO userUpdated,
                             @PathVariable Long id,
                             HttpServletRequest req,
                             RedirectAttributes redirectAttributes) {
        if (!isAuthenticated(req, null, false)) {
            return AUTH_PAGE_URL;
        }

        if (userUpdated == null) {
            redirectAttributes.addFlashAttribute("alert_danger", "Cannot update not existing user.");
            return USER_EDIT_PAGE_URL + id;
        }

        UserDTO userDTO = userFacade.findById(id);
        if (userDTO == null) {
            redirectAttributes.addFlashAttribute("alert_danger", "Cannot update not existing user.");
            return USER_EDIT_PAGE_URL + id;
        }

        if (isEmpty(userUpdated.getName())) {
            redirectAttributes.addFlashAttribute("alert_danger", "Cannot update user with empty surname.");
            return USER_EDIT_PAGE_URL + id;
        }

        if (isEmpty(userUpdated.getIdCardNumber())) {
            redirectAttributes.addFlashAttribute("alert_danger", "Cannot update user with empty idCardNumber.");
            return USER_EDIT_PAGE_URL + id;
        }

        if (isEmpty(userUpdated.getEmail())) {
            redirectAttributes.addFlashAttribute("alert_danger", "Cannot update user with empty email.");
            return USER_EDIT_PAGE_URL + id;
        }

        try {
            userDTO.setName(userUpdated.getName());
            userDTO.setSurname(userUpdated.getSurname());
            userDTO.setEmail(userUpdated.getEmail());
            userDTO.setIdCardNumber(userUpdated.getIdCardNumber());
            userDTO.setPhoneNumber(userUpdated.getPhoneNumber());

            userFacade.updateUser(userDTO);
        } catch (Exception e) {
            LOGGER.error("POST request: user/edit/{}", id, e);
            redirectAttributes.addFlashAttribute("alert_danger", "Error occurred during updating");
            return USER_EDIT_PAGE_URL + id;
        }

        LOGGER.info("POST request: user/edit/{}", id);
        return "redirect:/user/view/" + id;
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
