package fi.muni.cz.pa165.travelagency.controllers;

import fi.muni.cz.pa165.travelagency.dto.ExcursionDTO;
import fi.muni.cz.pa165.travelagency.dto.TripCreateDTO;
import fi.muni.cz.pa165.travelagency.dto.TripDTO;
import fi.muni.cz.pa165.travelagency.dto.UserDTO;
import fi.muni.cz.pa165.travelagency.facade.ExcursionFacade;
import fi.muni.cz.pa165.travelagency.facade.ReservationFacade;
import fi.muni.cz.pa165.travelagency.facade.TripFacade;
import fi.muni.cz.pa165.travelagency.forms.TripCreateDTOValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Pavel Kotala
 */
@Controller
@RequestMapping("/trip")
public class TripController {

    public static final Logger LOGGER = LoggerFactory.getLogger(TripController.class);

    @Autowired
    private TripFacade tripFacade;

    @Autowired
    private ExcursionFacade excursionFacade;

    @Autowired
    private ReservationFacade reservationFacade;

    /**
     * Shows a list of trips with the ability to add, delete or edit.
     *
     * @param redirectAttributes redirect attributes
     * @param request request
     * @param model data to display
     * @return JSP page name
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        UserDTO authUser = (UserDTO) request.getSession().getAttribute("authenticatedUser");
        if (authUser == null || !authUser.getIsAdmin()) {
            LOGGER.warn("Failed. Unauthorized");
            redirectAttributes.addFlashAttribute("alert_danger",
                    "Unauthorized.");
            return "redirect:/auth/login";
        }
        model.addAttribute("authenticatedUser", authUser);
        model.addAttribute("trips", tripFacade.getAllTrips());
        return "trip/list";
    }

    /**
     * Deletes trip with given trip
     * @param request request
     * @param id id of trip to delete
     * @param model model
     * @param uriBuilder uri builder
     * @param redirectAttributes redirect attributes
     * @return jsp page name
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable long id, Model model, UriComponentsBuilder uriBuilder,
                         HttpServletRequest request,
                         RedirectAttributes redirectAttributes) {

        UserDTO authUser = (UserDTO) request.getSession().getAttribute("authenticatedUser");
        if (authUser == null || !authUser.getIsAdmin()) {
            LOGGER.warn("Failed. Unauthorized");
            redirectAttributes.addFlashAttribute("alert_danger",
                    "Unauthorized.");
            return "redirect:/auth/login";
        }

        TripDTO trip = tripFacade.getTripWithId(id);
        if(!reservationFacade.findReservationByTrip(id).isEmpty()) {
            redirectAttributes.addFlashAttribute("alert_danger", "Trip \"" + trip.getName() + "\" is reserved " +
                    "and cannot be deleted.");
            return "redirect:" + uriBuilder.path("/trip/list").toUriString();
        }
        model.addAttribute("authenticatedUser", authUser);
        tripFacade.deleteTrip(id);
        LOGGER.debug("delete({})", id);
        redirectAttributes.addFlashAttribute("alert_success", "Trip \"" + trip.getName() + "\" was deleted.");
        return "redirect:" + uriBuilder.path("/trip/list").toUriString();
    }

    /**
     * Shows trip with given id
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
        if (authUser == null || !authUser.getIsAdmin()) {
            LOGGER.warn("Failed. Unauthorized");
            redirectAttributes.addFlashAttribute("alert_danger",
                    "Unauthorized.");
            return "redirect:/auth/login";
        }
        model.addAttribute("authenticatedUser", authUser);
        LOGGER.debug("view({})", id);
        model.addAttribute("trip", tripFacade.getTripWithId(id));
        return "trip/view";
    }

    /**
     * Prepares an empty form.
     *
     * @param redirectAttributes redirect attributes
     * @param request request
     * @param model data to be displayed
     * @return JSP page
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newTrip(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        UserDTO authUser = (UserDTO) request.getSession().getAttribute("authenticatedUser");
        if (authUser == null || !authUser.getIsAdmin()) {
            LOGGER.warn("Failed. Unauthorized");
            redirectAttributes.addFlashAttribute("alert_danger",
                    "Unauthorized.");
            return "redirect:/auth/login";
        }
        model.addAttribute("authenticatedUser", authUser);
        LOGGER.debug("new()");
        model.addAttribute("tripCreate", new TripCreateDTO());
        return "trip/new";
    }

    /**
     * model excursions
     * @return all excursions
     */
    @ModelAttribute("excursions")
    public List<ExcursionDTO> excursions() {
        LOGGER.debug("excursions()");
        return excursionFacade.getAllExcursions();
    }

/**
 * Spring Validator added to JSR-303 Validator for this @Controller only.
 * It is useful  for custom validations that are not defined on the form bean by annotations.
 * http://docs.spring.io/spring/docs/current/spring-framework-reference/html/validation.html#validation-mvc-configuring
 */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        if (binder.getTarget() instanceof TripCreateDTO) {
            binder.addValidators(new TripCreateDTOValidator());
        }
    }

    /**
     * create method
     * @param request request
     * @param formBean form bean
     * @param bindingResult binding result
     * @param model model
     * @param redirectAttributes redirect attributes
     * @param uriBuilder uri builder
     * @return redirect link
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("tripCreate") TripCreateDTO formBean, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder,
                         HttpServletRequest request) {

        UserDTO authUser = (UserDTO) request.getSession().getAttribute("authenticatedUser");
        if (authUser == null || !authUser.getIsAdmin()) {
            LOGGER.warn("Failed. Unauthorized");
            redirectAttributes.addFlashAttribute("alert_danger",
                    "Unauthorized.");
            return "redirect:/auth/login";
        }

        LOGGER.debug("create(tripCreate={})", formBean);
        //in case of validation error forward back to the the form
        if (bindingResult.hasErrors()) {
            for (ObjectError ge : bindingResult.getGlobalErrors()) {
                LOGGER.trace("ObjectError: {}", ge);
            }
            for (FieldError fe : bindingResult.getFieldErrors()) {
                model.addAttribute(fe.getField() + "_error", true);
                LOGGER.trace("FieldError: {}", fe);
            }
            return "trip/new";
        }
        //create trip
        Long id = tripFacade.createTrip(formBean);

        List<ExcursionDTO> suitable = tripFacade.getAllSuitableExcursions(id);
        Set<Long> excs = new HashSet<>();
        for(ExcursionDTO exc : suitable) {
            excs.add(exc.getId());
        }
        tripFacade.addAllExcursions(id, excs);
        model.addAttribute("authenticatedUser", authUser);
        //report success
        redirectAttributes.addFlashAttribute("alert_success", "Trip " + id + " was created");
        return "redirect:" + uriBuilder.path("/trip/view/{id}").buildAndExpand(id).encode().toUriString();
    }

    /**
     * edit method
     * @param request request
     * @param id id
     * @param formBean form bean
     * @param bindingResult binding result
     * @param model model
     * @param redirectAttributes redirect attributes
     * @param uriBuilder uri builder
     * @return redirect link
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String edit(@PathVariable long id, @Valid @ModelAttribute("trip") TripDTO formBean,
                       BindingResult bindingResult, HttpServletRequest request,
                         Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder) {

        UserDTO authUser = (UserDTO) request.getSession().getAttribute("authenticatedUser");
        if (authUser == null || !authUser.getIsAdmin()) {
            LOGGER.warn("Failed. Unauthorized");
            redirectAttributes.addFlashAttribute("alert_danger",
                    "Unauthorized.");
            return "redirect:/auth/login";
        }

        LOGGER.debug("edit(trip={})", formBean);
        //in case of validation error forward back to the the form
        if (bindingResult.hasErrors()) {
            for (ObjectError ge : bindingResult.getGlobalErrors()) {
                LOGGER.trace("ObjectError: {}", ge);
            }
            for (FieldError fe : bindingResult.getFieldErrors()) {
                model.addAttribute(fe.getField() + "_error", true);
                LOGGER.trace("FieldError: {}", fe);
            }
            return "trip/view/{id}";
        }
        //update trip
        formBean.setId(id);
        tripFacade.updateTrip(formBean);
        model.addAttribute("authenticatedUser", authUser);
        //report success
        redirectAttributes.addFlashAttribute("alert_success", "Trip " + id + " was edited");
        return "redirect:" + uriBuilder.path("/trip/view/{id}").buildAndExpand(id).encode().toUriString();
    }

    /**
     * reset excursions method
     * @param request request
     * @param id id
     * @param formBean form bean
     * @param bindingResult binding result
     * @param model model
     * @param redirectAttributes redirect attributes
     * @param uriBuilder uri builder
     * @return redirect link
     */
    @RequestMapping(value = "/resetExcursions/{id}", method = RequestMethod.POST)
    public String resetExcursions(@PathVariable long id, @Valid @ModelAttribute("trip") TripDTO formBean,
                                  BindingResult bindingResult, HttpServletRequest request,
                                  Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder) {

        UserDTO authUser = (UserDTO) request.getSession().getAttribute("authenticatedUser");
        if (authUser == null || !authUser.getIsAdmin()) {
            LOGGER.warn("Failed. Unauthorized");
            redirectAttributes.addFlashAttribute("alert_danger",
                    "Unauthorized.");
            return "redirect:/auth/login";
        }

        List<ExcursionDTO> suitable = tripFacade.getAllSuitableExcursions(id);
        Set<Long> excs = new HashSet<>();
        for(ExcursionDTO exc : suitable) {
            excs.add(exc.getId());
        }
        tripFacade.addAllExcursions(id, excs);
        model.addAttribute("authenticatedUser", authUser);
        return "redirect:" + uriBuilder.path("/trip/view/{id}").buildAndExpand(id).encode().toUriString();
    }
}

