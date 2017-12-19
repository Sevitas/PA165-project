package fi.muni.cz.pa165.travelagency.controllers;

import fi.muni.cz.pa165.travelagency.dto.ReservationCreateDTO;
import fi.muni.cz.pa165.travelagency.dto.ReservationDTO;
import fi.muni.cz.pa165.travelagency.dto.UserDTO;
import fi.muni.cz.pa165.travelagency.facade.ExcursionFacade;
import fi.muni.cz.pa165.travelagency.facade.ReservationFacade;
import fi.muni.cz.pa165.travelagency.facade.TripFacade;
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
import java.util.Date;

/**
 * @author Pavel Kotala
 */
@Controller
@RequestMapping("/browsing")
public class BrowsingController {

    public static final Logger LOGGER = LoggerFactory.getLogger(BrowsingController.class);

    @Autowired
    private TripFacade tripFacade;

    @Autowired
    private ExcursionFacade excursionFacade;

    @Autowired
    private ReservationFacade reservationFacade;

    /**
     * Shows a list of trips with the ability to buy.
     *
     * @param redirectAttributes redirect attributes
     * @param request request
     * @param model data to display
     * @return JSP page name
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        UserDTO authUser = (UserDTO) request.getSession().getAttribute("authenticatedUser");
        if (authUser == null) {
            LOGGER.warn("Failed. Not logged in");
            redirectAttributes.addFlashAttribute("alert_danger",
                    "You must log in.");
            return "redirect:/auth/login";
        }
        model.addAttribute("authenticatedUser", authUser);
        model.addAttribute("trips", tripFacade.getAllTrips());
        return "browsing/list";
    }

    /**
     * Creates reservation with given trip id
     * @param redirectAttributes redirect attributes
     * @param request request
     * @param id id of trip to show
     * @param model model
     * @return jsp page name
     */
    @RequestMapping(value = "/create/{id}", method = RequestMethod.GET)
    public String create(@PathVariable long id, Model model, HttpServletRequest request,
                       RedirectAttributes redirectAttributes) {

        UserDTO authUser = (UserDTO) request.getSession().getAttribute("authenticatedUser");
        if (authUser == null) {
            LOGGER.warn("Failed. Not authorized");
            redirectAttributes.addFlashAttribute("alert_danger",
                    "Not logged in.");
            return "redirect:/auth/login";
        }
        model.addAttribute("authenticatedUser", authUser);
        ReservationCreateDTO create = new ReservationCreateDTO();
        create.setDate(new Date());
        create.setUserId(authUser.getId());
        create.setTripId(id);

        Long resId = reservationFacade.createReservation(create);

        return view(resId, model, request, redirectAttributes);
    }

    /**
     * Shows reservation with given id
     * @param redirectAttributes redirect attributes
     * @param request request
     * @param id id of trip to show
     * @param model model
     * @return jsp page name
     */
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable long id, Model model, HttpServletRequest request,
                       RedirectAttributes redirectAttributes) {

        UserDTO authUser = (UserDTO) request.getSession().getAttribute("authenticatedUser");
        ReservationDTO reservationDTO = reservationFacade.findReservationById(id);
        if (authUser == null || !authUser.equals(reservationDTO.getUser())) {
            LOGGER.warn("Failed. Not authorized");
            redirectAttributes.addFlashAttribute("alert_danger",
                    "Not authorized.");
            return "redirect:/auth/login";
        }
        model.addAttribute("authenticatedUser", authUser);
        LOGGER.debug("view({})", id);
        model.addAttribute("trip", reservationDTO.getTrip());
        model.addAttribute("reservation", reservationFacade.findReservationById(id));
        return "browsing/view";
    }

    /**
     * Adds excursion
     * @param redirectAttributes redirect attributes
     * @param request request
     * @param idRes id of reservation
     * @param idExc id of excursion
     * @param model model
     * @return jsp page name
     */
    @RequestMapping(value = "/add/{idRes}/{idExc}", method = RequestMethod.GET)
    public String add(@PathVariable long idRes, @PathVariable long idExc, Model model, HttpServletRequest request,
                       RedirectAttributes redirectAttributes) {

        UserDTO authUser = (UserDTO) request.getSession().getAttribute("authenticatedUser");
        ReservationDTO reservationDTO = reservationFacade.findReservationById(idRes);
        if (authUser == null || !authUser.equals(reservationDTO.getUser())) {
            LOGGER.warn("Failed. Not authorized");
            redirectAttributes.addFlashAttribute("alert_danger",
                    "Not authorized.");
            return "redirect:/auth/login";
        }
        
        reservationFacade.addExcursionToReservation(idRes, idExc);
        model.addAttribute("authenticatedUser", authUser);
        LOGGER.debug("view({})", idRes);
        model.addAttribute("trip", reservationDTO.getTrip());
        model.addAttribute("reservation", reservationFacade.findReservationById(idRes));
        return "reservation/detail";
    }


}

