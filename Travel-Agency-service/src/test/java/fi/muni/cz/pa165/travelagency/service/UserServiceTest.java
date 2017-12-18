package fi.muni.cz.pa165.travelagency.service;

import fi.muni.cz.pa165.travelagency.dao.UserDao;
import fi.muni.cz.pa165.travelagency.entity.User;
import fi.muni.cz.pa165.travelagency.entity.Excursion;
import fi.muni.cz.pa165.travelagency.entity.Reservation;
import fi.muni.cz.pa165.travelagency.entity.Trip;
import fi.muni.cz.pa165.travelagency.enums.PaymentStateType;
import fi.muni.cz.pa165.travelagency.service.config.ServiceConfiguration;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Martin Sevcik
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
public class UserServiceTest extends AbstractTestNGSpringContextTests {

    @Mock
    private UserDao userDao;

    @Mock
    private ReservationService reservationService;

    @Autowired
    @InjectMocks
    private UserService userService;

    private User user, userNew;
    private Reservation reservation, reservationNew;

    @BeforeClass
    public void setupClass() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeMethod
    public void setup() {
        Calendar cal = Calendar.getInstance();
        user = new User();
        user.setId(1L);
        user.setEmail("email@email.com");
        user.setIdCardNumber("idCardNumber");
        user.setName("Name");
        user.setSurname("Surname");
        user.setPhoneNumber("7894556123");
        user.setPasswordHash("2:56156");
        cal.set(2017, 28, 10);
        user.setBirthDate(cal.getTime());

        userNew = new User();
        userNew.setId(2L);
        userNew.setEmail("email2@email.com");
        userNew.setIdCardNumber("idCardNumber2");
        userNew.setName("Name2");
        userNew.setSurname("Surname2");
        userNew.setPhoneNumber("123456789");
        cal.set(2017, 10, 9);
        userNew.setBirthDate(cal.getTime());

        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setPaymentState(PaymentStateType.Paid);
        reservation.setUser(user);

        reservationNew = new Reservation();
        reservationNew.setId(1L);
        reservationNew.setPaymentState(PaymentStateType.NotPaid);
        reservationNew.setUser(userNew);


        Trip trip = new Trip();
        trip.setName("tripName");
        trip.setAvailableSpots(2);
        cal.set(2017, 10, 10);
        trip.setDateFrom(cal.getTime());
        cal.set(2017, 10, 20);
        trip.setDateTo(cal.getTime());
        trip.setDestination("tripDestination");
        trip.setPrice(new BigDecimal("150"));

        for (int i = 0; i < 3; i++) {
            Excursion excursion = new Excursion();
            excursion.setDescription("description" + i);
            excursion.setDestination("excursionDestination" + i);
            excursion.setDuration(2 + i);
            excursion.setPrice(new BigDecimal("50"));
            trip.addExcursion(excursion);
            reservation.addReservedExcursion(excursion);
        }
        reservation.setTrip(trip);


        user.addReservation(reservation);
    }

    @Test
    public void createUserTest() {
        userService.registerUser(user, "pw");
        verify(userDao).create(user);
    }

    @Test
    public void updateUserTest() {
        user.setName("x");
        userService.updateUser(user);
        verify(userDao, times(3)).update(user);
    }

    @Test
    public void getTotalPriceUsersReservationsUserTest() {
        when(reservationService.findByUser(user)).thenReturn(Arrays.asList(reservation, reservationNew));
        assertEquals(userService.getTotalPriceUsersReservations(user), new BigDecimal("300"),
                "Price equality, price should be ");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void getTotalPriceUsersReservationsUserNullTest() {
        when(reservationService.findByUser(any())).thenReturn(null);
        userService.getTotalPriceUsersReservations(user);
    }

    @Test
    public void changeUserOnReservationUserTest() {
        Assert.assertEquals(reservation.getUser().getId(), user.getId(), "Id equality");
        userEqualsTest(reservation.getUser(), user);

        when(userDao.update(userNew)).thenReturn(userNew);
        when(userDao.update(user)).thenReturn(user);
        when(reservationService.updateReservation(reservation)).thenReturn(reservationNew);
        userService.changeUserOnReservation(userNew, reservation);
        Assert.assertNotEquals(reservationNew.getUser().getId(), user.getId(), "Id non equality");
        Assert.assertEquals(reservationNew.getUser().getId(), userNew.getId());
        userEqualsTest(reservationNew.getUser(), userNew);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void changeUserOnReservationUserNullUserTest() {
        userService.changeUserOnReservation(null, reservation);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void changeUserOnReservationUserNullReservationTest() {
        userService.changeUserOnReservation(user, null);
    }

    @Test
    public void findAllUserTest() {
        when(userDao.findAll()).thenReturn(new ArrayList<>());
        Assert.assertEquals(userDao.findAll().size(), 0, "list should be empty");

        when(userDao.findAll()).thenReturn(Arrays.asList(user));
        List<User> userList = userService.findAll();
        Assert.assertEquals(userList.size(), 1, "size should be 1");
        Assert.assertEquals(userList.get(0).getId(), user.getId(), "Id quality");
        userEqualsTest(userList.get(0), user);
    }

    @Test
    public void findByEmailUserTest() {
        when(userDao.findByEmail("email@email.com")).thenReturn(user);
        User found = userService.findByEmail("email@email.com");
        Assert.assertNotNull(found, "Element should be found");
        Assert.assertEquals(found.getId(), user.getId(), "Id equality");
        userEqualsTest(found, user);

        when(userDao.findByEmail(any())).thenReturn(null);
        Assert.assertNull(userService.findByEmail(""));
    }

    @Test
    public void findByIdUserTest() {
        when(userDao.findById(1L)).thenReturn(user);
        User found = userService.findById(user.getId());
        Assert.assertNotNull(found, "Element should be found");
        Assert.assertEquals(found.getId(), user.getId(), "Id equality");
        userEqualsTest(found, user);

        when(userDao.findById(any())).thenReturn(null);
        Assert.assertNull(userService.findById(5L));
    }

    @Test
    public void findByIdCardNumberUserTest() {
        when(userDao.findByIdCardNumber("idCardNumber")).thenReturn(user);
        User found = userService.findByIdCardNumber("idCardNumber");
        Assert.assertNotNull(found, "Element should be found");
        Assert.assertEquals(found.getId(), user.getId(), "Id equality");
        userEqualsTest(found, user);

        when(userDao.findByIdCardNumber(any())).thenReturn(null);
        Assert.assertNull(userService.findByIdCardNumber("null"), "returned value should be null");
    }

    @Test
    public void findByReservationUserTest() {
        when(userDao.findByReservation(reservation)).thenReturn(user);
        User found = userService.findByReservation(reservation);
        Assert.assertNotNull(found, "Element can not be null");
        Assert.assertEquals(found.getId(), user.getId(), "Id equality");
        userEqualsTest(found, user);

        when(userDao.findByReservation(any())).thenReturn(null);
        Assert.assertNull(userService.findByReservation(reservation), "returned value should be null");
    }

    @Test
    public void removeUserTest() {
        userService.removeUser(user);
        verify(userDao).remove(user);
    }

    public void userEqualsTest(User userNew, User userExpected) {
        Assert.assertEquals(userNew.getIdCardNumber(), userExpected.getIdCardNumber(),"IdCardNumber Equality");
        Assert.assertEquals(userNew.getPhoneNumber(), userExpected.getPhoneNumber(), "PhoneNumber Equality");
        Assert.assertEquals(userNew.getBirthDate(), userExpected.getBirthDate(), "BirthDate Equality");
        Assert.assertEquals(userNew.getEmail(), userExpected.getEmail(), "Email Equality");
        Assert.assertEquals(userNew.getSurname(), userExpected.getSurname(), "Surname Equality");
        Assert.assertEquals(userNew.getName(), userExpected.getName(), "Name Equality");
        Assert.assertEquals(userNew.getReservations(), userExpected.getReservations(), "Reservations Equality");
        Assert.assertEquals(userNew.getPasswordHash(), userExpected.getPasswordHash(), "Password hash Equality");
    }
    
}
