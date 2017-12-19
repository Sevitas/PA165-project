package fi.muni.cz.pa165.travelagency.service;

import fi.muni.cz.pa165.travelagency.dao.ExcursionDao;
import fi.muni.cz.pa165.travelagency.entity.User;
import fi.muni.cz.pa165.travelagency.entity.Excursion;
import fi.muni.cz.pa165.travelagency.entity.Reservation;
import fi.muni.cz.pa165.travelagency.entity.Trip;
import fi.muni.cz.pa165.travelagency.service.config.ServiceConfiguration;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;


import org.hibernate.service.spi.ServiceException;
import org.testng.annotations.BeforeClass;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Excursion service Tests for the Travel Angecy project.
 * 
 * @author (name = "Nermin Jukan", UCO = "<473370>")
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
public class ExcursionServiceTest extends AbstractTransactionalTestNGSpringContextTests{
    
    @Mock
    private ExcursionDao excursionDao;
    
    @Mock
    private TripService tripService;
    
    @Autowired
    @InjectMocks
    private ExcursionService excursionService;
    
    @BeforeClass
    public void setup() throws ServiceException{
        MockitoAnnotations.initMocks(this);
    }
    
    private Excursion excursionCastle;
    private Excursion excursionLake;
    private Excursion excursionHill;
    private Trip trip;
    private User user;
    private Reservation reservation;
    
    @BeforeMethod
    public void prepareTests(){
        trip = new Trip();
        reservation = new Reservation();
        user = new User();
        
        Calendar calendar =  Calendar.getInstance();
        calendar.set(2012, 4, 20);
        
        excursionCastle = new Excursion("castle", "france", 2, calendar.getTime(), BigDecimal.valueOf(250), trip);
        excursionLake = new Excursion("lake", "france", 5, calendar.getTime(), BigDecimal.valueOf(1500), trip);
        excursionHill = new Excursion("hill", "france", 5, calendar.getTime(), BigDecimal.valueOf(2000), trip);
    }
    
    @Test
    public void createExcursionTest(){
        excursionService.create(excursionCastle);
        verify(excursionDao).create(excursionCastle);
    }
    
    @Test
    public void updateExcursion(){
        excursionCastle.setPrice(BigDecimal.valueOf(244.99));
        excursionCastle.setDuration(3);
        excursionService.update(excursionCastle);
        verify(excursionDao).update(excursionCastle);
    }
    
    @Test
    public void deleteExcursion(){
        excursionService.deleteExcursion(excursionCastle);
        verify(excursionDao).delete(excursionCastle);
    }
    
    @Test
    public void findByIdTest(){
        excursionCastle.setId(1l);
        when(excursionDao.findById(excursionCastle.getId())).thenReturn(excursionCastle);
        assertEquals(excursionService.findById(excursionCastle.getId()), excursionCastle);
    }
    
    @Test
    public void findAllTest(){
        when(excursionDao.findAll()).thenReturn(new ArrayList<>());
        assertEquals(excursionDao.findAll().size(), 0);
        when(excursionDao.findAll()).thenReturn(Arrays.asList(excursionCastle));
        assertEquals(excursionService.getAllExcursions().size(), 1);
        excursionEquals(excursionService.getAllExcursions().get(0), excursionCastle);
    }
    
    @Test
    public void findByNonExistingIdTest(){
        assertEquals(excursionDao.findById(Long.MAX_VALUE), null);
    }
    
    @Test
    public void findByDestinationTest(){
        String destination = "france";
        when(excursionDao.findByDestination(destination)).thenReturn(Arrays.asList(excursionCastle));
        assertEquals(excursionService.findByDestination(destination), Arrays.asList(excursionCastle));
    }
    
    @Test
    public void findByPriceLowerThanOrEqualTest(){
        Integer price = 1500;
        when(excursionDao.findAll()).thenReturn(Arrays.asList(excursionCastle, excursionLake, excursionHill));
        assertEquals(excursionService.findByPriceLowerThanOrEqual(price).size(), 2);
        assertThat(excursionService.findByPriceLowerThanOrEqual(price)).containsOnly(excursionCastle, excursionLake);
    }
    
    @Test
    public void findByPriceHigherThanOrEqualTest(){
        Integer price = 1500;
        when(excursionDao.findAll()).thenReturn(Arrays.asList(excursionCastle, excursionLake, excursionHill));
        assertEquals(excursionService.findByPriceHigherThanOrEqual(price).size(), 2);
        assertThat(excursionService.findByPriceHigherThanOrEqual(price)).containsOnly(excursionHill, excursionLake);
    }
    
    @Test
    public void findByDurationTest(){
        Integer duration = 5;
        when(excursionDao.findAll()).thenReturn(Arrays.asList(excursionCastle, excursionLake, excursionHill));
        assertEquals(excursionService.findByDuration(duration).size(), 2);
        assertThat(excursionService.findByDuration(duration)).containsOnly(excursionHill, excursionLake);
    }
    
    private void excursionEquals(Excursion excursionOne, Excursion excursionTwo){
        assertEquals(excursionOne, excursionTwo);
        assertEquals(excursionOne.getId(), excursionTwo.getId());
        assertEquals(excursionOne.getDescription(), excursionTwo.getDescription());
        assertEquals(excursionOne.getDestination(), excursionTwo.getDestination());
        assertEquals(excursionOne.getDuration(), excursionTwo.getDuration());
        assertEquals(excursionOne.getExcursionDate(), excursionTwo.getExcursionDate());
        assertEquals(excursionOne.getPrice(), excursionTwo.getPrice());
        assertEquals(excursionOne.getTrips(), excursionTwo.getTrips());
    }
}
