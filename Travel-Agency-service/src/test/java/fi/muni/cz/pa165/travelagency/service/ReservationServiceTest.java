package fi.muni.cz.pa165.travelagency.service;

import fi.muni.cz.pa165.travelagency.dao.ReservationDao;
import fi.muni.cz.pa165.travelagency.entity.User;
import fi.muni.cz.pa165.travelagency.entity.Excursion;
import fi.muni.cz.pa165.travelagency.entity.Reservation;
import fi.muni.cz.pa165.travelagency.entity.Trip;
import fi.muni.cz.pa165.travelagency.enums.PaymentStateType;
import fi.muni.cz.pa165.travelagency.exceptions.TravelAgencyServiceException;

import fi.muni.cz.pa165.travelagency.service.config.ServiceConfiguration;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.Date;
import org.hibernate.service.spi.ServiceException;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Radoslav Micko <445611>
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
public class ReservationServiceTest extends AbstractTransactionalTestNGSpringContextTests {
    
    @Mock
    private ReservationDao reservationDao;
    
    @Mock
    private TripService tripService;
    
    @Autowired
    @InjectMocks
    private ReservationService reservationService;
    
    @BeforeClass
    public void setUp() throws ServiceException {
        MockitoAnnotations.initMocks(this);
    }
    
    private Reservation reservation;
    private Trip trip;
    private Excursion excursion;
    private User user;
    
    @BeforeMethod
    public void prepareTest() { 
        trip = newTrip("1");
        user = newUser("1");
        excursion = newExcursion("1");
        
        reservation = new Reservation();
        trip.addExcursion(excursion);
        reservation.setCreated(Date.valueOf("2014-1-1"));
        reservation.setUser(user);
        reservation.setTrip(trip);
        reservation.addReservedExcursion(excursion);
    }
    
    @Test
    public void updateReservationTest() {
        reservation.setPaymentState(PaymentStateType.Paid);
        reservation.setCreated(Date.valueOf("2014-2-2"));
        reservationService.updateReservation(reservation);
        verify(reservationDao).update(reservation);
    }
    
    @Test
    public void removeReservationTest() {
        reservationService.removeReservation(reservation);
        verify(reservationDao).remove(reservation);
    }
    
    
    @Test(expectedExceptions = TravelAgencyServiceException.class)
    public void addNotInTripExcursionTest() {
        Excursion ex = newExcursion("2");
        when(reservationDao.findById(reservation.getId())).thenReturn(reservation);
        reservationService.addExcursionToReservation(reservation, ex);
        
    }
    
    
    @Test
    public void addInTripExcursionTest() {
        when(reservationDao.findById(reservation.getId())).thenReturn(reservation);
        reservationService.addExcursionToReservation(reservation, excursion);
        verify(reservationDao).update(reservation);
    }
    
    @Test
    public void addInTripExcursionsTest() {
        when(reservationDao.findById(reservation.getId())).thenReturn(reservation);
        reservationService.addExcrusionsToReservation(reservation, 
                Arrays.asList(excursion));
        assertEquals(reservation.getExcursions().size(), 1);
        
    }
    
    @Test(expectedExceptions = TravelAgencyServiceException.class)
    public void addNotInTripExcursionsTest() {
        Excursion ex = newExcursion("2");
        List<Excursion> list = new ArrayList<>();
        list.add(ex);
        reservationService.addExcrusionsToReservation(reservation, list);
        verify(reservationDao).update(reservation);
    }
    
    @Test
    public void createReservationTest() {
        when(
            tripService.findTripWithId(reservation.getTrip().getId())
        ).thenReturn(trip);
        reservationService.createReservation(reservation);
        assertTrue(trip.getAvailableSpots() == 9);
        verify(reservationDao).create(reservation);
    }
    
    @Test(expectedExceptions = TravelAgencyServiceException.class)
    public void createReservationForFullTripTest() {
        trip.setAvailableSpots(0);
        when(
            tripService.findTripWithId(reservation.getTrip().getId())
        ).thenReturn(trip);
        reservationService.createReservation(reservation);
        verify(reservationDao).create(reservation);
    }
    
    @Test
    public void findAllTest() {
        when(reservationDao.findAll()).thenReturn(new ArrayList<>());
        assertEquals(reservationDao.findAll().size(), 0);
        when(reservationDao.findAll()).thenReturn(Arrays.asList(reservation));
        assertEquals(reservationService.findAll().size(), 1);
        reservationEquals(reservationDao.findAll().get(0), reservation);
    }
    
    @Test
    public void findByIdTest() {
        reservation.setId(1l);
        when(reservationDao.findById(reservation.getId())).thenReturn(reservation);
        reservationEquals(reservationService.findById(reservation.getId()), reservation);
    }
    
    @Test
    public void findByNotexistingIdTest() {
        assertEquals(reservationDao.findById(Long.MAX_VALUE), null);
    }
    
    @Test
    public void findByUserTest() {
        when(reservationDao.findByUser(user)
        ).thenReturn(Arrays.asList(reservation));
        assertEquals(reservationService.findByUser(user), Arrays.asList(reservation));
    }
    
    @Test
    public void findByUserWithNoReservationTest() {
        User c = newUser("2");
        when(reservationDao.findByUser(c)
        ).thenReturn(new ArrayList<>());
        assertEquals(reservationService.findByUser(c).size(), 0);
    }
    
    @Test
    public void findByTripTest() {
        when(reservationDao.findByTrip(trip)
        ).thenReturn(Arrays.asList(reservation));
        assertEquals(reservationService.findByTrip(trip), Arrays.asList(reservation));
    }
    
    @Test
    public void getTotalPriceTest() {
        when(reservationDao.findById(reservation.getId())).thenReturn(reservation);
        assertEquals(reservationService.getTotalPrice(reservation), new BigDecimal("2000"));
    }
    
    @Test
    public void getReservationsCreatedBetweenTest() {
        Date start = Date.valueOf("2014-1-1");
        Date end = Date.valueOf("2014-1-2");
        when(reservationDao.getReservationsCreatedBetween(start, end))
                .thenReturn(Arrays.asList(reservation));
        reservationEquals(reservationService.getReservationsCreatedBetween(start, end)
                .get(0), reservation);
    }
    
    @Test(expectedExceptions = TravelAgencyServiceException.class)
    public void wrongOrderDatesTest() {
        Date start = Date.valueOf("2014-1-2");
        Date end = Date.valueOf("2014-1-1");
        reservationService.getReservationsCreatedBetween(start, end);
    }
    
    @Test
    public void setPaidStateTest() {
        when(reservationDao.findById(reservation.getId())).thenReturn(reservation);
        reservationService.setPaidState(reservation);
        assertEquals(reservation.getPaymentState(), PaymentStateType.Paid);
    }
    
    @Test
    public void setNotPaidStateTest() {
        when(reservationDao.findById(reservation.getId())).thenReturn(reservation);
        reservationService.setNotPaidState(reservation);
        assertEquals(reservation.getPaymentState(), PaymentStateType.NotPaid);
    }
    
    @Test
    public void findAllSortedByDateTest() {
        Reservation r = new Reservation();
        r.setCreated(Date.valueOf("2014-1-2"));
        when(reservationDao.findAll()).thenReturn(Arrays.asList(reservation, r));
        assertEquals(reservationService.findAllSortedByDate().get(0), reservation);
        assertEquals(reservationService.findAllSortedByDate().get(1), r);
        
        r.setCreated(Date.valueOf("2013-12-31"));
        when(reservationDao.findAll()).thenReturn(Arrays.asList(reservation, r));
        assertEquals(reservationService.findAllSortedByDate().get(0), r);
        assertEquals(reservationService.findAllSortedByDate().get(1), reservation);
    }
    
    @Test
    public void findAllNotPaid() {
        Reservation r = new Reservation();
        r.setPaymentState(PaymentStateType.Paid);
        when(reservationDao.findAll()).thenReturn(Arrays.asList(reservation, r));
        assertEquals(reservationService.findAllNotPaid().size(), 1);
        reservationEquals(reservationService.findAllNotPaid().get(0), reservation);
    }
    
    private void reservationEquals(Reservation reservation1, Reservation reservation2){
        assertEquals(reservation1, reservation2);
        assertEquals(reservation1.getId(), reservation2.getId());
        assertEquals(reservation1.getCreated(), reservation2.getCreated());
        assertEquals(reservation1.getUser(), reservation2.getUser());
        assertEquals(reservation1.getExcursions(), reservation2.getExcursions());
        assertEquals(reservation1.getTrip(), reservation2.getTrip());
        assertEquals(reservation1.getPaymentState(), reservation2.getPaymentState());
    }
    
    private Trip newTrip(String s) {
        Trip t = new Trip();
        t.setAvailableSpots(10);
        t.setDateFrom(Date.valueOf("2015-1-1"));
        t.setDateTo(Date.valueOf("2015-1-2"));
        t.setDestination("Destination");
        t.setName("Trip - " + s);
        t.setPrice(new BigDecimal("1000"));
        return t;
    }
    
    private User newUser(String s) {
        User c = new User();
        c.setSurname("Name " + s);
        c.setEmail(s + "@email.com");
        c.setBirthDate(Date.valueOf("2000-1-1"));
        c.setIdCardNumber("Id Card " + s);
        return c;
    }
    
    private Excursion newExcursion(String s) {
        Excursion e = new Excursion();
        e.setDescription("Description " + s);
        e.setDestination("Destination " + s);
        e.setDuration(1);
        e.setExcursionDate(Date.valueOf("2015-1-1"));
        e.setPrice(new BigDecimal("1000"));
        return e;
    }
    
}
