package fi.muni.cz.pa165.travelagency.service.service;

import fi.muni.cz.pa165.travelagency.dao.ReservationDao;
import fi.muni.cz.pa165.travelagency.entity.Customer;
import fi.muni.cz.pa165.travelagency.entity.Excursion;
import fi.muni.cz.pa165.travelagency.entity.Reservation;
import fi.muni.cz.pa165.travelagency.entity.Trip;
import fi.muni.cz.pa165.travelagency.service.ReservationService;
import fi.muni.cz.pa165.travelagency.service.config.ServiceConfiguration;
import java.math.BigDecimal;
import java.sql.Date;
import org.hibernate.service.spi.ServiceException;
import org.junit.BeforeClass;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;

/**
 * @author Radoslav Micko <445611>
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
public class ReservationServiceTest extends AbstractTransactionalTestNGSpringContextTests {
    
    @Mock
    private ReservationDao reservationDao;
    
    @Autowired
    @InjectMocks
    private ReservationService reservationService;
    
    @BeforeClass
    public void setUp() throws ServiceException {
        MockitoAnnotations.initMocks(this);
    }
    
    private Reservation reservation;
    
    @BeforeMethod
    public void prepareTest() { 
        Trip trip = newTrip("1");
        Customer customer = newCustomer("1");
        Excursion excursion = newExcursion("1");
        
        reservation = new Reservation();
        reservation.setCreated(Date.valueOf("2014-1-1"));
        reservation.setCustomer(customer);
        reservation.setReservedTrip(trip);
        reservation.addReservedExcursion(excursion);
    }
    
    
    public void createReservationTest() {
        reservationService.createReservation(reservation);
        verify(reservationDao).create(reservation);
    }
    
    
    private Trip newTrip(String s) {
        Trip trip = new Trip();
        trip.setAvailableSpots(10);
        trip.setDateFrom(Date.valueOf("2015-1-1"));
        trip.setDateTo(Date.valueOf("2015-1-2"));
        trip.setDestination("Destination");
        trip.setName("Trip - " + s);
        trip.setPrice(new BigDecimal("1000"));
        return trip;
    }
    
    private Customer newCustomer(String s) {
        Customer customer = new Customer();
        customer.setSurname("Name " + s);
        customer.setEmail(s + "@email.com");
        customer.setBirthDate(Date.valueOf("2000-1-1"));
        customer.setIdCardNumber("Id Card " + s);
        return customer;
    }
    
    private Excursion newExcursion(String s) {
        Excursion excursion = new Excursion();
        excursion.setDescription("Description " + s);
        excursion.setDestination("Destination " + s);
        excursion.setDuration(1);
        excursion.setExcursionDate(Date.valueOf("2015-1-1"));
        excursion.setPrice(new BigDecimal("1000"));
        return excursion;
    }
    
}
