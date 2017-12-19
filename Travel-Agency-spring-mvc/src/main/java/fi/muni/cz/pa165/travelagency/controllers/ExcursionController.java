package fi.muni.cz.pa165.travelagency.controllers;

import fi.muni.cz.pa165.travelagency.dto.ExcursionDTO;
import fi.muni.cz.pa165.travelagency.dto.UserDTO;
import fi.muni.cz.pa165.travelagency.facade.ExcursionFacade;
import fi.muni.cz.pa165.travelagency.facade.TripFacade;
import fi.muni.cz.pa165.travelagency.forms.ExcursionDTOValidator;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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

/**
 * Controller for Excursion.
 * 
 * @author (name = "Nermin Jukan", UCO = "<473370>")
 */
@Controller
@RequestMapping("/excursion")
public class ExcursionController {
    
    public static final Logger LOGGER = LoggerFactory.getLogger(ExcursionController.class);
    
    
    @Autowired
    private ExcursionFacade excursionFacade;
    
    @Autowired
    private TripFacade tripFacade;
    
    private final String defaultRedirect = "redirect:/excursion/list";
    
    
    /**
     * Shows a list of excursions with the ability to add, delete or edit.
     *
     * @param model data to display
     * @return JSP page name
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listAll(Model model) {
        model.addAttribute("excursions", excursionFacade.getAllExcursions());
        return "excursion/list";
    }
    
    /**
     * Deletes excursion with given trip.
     * @param id id of excursion to delete
     * @param model model
     * @param uriBuilder uri builder
     * @param redirectAttributes redirect attributes
     * @return jsp page name
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable long id, Model model, UriComponentsBuilder uriBuilder,
                         RedirectAttributes redirectAttributes) {
        
        if (excursionFacade.getByID(id) == null) {
            redirectAttributes.addFlashAttribute("alert_danger", "Excursion no. " + id + " does not exist");
            return defaultRedirect;
        }
        
        try {
            excursionFacade.deleteExcursion(excursionFacade.getByID(id));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("alert_danger", "Excursion no. " + id + " could not be deleted");
            return defaultRedirect;
        }
        LOGGER.debug("delete({})", id);
        redirectAttributes.addFlashAttribute("alert_success", "Excursion to \"" +
                excursionFacade.getByID(id).getDestination() + " - " +
                excursionFacade.getByID(id).getDescription() + "\" was deleted.");
        return "redirect:" + uriBuilder.path("/excursion/list").toUriString();
    }
    
    /**
     * Shows excursion with given id.
     * @param id id of excursion to show
     * @param model model
     * @param redirectAttributes attributes for redirecting
     * @return jsp page name
     */
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable long id, Model model, RedirectAttributes redirectAttributes) {
        LOGGER.debug("view({})", id);
        
        if (excursionFacade.getByID(id) == null) {
            redirectAttributes.addFlashAttribute("alert_danger", "Excursion no. " + id + " doesn't exist");
            return defaultRedirect;
        }
        
        model.addAttribute("excursion", excursionFacade.getByID(id));
        return "excursion/view";
    }
    
    /**
     * Prepares an empty form.
     *
     * @param model data to be displayed
     * @param req session request
     * @param redAttr reddirect attributes
     * @return JSP page
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newExcursion(Model model, HttpServletRequest req, RedirectAttributes redAttr) {
        LOGGER.debug("new()");
        
        UserDTO authUser = (UserDTO) req.getSession().getAttribute("authUser");
        
        /*if (authUser.getUserRoleType() != UserRoleType.ADMINISTRATOR) {
            redAttr.addFlashAttribute("alert_danger", "You don't have permission to create new excursion");
            return defaultRedirect;
        }*/
        
        model.addAttribute("excursionCreate", new ExcursionDTO());
        return "excursion/new";
    }
    
    /**
 * Spring Validator added to JSR-303 Validator for this @Controller only.
 * It is useful  for custom validations that are not defined on the form bean by annotations.
 * http://docs.spring.io/spring/docs/current/spring-framework-reference/html/validation.html#validation-mvc-configuring
     * @param binder binder
 */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        if (binder.getTarget() instanceof ExcursionDTO) {
            binder.addValidators(new ExcursionDTOValidator());
        }
    }
    
    /**
     * Creates a new Excursion [EDIT THIS].
     *
     * @param formBean ExcursionDTO
     * @param bindingResult results of bind
     * @param model model to transfer
     * @param redirectAttributes redirect attributes
     * @param uriBuilder uri builder
     * @return uri to redirect to
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("excursionCreate") ExcursionDTO formBean, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder) {
        LOGGER.debug("create(excursionCreate={})", formBean);
        //in case of validation error forward back to the the form
        if (bindingResult.hasErrors()) {
            for (ObjectError ge : bindingResult.getGlobalErrors()) {
                LOGGER.trace("ObjectError: {}", ge);
            }
            for (FieldError fe : bindingResult.getFieldErrors()) {
                model.addAttribute(fe.getField() + "_error", true);
                LOGGER.trace("FieldError: {}", fe);
            }
            return "excursion/new";
        }
        //create excursion
        
        
        
        Long id = excursionFacade.create(formBean);

        //report success
        redirectAttributes.addFlashAttribute("alert_success", "Excursion " + id + " was created");
        return "redirect:" + uriBuilder.path("/excursion/view/{id}").buildAndExpand(id).encode().toUriString();
    }
    
}
