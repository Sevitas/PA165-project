package fi.muni.cz.pa165.travelagency.forms;

import fi.muni.cz.pa165.travelagency.dto.TripCreateDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

/**
 * @author Pavel Kotala
 */
public class TripCreateDTOValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return TripCreateDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TripCreateDTO productCreateDTO = (TripCreateDTO) target;

        if(productCreateDTO.getDateFrom() == null) {
            errors.rejectValue("dateFrom", "TripCreateDTOValidator.dateFrom.null");
        }

        if(productCreateDTO.getDateTo() == null) {
            errors.rejectValue("dateTo", "TripCreateDTOValidator.dateTo.null");
        }

        if (productCreateDTO.getDateFrom() != null &&
                productCreateDTO.getDateTo() != null &&
                productCreateDTO.getDateTo().compareTo(productCreateDTO.getDateFrom()) < 0) {
            errors.rejectValue("dateTo", "TripCreateDTOValidator.dateTo.before.dateFrom");
        }

        if (productCreateDTO.getAvailableSpots() < 0) {
            errors.rejectValue("availableSpots", "TripCreateDTOValidator.availableSpots.negative");
        }

        if(productCreateDTO.getPrice() == null) {
            errors.rejectValue("price", "TripCreateDTOValidator.price.null");
        }

        if (productCreateDTO.getPrice() != null &&
                productCreateDTO.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            errors.rejectValue("price", "TripCreateDTOValidator.price.negative");
        }
    }
}
