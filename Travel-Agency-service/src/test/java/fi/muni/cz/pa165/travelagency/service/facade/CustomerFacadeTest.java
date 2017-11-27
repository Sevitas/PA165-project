package fi.muni.cz.pa165.travelagency.service.facade;

import fi.muni.cz.pa165.travelagency.dao.CustomerDao;
import fi.muni.cz.pa165.travelagency.dto.CustomerDTO;
import fi.muni.cz.pa165.travelagency.dto.ReservationDTO;
import fi.muni.cz.pa165.travelagency.dto.TripDTO;
import fi.muni.cz.pa165.travelagency.entity.Customer;
import fi.muni.cz.pa165.travelagency.entity.Excursion;
import fi.muni.cz.pa165.travelagency.entity.Reservation;
import fi.muni.cz.pa165.travelagency.entity.Trip;
import fi.muni.cz.pa165.travelagency.enums.PaymentStateType;
import fi.muni.cz.pa165.travelagency.facade.CustomerFacade;
import fi.muni.cz.pa165.travelagency.service.BeanMappingService;
import fi.muni.cz.pa165.travelagency.service.CustomerService;
import fi.muni.cz.pa165.travelagency.service.config.ServiceConfiguration;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = ServiceConfiguration.class)
@Transactional
public class CustomerFacadeTest extends AbstractTestNGSpringContextTests {

    @Mock
    private BeanMappingService beanMappingService;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerFacade customerFacade = new CustomerFacadeImpl();

    @BeforeClass
    public void setupClass() {
        MockitoAnnotations.initMocks(this);
    }

    private Customer customer, customerNew;
    private Reservation reservation, reservationNew;
    private ReservationDTO reservationDTO, reservationNewDTO;
    private CustomerDTO customerDTO, customerNewDTO;
    private List<CustomerDTO> customerDTOList;
    private List<Customer> customerList;


    @BeforeMethod
    public void setup() {
        Calendar cal = Calendar.getInstance();
        customer = new Customer();
        customer.setEmail("email@email.com");
        customer.setIdCardNumber("idCardNumber");
        customer.setName("Name");
        customer.setSurname("Surname");
        customer.setPhoneNumber("7894556123");
        cal.set(2017, 28, 10);
        customer.setBirthDate(cal.getTime());

        customerDTO = new CustomerDTO();
        customerDTO.setEmail("email@email.com");
        customerDTO.setIdCardNumber("idCardNumber");
        customerDTO.setName("Name");
        customerDTO.setSurname("Surname");
        customerDTO.setPhoneNumber("7894556123");
        cal.set(2017, 28, 10);
        customerDTO.setBirthDate(cal.getTime());

        customerNew = new Customer();
        customerNew.setEmail("email2@email.com");
        customerNew.setIdCardNumber("idCardNumber2");
        customerNew.setName("Name2");
        customerNew.setSurname("Surname2");
        customerNew.setPhoneNumber("123456789");
        cal.set(2017, 10, 9);
        customerNew.setBirthDate(cal.getTime());

        customerNewDTO = new CustomerDTO();
        customerNewDTO.setEmail("email2@email.com");
        customerNewDTO.setIdCardNumber("idCardNumber2");
        customerNewDTO.setName("Name2");
        customerNewDTO.setSurname("Surname2");
        customerNewDTO.setPhoneNumber("123456789");
        cal.set(2017, 10, 9);
        customerNewDTO.setBirthDate(cal.getTime());

        reservation = new Reservation();
        reservation.setPaymentState(PaymentStateType.Paid);
        reservation.setCustomer(customer);
        reservation.setCreated(cal.getTime());

        reservationDTO = new ReservationDTO();
        reservationDTO.setCustomer(customerDTO);
        reservationDTO.setCreated(cal.getTime());

        reservationNew = new Reservation();
        reservationNew.setPaymentState(PaymentStateType.NotPaid);
        reservationNew.setCustomer(customerNew);
        reservationNew.setCreated(cal.getTime());

        reservationNewDTO = new ReservationDTO();
        reservationNewDTO.setCustomer(customerNewDTO);
        reservationNewDTO.setCreated(cal.getTime());


        Set<ReservationDTO> listReservationsDTO = new HashSet<>();
        listReservationsDTO.add(reservationDTO);
        customerDTO.setReservations(listReservationsDTO);

        Set<Reservation> listReservations = new HashSet<>();
        listReservations.add(reservation);
        customer.setReservation(listReservations);

        Set<ReservationDTO> listReservationsNewDTO = new HashSet<>();
        listReservationsDTO.add(reservationNewDTO);
        customerNewDTO.setReservations(listReservationsNewDTO);

        Set<Reservation> listReservationsNew = new HashSet<>();
        listReservationsNew.add(reservationNew);
        customerNew.setReservation(listReservationsNew);

        Trip trip = new Trip();
        trip.setName("tripName");
        trip.setAvailableSpots(2);
        cal.set(2017, 10, 10);
        trip.setDateFrom(cal.getTime());
        cal.set(2017, 10, 20);
        trip.setDateTo(cal.getTime());
        trip.setDestination("tripDestination");
        trip.setPrice(new BigDecimal("150"));
        reservation.setReservedTrip(trip);
        reservationNew.setReservedTrip(trip);

        TripDTO tripDTO = new TripDTO();
        tripDTO.setName("tripName");
        tripDTO.setAvailableSpots(2);
        cal.set(2017, 10, 10);
        tripDTO.setDateFrom(cal.getTime());
        cal.set(2017, 10, 20);
        tripDTO.setDateTo(cal.getTime());
        tripDTO.setDestination("tripDestination");
        tripDTO.setPrice(new BigDecimal("150"));
        reservationDTO.setTrip(tripDTO);
        reservationNewDTO.setTrip(tripDTO);

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
        reservationNew.setReservedTrip(trip);
        reservationDTO.setTrip(tripDTO);
        reservationNewDTO.setTrip(tripDTO);
        customer.addReservation(reservation);

        customerDTOList = new ArrayList<>();
        customerDTOList.add(customerDTO);
        customerDTOList.add(customerNewDTO);

        customerList = new ArrayList<>();
        customerList.add(customer);
        customerList.add(customerNew);
    }

    @Test
    public void createCustomer() {
        when(beanMappingService.mapTo(customerDTO, Customer.class)).thenReturn(customer);
        customerFacade.create(customerDTO);
        verify(customerService).createCustomer(customer);
    }

    @Test
    public void updateCustomer() {
        customer.setId(1L);
        customerDTO.setId(1L);
        when(beanMappingService.mapTo(customerDTO, Customer.class)).thenReturn(customer);
        customerFacade.update(customerDTO);
        verify(customerService).updateCustomer(customer);
    }

    @Test
    public void removeCustomer() {
        customer.setId(1L);
        customerDTO.setId(1L);
        when(beanMappingService.mapTo(customerDTO, Customer.class)).thenReturn(customer);
        customerFacade.remove(customerDTO);
        verify(customerService).removeCustomer(customer);
    }

    @Test
    public void getTotalPriceCustomersReservationsCustomer() {
        when(beanMappingService.mapTo(customerDTO, Customer.class)).thenReturn(customer);
        when(customerService.getTotalPriceCustomersReservations(customer)).thenReturn(new BigDecimal("300"));
        Assert.assertEquals(customerService.getTotalPriceCustomersReservations(customer), new BigDecimal("300")
                , "returned value should be 300");
    }

    @Test
    public void changeCustomerOnReservationCustomer() {
        when(beanMappingService.mapTo(customerDTO, Customer.class)).thenReturn(customer);
        when(beanMappingService.mapTo(reservationDTO, Reservation.class)).thenReturn(reservation);
        when(customerService.changeCustomerOnReservation(customer, reservation)).thenReturn(customerNew);
        when(beanMappingService.mapTo(customerNew, CustomerDTO.class)).thenReturn(customerNewDTO);
        CustomerDTO obj = customerFacade.changeCustomerOnReservation(customerDTO, reservationDTO);
        Assert.assertNotNull(obj, "object is not null");
        Assert.assertEquals(obj.getId(), customerNewDTO.getId());
        customerEqualsTest(obj, customerNewDTO);
    }

    @Test
    public void findAllCustomer() {
        when(beanMappingService.mapTo(Matchers.anyCollection(), Matchers.eq(CustomerDTO.class)))
                .thenReturn(customerDTOList);
        when(customerService.findAll()).thenReturn(customerList);

        Assert.assertEquals(customerFacade.findAll().size(), customerList.size(), "foundList contains 2 elements");
    }

    @Test
    public void findByEmailCustomer() {
        when(beanMappingService.mapTo(customer, CustomerDTO.class)).thenReturn(customerDTO);
        when(customerService.findByEmail(Matchers.anyString())).thenReturn(customer);
        CustomerDTO foundDTO = customerFacade.findByEmail("email@email.com");
        Assert.assertNotNull(foundDTO);
        customerEqualsTest(foundDTO, customerDTO);
    }

    @Test
    public void findByIdCustomer() {
        customer.setId(1L);
        customerDTO.setId(1L);
        when(customerService.findById(1L)).thenReturn(customer);
        when(beanMappingService.mapTo(customer, CustomerDTO.class)).thenReturn(customerDTO);
        CustomerDTO foundDTO = customerFacade.findById(1L);
        Assert.assertNotNull(foundDTO);
        customerEqualsTest(foundDTO, customerDTO);
    }

    @Test
    public void findByIdCardNumberCustomer() {
        when(customerService.findByIdCardNumber("idCardNumber")).thenReturn(customer);
        when(beanMappingService.mapTo(customer, CustomerDTO.class)).thenReturn(customerDTO);
        CustomerDTO foundDTO = customerFacade.findByIdCardNumber("idCardNumber");
        Assert.assertNotNull(foundDTO);
        customerEqualsTest(foundDTO, customerDTO);
    }

    @Test
    public void findByReservationCustomer() {
        when(beanMappingService.mapTo(reservationDTO, Reservation.class)).thenReturn(reservation);
        when(customerService.findByReservation(reservation)).thenReturn(customer);
        when(beanMappingService.mapTo(customer, CustomerDTO.class)).thenReturn(customerDTO);
        CustomerDTO foundDTO = customerFacade.findByReservation(reservationDTO);
        Assert.assertNotNull(foundDTO);
        customerEqualsTest(foundDTO, customerDTO);
    }

    public void customerEqualsTest(CustomerDTO customerNew, CustomerDTO customerExpected) {
        Assert.assertEquals(customerNew.getIdCardNumber(), customerExpected.getIdCardNumber(),"IdCardNumber Equality");
        Assert.assertEquals(customerNew.getPhoneNumber(), customerExpected.getPhoneNumber(), "PhoneNumber Equality");
        Assert.assertEquals(customerNew.getBirthDate(), customerExpected.getBirthDate(), "BirthDate Equality");
        Assert.assertEquals(customerNew.getEmail(), customerExpected.getEmail(), "Email Equality");
        Assert.assertEquals(customerNew.getSurname(), customerExpected.getSurname(), "Surname Equality");
        Assert.assertEquals(customerNew.getName(), customerExpected.getName(), "Name Equality");
        Assert.assertEquals(customerNew.getReservations(), customerExpected.getReservations(), "Reservations Equality");
    }
}
