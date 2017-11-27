package fi.muni.cz.pa165.travelagency.service;

import fi.muni.cz.pa165.travelagency.dao.CustomerDao;
import fi.muni.cz.pa165.travelagency.entity.Customer;
import fi.muni.cz.pa165.travelagency.entity.Excursion;
import fi.muni.cz.pa165.travelagency.entity.Reservation;
import fi.muni.cz.pa165.travelagency.entity.Trip;
import fi.muni.cz.pa165.travelagency.enums.PaymentStateType;
import fi.muni.cz.pa165.travelagency.service.CustomerService;
import fi.muni.cz.pa165.travelagency.service.ReservationService;
import fi.muni.cz.pa165.travelagency.service.config.ServiceConfiguration;
import org.junit.rules.ExpectedException;
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
public class CustomerServiceTest extends AbstractTestNGSpringContextTests {

    @Mock
    private CustomerDao customerDao;

    @Mock
    private ReservationService reservationService;

    @Autowired
    @InjectMocks
    private CustomerService customerService;

    private Customer customer, customerNew;
    private Reservation reservation, reservationNew;

    @BeforeClass
    public void setupClass() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeMethod
    public void setup() {
        Calendar cal = Calendar.getInstance();
        customer = new Customer();
        customer.setId(1L);
        customer.setEmail("email@email.com");
        customer.setIdCardNumber("idCardNumber");
        customer.setName("Name");
        customer.setSurname("Surname");
        customer.setPhoneNumber("7894556123");
        cal.set(2017, 28, 10);
        customer.setBirthDate(cal.getTime());

        customerNew = new Customer();
        customerNew.setId(2L);
        customerNew.setEmail("email2@email.com");
        customerNew.setIdCardNumber("idCardNumber2");
        customerNew.setName("Name2");
        customerNew.setSurname("Surname2");
        customerNew.setPhoneNumber("123456789");
        cal.set(2017, 10, 9);
        customerNew.setBirthDate(cal.getTime());

        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setPaymentState(PaymentStateType.Paid);
        reservation.setCustomer(customer);

        reservationNew = new Reservation();
        reservationNew.setId(1L);
        reservationNew.setPaymentState(PaymentStateType.NotPaid);
        reservationNew.setCustomer(customerNew);


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
        reservation.setReservedTrip(trip);


        customer.addReservation(reservation);
    }

    @Test
    public void createCustomer() {
        customerService.createCustomer(customer);
        verify(customerDao).create(customer);
    }

    @Test
    public void updateCustomer() {
        customer.setName("x");
        customerService.updateCustomer(customer);
        verify(customerDao, times(3)).update(customer);
    }

    @Test
    public void getTotalPriceCustomersReservationsCustomer() {
        when(reservationService.findByCustomer(customer)).thenReturn(Arrays.asList(reservation, reservationNew));
        assertEquals(customerService.getTotalPriceCustomersReservations(customer), new BigDecimal("300"),
                "Price equality, price should be ");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void getTotalPriceCustomersReservationsCustomerNull() {
        when(reservationService.findByCustomer(any())).thenReturn(null);
        customerService.getTotalPriceCustomersReservations(customer);
    }

    @Test
    public void changeCustomerOnReservationCustomer() {
        Assert.assertEquals(reservation.getCustomer().getId(), customer.getId(), "Id equality");
        customerEqualsTest(reservation.getCustomer(), customer);

        when(customerDao.update(customerNew)).thenReturn(customerNew);
        when(customerDao.update(customer)).thenReturn(customer);
        when(reservationService.updateReservation(reservation)).thenReturn(reservationNew);
        customerService.changeCustomerOnReservation(customerNew, reservation);
        Assert.assertNotEquals(reservationNew.getCustomer().getId(), customer.getId(), "Id non equality");
        Assert.assertEquals(reservationNew.getCustomer().getId(), customerNew.getId());
        customerEqualsTest(reservationNew.getCustomer(), customerNew);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void changeCustomerOnReservationCustomerNullCustomer() {
        customerService.changeCustomerOnReservation(null, reservation);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void changeCustomerOnReservationCustomerNullReservation() {
        customerService.changeCustomerOnReservation(customer, null);
    }

    @Test
    public void findAllCustomer() {
        when(customerDao.findAll()).thenReturn(new ArrayList<>());
        Assert.assertEquals(customerDao.findAll().size(), 0, "list should be empty");

        when(customerDao.findAll()).thenReturn(Arrays.asList(customer));
        List<Customer> customerList = customerService.findAll();
        Assert.assertEquals(customerList.size(), 1, "size should be 1");
        Assert.assertEquals(customerList.get(0).getId(), customer.getId(), "Id quality");
        customerEqualsTest(customerList.get(0), customer);
    }

    @Test
    public void findByEmailCustomer() {
        when(customerDao.findByEmail("email@email.com")).thenReturn(customer);
        Customer found = customerService.findByEmail("email@email.com");
        Assert.assertNotNull(found, "Element should be found");
        Assert.assertEquals(found.getId(), customer.getId(), "Id equality");
        customerEqualsTest(found, customer);

        when(customerDao.findByEmail(any())).thenReturn(null);
        Assert.assertNull(customerService.findByEmail(""));
    }

    @Test
    public void findByIdCustomer() {
        when(customerDao.findById(1L)).thenReturn(customer);
        Customer found = customerService.findById(customer.getId());
        Assert.assertNotNull(found, "Element should be found");
        Assert.assertEquals(found.getId(), customer.getId(), "Id equality");
        customerEqualsTest(found, customer);

        when(customerDao.findById(any())).thenReturn(null);
        Assert.assertNull(customerService.findById(5L));
    }

    @Test
    public void findByIdCardNumberCustomer() {
        when(customerDao.findByIdCardNumber("idCardNumber")).thenReturn(customer);
        Customer found = customerService.findByIdCardNumber("idCardNumber");
        Assert.assertNotNull(found, "Element should be found");
        Assert.assertEquals(found.getId(), customer.getId(), "Id equality");
        customerEqualsTest(found, customer);

        when(customerDao.findByIdCardNumber(any())).thenReturn(null);
        Assert.assertNull(customerService.findByIdCardNumber("null"), "returned value should be null");
    }

    @Test
    public void findByReservationCustomer() {
        when(customerDao.findByReservation(reservation)).thenReturn(customer);
        Customer found = customerService.findByReservation(reservation);
        Assert.assertNotNull(found, "Element can not be null");
        Assert.assertEquals(found.getId(), customer.getId(), "Id equality");
        customerEqualsTest(found, customer);

        when(customerDao.findByReservation(any())).thenReturn(null);
        Assert.assertNull(customerService.findByReservation(reservation), "returned value should be null");
    }

    @Test
    public void removeCustomer() {
        customerService.removeCustomer(customer);
        verify(customerDao).remove(customer);
    }

    public void customerEqualsTest(Customer customerNew, Customer customerExpected) {
        Assert.assertEquals(customerNew.getIdCardNumber(), customerExpected.getIdCardNumber(),"IdCardNumber Equality");
        Assert.assertEquals(customerNew.getPhoneNumber(), customerExpected.getPhoneNumber(), "PhoneNumber Equality");
        Assert.assertEquals(customerNew.getBirthDate(), customerExpected.getBirthDate(), "BirthDate Equality");
        Assert.assertEquals(customerNew.getEmail(), customerExpected.getEmail(), "Email Equality");
        Assert.assertEquals(customerNew.getSurname(), customerExpected.getSurname(), "Surname Equality");
        Assert.assertEquals(customerNew.getName(), customerExpected.getName(), "Name Equality");
        Assert.assertEquals(customerNew.getReservations(), customerExpected.getReservations(), "Reservations Equality");
    }

}
