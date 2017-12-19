/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.muni.cz.pa165.travelagency.forms;

import fi.muni.cz.pa165.travelagency.dto.ExcursionDTO;
import java.math.BigDecimal;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator for Excursion.
 * 
 * @author (name = "Nermin Jukan", UCO = "<473370>")
 */
public class ExcursionDTOValidator implements Validator{

    @Override
    public boolean supports(Class<?> type) {
        return ExcursionDTO.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ExcursionDTO excursionDTO = (ExcursionDTO) o;
        
        if (excursionDTO.getDuration().compareTo(1) < -1) {
            errors.rejectValue("duration", "ExcursionDTOValidator.duration.too.short");
        }

        if (excursionDTO.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            errors.rejectValue("price", "TripCreateDTOValidator.price.negative");
        }
    }
    
}
