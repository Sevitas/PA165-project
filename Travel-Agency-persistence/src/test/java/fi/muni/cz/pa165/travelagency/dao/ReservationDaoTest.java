package fi.muni.cz.pa165.travelagency.dao;


import fi.muni.cz.pa165.travelagency.PersistenceSampleApplicationContext;
import fi.muni.cz.pa165.travelagency.entity.User;
import fi.muni.cz.pa165.travelagency.entity.Reservation;
import fi.muni.cz.pa165.travelagency.entity.Trip;
import fi.muni.cz.pa165.travelagency.enums.PaymentStateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolationException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by martin on 24.10.2017.
 * @author Martin Sevcik <422365>
 */
@ContextConfiguration(classes=PersistenceSampleApplicationContext.class)
@TestExecutionListeners(TransactionalTestExecutionListener.class)
@Transactional
public class ReservationDaoTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private TripDao tripDao;

    @Autowired
    private UserDao userDao;

    private User user;
    private Trip trip;
    private Reservation reservation;

    @BeforeMethod
    public void setup(){
        user = new User();
        Calendar cal = Calendar.getInstance();
        cal.set(2017, 11, 10);
        user.setBirthDate(cal.getTime());
        user.setEmail("name@name.com");
        user.setIdCardNumber("idCard");
        user.setSurname("Surname");
        user.setPasswordHash("passwordHash");
        userDao.create(user);

        trip = new Trip();
        trip.setName("Trip");
        tripDao.create(trip);

        reservation = new Reservation();
        reservation.setPaymentState(PaymentStateType.NotPaid);
        reservation.setCreated(cal.getTime());
        reservation.setUser(user);
        reservation.setTrip(trip);
    }

    private void assertReservationsEqual(Reservation compared){
        Assert.assertEquals(compared.getCreated(), this.reservation.getCreated());
        Assert.assertEquals(compared.getUser(), this.reservation.getUser());
        Assert.assertEquals(compared.getId(), this.reservation.getId());
        Assert.assertEquals(compared.getPaymentState(), this.reservation.getPaymentState());
        Assert.assertEquals(compared.getExcursions(), this.reservation.getExcursions());
        Assert.assertEquals(compared.getTrip(), this.reservation.getTrip());
    }

    @Test
    public void createTest(){
        reservationDao.create(reservation);

        Assert.assertEquals(reservationDao.findAll().size(), 1);
        this.assertReservationsEqual(reservationDao.findAll().get(0));
    }

    @Test(expectedExceptions=ConstraintViolationException.class)
    public void createNullTest(){
        Reservation res = new Reservation();
        reservationDao.create(res);
    }

    @Test
    public void findAllTest(){
        List<Reservation> list = reservationDao.findAll();
        Assert.assertEquals(list.size(), 0);

        reservationDao.create(reservation);

        list = reservationDao.findAll();
        Assert.assertEquals(list.size(), 1);
        this.assertReservationsEqual(reservationDao.findAll().get(0));
    }

    @Test
    public void findByUserTest(){
        reservationDao.create(reservation);
        List<Reservation> list = reservationDao.findByUser(user);

        Assert.assertEquals(list.size(), 1);
        Assert.assertEquals(list.get(0), reservation);
        Assert.assertEquals(list.get(0).getUser(), user);
    }

    @Test
    public void findByUserNoResultTest(){
        User user2 = new User();
        Calendar cal = Calendar.getInstance();
        cal.set(2016, 9, 9);
        user2.setBirthDate(cal.getTime());
        user2.setEmail("name2@name.com");
        user2.setIdCardNumber("idCard2");
        user2.setSurname("Surname2");
        user2.setPasswordHash("passwordHash2");
        userDao.create(user2);

        reservation.setUser(user2);
        reservationDao.create(reservation);
        List<Reservation> list = reservationDao.findByUser(user);

        Assert.assertEquals(list.size(), 0);
    }

    @Test
    public void findByIdTest(){
        reservationDao.create(reservation);
        Reservation found = reservationDao.findById(reservation.getId());

        Assert.assertNotNull(found);
        Assert.assertEquals(found, reservation);
    }

    @Test
    public void findByIdNullResultTest(){
        reservationDao.create(reservation);
        Reservation found = reservationDao.findById(-1L);

        Assert.assertNull(found);
    }

    @Test
    public void removeTest(){
        reservationDao.create(reservation);
        Assert.assertFalse(reservationDao.findAll().isEmpty());

        reservationDao.remove(reservation);
        Assert.assertTrue(reservationDao.findAll().isEmpty());
    }

    @Test
    public void testFindByTrip() {
        reservationDao.create(reservation);
        
        List<Reservation> resList = reservationDao.findByTrip(reservation.getTrip());
        Assert.assertTrue(resList.size() == 1);
        Assert.assertEquals(resList.get(0), reservation);
    }
    
    @Test
    public void getReservationsCreatedBetweenTest(){
        Calendar cal = Calendar.getInstance();

        User user2 = new User();
        cal.set(2016, 9, 9);
        user2.setBirthDate(cal.getTime());
        user2.setEmail("name2@name.com");
        user2.setIdCardNumber("idCard2");
        user2.setSurname("Surname3");
        user2.setPasswordHash("passwordHash2");
        userDao.create(user2);

        User user3 = new User();
        cal.set(2016, 9, 3);
        user3.setBirthDate(cal.getTime());
        user3.setEmail("name3@name.com");
        user3.setIdCardNumber("idCard3");
        user3.setSurname("Surname3");
        user3.setPasswordHash("passwordHash3");
        userDao.create(user3);

        Trip trip2 = new Trip();
        trip2.setName("Trip2");
        tripDao.create(trip2);

        Trip trip3 = new Trip();
        trip3.setName("Trip3");
        tripDao.create(trip3);

        Reservation reservation2 = new Reservation();
        reservation2.setPaymentState(PaymentStateType.NotPaid);
        cal.set(2017, 10, 2);
        reservation2.setCreated(cal.getTime());
        reservation2.setUser(user2);
        reservation2.setTrip(trip2);
        reservationDao.create(reservation2);

        Reservation reservation3 = new Reservation();
        reservation3.setPaymentState(PaymentStateType.NotPaid);
        cal.set(2017, 10, 6);
        reservation3.setCreated(cal.getTime());
        reservation3.setUser(user3);
        reservation3.setTrip(trip3);
        reservationDao.create(reservation3);

        Calendar calStart = Calendar.getInstance();
        calStart.set(2017, 10, 1);
        Calendar calEnd = Calendar.getInstance();
        calEnd.set(2017, 10, 10);

        List<Reservation> list  = reservationDao.getReservationsCreatedBetween(calStart.getTime(), calEnd.getTime());
        Assert.assertEquals(list.size(), 2);
        Assert.assertTrue(list.contains(reservation2));
        Assert.assertTrue(list.contains(reservation3));

        calStart.set(2017, 10, 3);
        list = reservationDao.getReservationsCreatedBetween(calStart.getTime(), calEnd.getTime());
        Assert.assertEquals(list.size(), 1);
        Assert.assertTrue(list.contains(reservation3));
    }

    @Test
    public void getReservationsCreatedBetweenEmptyTest(){


        //reservationDao.create(reservation);

        Calendar calStart = Calendar.getInstance();
        calStart.set(2017, 10, 1);
        Calendar calEnd = Calendar.getInstance();
        calEnd.set(2017, 10, 10);

        Assert.assertTrue(reservationDao.getReservationsCreatedBetween(calStart.getTime(), calEnd.getTime()).isEmpty());
    }
}
