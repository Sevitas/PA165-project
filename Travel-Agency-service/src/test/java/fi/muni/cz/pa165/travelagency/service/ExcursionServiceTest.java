package fi.muni.cz.pa165.travelagency.service;

import fi.muni.cz.pa165.travelagency.dao.ExcursionDao;
import fi.muni.cz.pa165.travelagency.entity.Customer;
import fi.muni.cz.pa165.travelagency.entity.Excursion;
import fi.muni.cz.pa165.travelagency.entity.Reservation;
import fi.muni.cz.pa165.travelagency.entity.Trip;
import fi.muni.cz.pa165.travelagency.service.ExcursionService;
import fi.muni.cz.pa165.travelagency.service.config.ServiceConfiguration;
import java.math.BigDecimal;
import java.util.Calendar;

import org.hibernate.service.spi.ServiceException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;

/**
 * Excursion service Tests for the Travel Angecy project.
 * 
 * @author (name = "Nermin Jukan", UCO = "<473370>")
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
public class ExcursionServiceTest extends AbstractTransactionalTestNGSpringContextTests{
    
    @Mock
    private ExcursionDao excursionDao;
    
    @Autowired
    @InjectMocks
    private ExcursionService excursionService;
    
    @BeforeClass
    public void setup() throws ServiceException{
        MockitoAnnotations.initMocks(this);
    }
    
    private Excursion excursion;
    
    @BeforeMethod
    public void prepareTest(){
        Trip trip = new Trip();
        Reservation reservation = new Reservation();
        Customer customer = new Customer();
        
        Calendar calendar =  Calendar.getInstance();
        calendar.set(2012, 4, 20);
        
        excursion = new Excursion("castle", "france", 2, calendar.getTime(), BigDecimal.valueOf(1999), trip);
    }
    
    @Test
    public void createExcursionTest(){
        excursionService.create(excursion);
        verify(excursionDao).create(excursion);
    }
}
