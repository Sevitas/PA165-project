package fi.muni.cz.pa165.travelagency.controllers;


import fi.muni.cz.pa165.travelagency.dto.ExcursionDTO;
import fi.muni.cz.pa165.travelagency.dto.ReservationDTO;
import fi.muni.cz.pa165.travelagency.dto.TripDTO;
import fi.muni.cz.pa165.travelagency.dto.UserDTO;

import fi.muni.cz.pa165.travelagency.exceptions.TravelAgencyServiceException;
import fi.muni.cz.pa165.travelagency.facade.ExcursionFacade;
import fi.muni.cz.pa165.travelagency.facade.ReservationFacade;
import fi.muni.cz.pa165.travelagency.facade.TripFacade;
import fi.muni.cz.pa165.travelagency.facade.UserFacade;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Spring-mvc controller for reservations
 * @author Radoslav Micko <445611>
 */
@Controller
@RequestMapping("/reservation")
public class ReservationController {
    
    @Autowired
    private ReservationFacade reservationFacade;

    @Autowired
    private TripFacade tripFacade;

    @Autowired
    private ExcursionFacade excursionFacade;
    
    @Autowired
    private UserFacade userFacade;
    
    static final Logger LOGGER = LoggerFactory.getLogger(ReservationController.class);
    
    static final String LISTALL = "reservation/list";
    
    /**
     * List all reservations as default, sorted by date or notpaid 
     * @param model data to show
     * @param sorted atribute to return different lists
     * @param request request
     * @param redirectAttributes redirection attributes
     * @return jsp page name
     */
    @RequestMapping(value = "/list/{sorted}", method = RequestMethod.GET)
    public String listsorted(Model model,
            @PathVariable String sorted,
            HttpServletRequest request, 
            RedirectAttributes redirectAttributes) {
        
        List<ReservationDTO> reservations;
        UserDTO authUser = (UserDTO) request.getSession().getAttribute("authenticatedUser");

        if (authUser != null) {
            if (authUser.getIsAdmin()) {
                switch (sorted) {
                    case "sorted" :
                        reservations = reservationFacade.findAllSortedByDate();
                        break;
                    case "notpaid" :
                        reservations = reservationFacade.findAllNotPaid();
                        break;
                    default:
                       reservations = reservationFacade.findAllReservations();
                       break;
                }
                        
                
            } else {
                LOGGER.warn("no administrator GET list/" + sorted);
                reservations = reservationFacade.findReservationByUser(authUser.getId()); 
            }  
        } else {
            LOGGER.warn("GET listSorted /list/{} failed. Unauthorized", sorted);
            redirectAttributes.addFlashAttribute("alert_danger",
                        "Unanthorized.");
            return "redirect:/auth/login";
        }
        model.addAttribute("authenticatedUser", authUser);
        model.addAttribute("reservations", reservations);
        model.addAttribute("users", userFacade.findAll());
        return LISTALL;
    }
    
    
    /**
     * Show details about one reservation
     * @param id id of reservation
     * @param model data to show
     * @param redirectAttributes redirection attributes
     * @param req request
     * @return jsp page name
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes,
            HttpServletRequest req) {
        LOGGER.info("GET reservation/detail/" + id);
        
        ReservationDTO reservationDTO;
        UserDTO authUser = (UserDTO) req.getSession().getAttribute("authenticatedUser");
        
        if (authUser != null) {
            try {        
                reservationDTO = reservationFacade.findReservationById(id);
                if (reservationDTO == null) {
                    redirectAttributes.addFlashAttribute("alert_danger", 
                            "Reservation with id=" + id + " doesn't exist");
                    return "redirect:/reservation/list/all";
                }

                if (!authUser.getIsAdmin() && 
                !authUser.getId().equals(reservationDTO.getUser().getId())) {
                    redirectAttributes.addFlashAttribute("alert_danger", 
                "You don't have permission to show other people's reservation");
                    return "redirect:/reservation/list";
                }
            } catch (TravelAgencyServiceException ex) {
                LOGGER.error("request: GET reservation/detail/{}; user id={}", id, authUser.getId(), ex);
                redirectAttributes.addFlashAttribute(
                        "alert_danger", "Reservation with id=" + id + " isn't accessible.");
                return "redirect:/reservation/list/all";
            }
        } else { 
            LOGGER.warn("GET detail /detail/{} failed. Unauthorized", id);
            redirectAttributes.addFlashAttribute("alert_danger",
                        "Unanthorized.");
            return "redirect:/auth/login";
        }
        model.addAttribute("authenticatedUser", authUser);
        model.addAttribute("reservation", reservationDTO);
        return "reservation/detail";
    }
    
    
    /**
     * Delete one reservation
     * @param id id of reservation to delete
     * @param req request
     * @param redirectAttributes redirection attributes
     * @return jsp page name
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(
            @PathVariable Long id, 
            HttpServletRequest req, 
            RedirectAttributes redirectAttributes) {

        LOGGER.info("GET /reservation/delete/" + id);
        
        UserDTO authUser = (UserDTO) req.getSession().getAttribute("authenticatedUser");
        ReservationDTO reservationDTO;
        
        if (authUser != null) {
            try {
                reservationDTO = reservationFacade.findReservationById(id);
                if (reservationDTO == null) {
                    redirectAttributes.addFlashAttribute(
                            "alert_danger", "Reservation with id=" + id + " doesn't exist");
                    return "redirect:/reservation/list/all";
                }

                if (!authUser.getIsAdmin() && 
                    !authUser.getId().equals(reservationDTO.getUser().getId())) {
                        redirectAttributes.addFlashAttribute("alert_danger", 
                    "You don't have permission to delete other people's reservation");
                        return "redirect:/reservation/list";
                }
                reservationFacade.removeReservation(reservationDTO.getId());
            } catch (TravelAgencyServiceException ex) {
                LOGGER.error("GET /reservatin/delete/" + id, ex);
                redirectAttributes.addFlashAttribute(
                    "alert_danger", "Reservation with id="+ id +
                            " couldn't be deleted right now.");
                return "redirect:/reservation/list/all";   
            }
        } else { 
            LOGGER.warn("GET delete /delete/{} failed. Unauthorized", id);
            redirectAttributes.addFlashAttribute("alert_danger",
                        "Unanthorized.");
            return "redirect:/auth/login";
        }    
        redirectAttributes.addFlashAttribute(
                    "alert_success", "Reservation with id="+ id +
                            " was deleted.");
        return "redirect:/reservation/list/all";
    }
    
    /**
     * Set reservation to paid state
     * @param id id of reservation to pay
     * @param req request
     * @param redirectAttributes redirection attributes
     * @return jsp page name
     */
    @RequestMapping(value = "/paid/{id}", method = RequestMethod.GET)
    public String paid(
            @PathVariable Long id, 
            HttpServletRequest req, 
            RedirectAttributes redirectAttributes) {

        LOGGER.info("GET /reservation/paid/" + id);
        
        UserDTO authUser = (UserDTO) req.getSession().getAttribute("authenticatedUser");
        ReservationDTO reservationDTO;
        
        if (authUser != null) {
            try {
                reservationDTO = reservationFacade.findReservationById(id);
                if (reservationDTO == null) {
                    redirectAttributes.addFlashAttribute(
                        "alert_danger", "Reservation with id=" + id + " doesn't exist");
                    return "redirect:/reservation/list/all";
                }

                if (!authUser.getIsAdmin() && 
                    !authUser.getId().equals(reservationDTO.getUser().getId())) {
                    redirectAttributes.addFlashAttribute("alert_danger", 
                    "You don't have permission to paid other people's reservation");
                    return "redirect:/reservation/list";
                }
                reservationFacade.setReservationPaid(reservationDTO.getId());
            } catch (TravelAgencyServiceException ex) {
                LOGGER.error("GET /reservatin/paid/" + id, ex);
                redirectAttributes.addFlashAttribute(
                    "alert_danger", "Reservation with id="+ id +
                            " couldn't be paid right now.");
                return "redirect:/reservation/list/all";    
            }
        } else { 
            LOGGER.warn("GET paid /paid/{} failed. Unauthorized", id);
            redirectAttributes.addFlashAttribute("alert_danger",
                        "Unanthorized.");
            return "redirect:/auth/login";
        }    
   
        redirectAttributes.addFlashAttribute(
                    "alert_success", "Reservation with id="+ id +
                            " was paid.");
        return "redirect:/reservation/list/all";
    }
    
    /**
     * Set reservation to notpaid state
     * @param id id of reservation to set notpaid
     * @param req request
     * @param redirectAttributes redirection attributes
     * @return jsp page name
     */
    @RequestMapping(value = "/notpaid/{id}", method = RequestMethod.GET)
    public String notpaid(
            @PathVariable Long id, 
            HttpServletRequest req, 
            RedirectAttributes redirectAttributes) {

        LOGGER.info("GET /reservation/notpaid/" + id);
        
        UserDTO authUser = (UserDTO) req.getSession().getAttribute("authenticatedUser");
        ReservationDTO reservationDTO;
        
        if (authUser != null) {
            try {
                reservationDTO = reservationFacade.findReservationById(id);
                if (reservationDTO == null) {
                    redirectAttributes.addFlashAttribute(
                        "alert_danger", "Reservation with id=" + id + " doesn't exist");
                    return "redirect:/reservation/list/all";
                }

                if (!authUser.getIsAdmin() && 
                    !authUser.getId().equals(reservationDTO.getUser().getId())) {
                    redirectAttributes.addFlashAttribute("alert_danger", 
                        "You don't have permission to paid other people's reservation");
                    return "redirect:/reservation/list";
                }
                reservationFacade.setReservationNotPaid(reservationDTO.getId());
            } catch (TravelAgencyServiceException ex) {
                LOGGER.error("GET /reservatin/paid/" + id, ex);
                redirectAttributes.addFlashAttribute(
                    "alert_danger", "Reservation with id="+ id +
                            " couldn't be notpaid right now.");
                return "redirect:/reservation/list/all";   
            }
        } else { 
            LOGGER.warn("GET notpaid /notpaid/{} failed. Unauthorized", id);
            redirectAttributes.addFlashAttribute("alert_danger",
                        "Unanthorized.");
            return "redirect:/auth/login";
        }
        redirectAttributes.addFlashAttribute(
                    "alert_success", "Reservation with id="+ id +
                            " was set to NotPaid.");
        return "redirect:/reservation/list/all";
    }
    
    /**
     * Edit reservation info 
     * @param id id of reservation to edit
     * @param model data to show
     * @param redAttr redirection attributes
     * @param res http servetl response
     * @param req request
     * @return jsp page name
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, Model model, 
            RedirectAttributes redAttr, HttpServletResponse res, HttpServletRequest req) {
        LOGGER.info("GET /edit/" + id);
        UserDTO authUser = (UserDTO) req.getSession().getAttribute("authenticatedUser");
        if (authUser != null) {
            List<ExcursionDTO> excursions = excursionFacade.getAllExcursions();
            ReservationDTO reservation = reservationFacade.findReservationById(id);
            TripDTO trip = tripFacade.getTripWithId(reservation.getTrip().getId());
            excursions.removeIf(p -> !p.getTrips().contains(trip));
            
            model.addAttribute("excursions", excursions);
            model.addAttribute("reservation", reservation);
        } else { 
            LOGGER.warn("GET edit /edit/{} failed. Unauthorized", id);
            redAttr.addFlashAttribute("alert_danger",
                        "Unanthorized.");
            return "redirect:/auth/login";
        }
        model.addAttribute("authenticatedUser", authUser);
        return "reservation/edit";

    }
    
    /**
     * Update reservation
     * @param id id of reservation to update
     * @param model data to show
     * @param redirectAttributes redirection attributes
     * @param res http servetl response
     * @param req request
     * @return jsp page name
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String update(@PathVariable Long id, Model model, 
            RedirectAttributes redirectAttributes, HttpServletResponse res, HttpServletRequest req) {
        LOGGER.info("GET /edit/" + id);
        UserDTO authUser = (UserDTO) req.getSession().getAttribute("authenticatedUser");
        if (authUser != null) {
            String[] list = req.getParameterValues("id");
            ReservationDTO reservationDTO = reservationFacade.findReservationById(id);
           List<Long> exc = new ArrayList<>();
            
            for (String s : list ) {
                exc.add(Long.valueOf(s).longValue());
            }
            reservationDTO.setExcursions(new HashSet<>());
            try {
                reservationFacade.updateReservation(reservationDTO);
                reservationFacade.addExcrusionsToReservation(id, exc);
            } catch (TravelAgencyServiceException ex) {
                LOGGER.warn("GET update /update/{} failed.", id);
                    redirectAttributes.addFlashAttribute("alert_danger",
                        "You can't add excursion not assigned to trip.");
                return "redirect:/reservation/list/all";
            }
            

        } else { 
            LOGGER.warn("GET edit /edit/{} failed. Unauthorized", id);
            redirectAttributes.addFlashAttribute("alert_danger",
                        "Unanthorized.");
            return "redirect:/auth/login";
        }
        model.addAttribute("authenticatedUser", authUser);
        redirectAttributes.addFlashAttribute(
                    "alert_success", "Reservation with id="+ id +
                            " was updated.");
        return "redirect:/reservation/list/all";
    }
}
