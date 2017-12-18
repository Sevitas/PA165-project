package fi.muni.cz.pa165.travelagency.service.facade;

import fi.muni.cz.pa165.travelagency.dto.UserDTO;
import fi.muni.cz.pa165.travelagency.dto.ReservationDTO;
import fi.muni.cz.pa165.travelagency.dto.TripDTO;
import fi.muni.cz.pa165.travelagency.entity.User;
import fi.muni.cz.pa165.travelagency.entity.Excursion;
import fi.muni.cz.pa165.travelagency.entity.Reservation;
import fi.muni.cz.pa165.travelagency.entity.Trip;
import fi.muni.cz.pa165.travelagency.enums.PaymentStateType;
import fi.muni.cz.pa165.travelagency.facade.UserFacade;
import fi.muni.cz.pa165.travelagency.service.BeanMappingService;
import fi.muni.cz.pa165.travelagency.service.UserService;
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
public class UserFacadeTest extends AbstractTestNGSpringContextTests {

    @Mock
    private BeanMappingService beanMappingService;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserFacade userFacade = new UserFacadeImpl();

    @BeforeClass
    public void setupClass() {
        MockitoAnnotations.initMocks(this);
    }

    private User user, userNew;
    private Reservation reservation, reservationNew;
    private ReservationDTO reservationDTO, reservationNewDTO;
    private UserDTO userDTO, userNewDTO;
    private List<UserDTO> userDTOList;
    private List<User> userList;


    @BeforeMethod
    public void setup() {
        Calendar cal = Calendar.getInstance();
        user = new User();
        user.setEmail("email@email.com");
        user.setIdCardNumber("idCardNumber");
        user.setName("Name");
        user.setSurname("Surname");
        user.setPhoneNumber("7894556123");
        cal.set(2017, 28, 10);
        user.setBirthDate(cal.getTime());

        userDTO = new UserDTO();
        userDTO.setEmail("email@email.com");
        userDTO.setIdCardNumber("idCardNumber");
        userDTO.setName("Name");
        userDTO.setSurname("Surname");
        userDTO.setPhoneNumber("7894556123");
        cal.set(2017, 28, 10);
        userDTO.setBirthDate(cal.getTime());

        userNew = new User();
        userNew.setEmail("email2@email.com");
        userNew.setIdCardNumber("idCardNumber2");
        userNew.setName("Name2");
        userNew.setSurname("Surname2");
        userNew.setPhoneNumber("123456789");
        cal.set(2017, 10, 9);
        userNew.setBirthDate(cal.getTime());

        userNewDTO = new UserDTO();
        userNewDTO.setEmail("email2@email.com");
        userNewDTO.setIdCardNumber("idCardNumber2");
        userNewDTO.setName("Name2");
        userNewDTO.setSurname("Surname2");
        userNewDTO.setPhoneNumber("123456789");
        cal.set(2017, 10, 9);
        userNewDTO.setBirthDate(cal.getTime());

        reservation = new Reservation();
        reservation.setPaymentState(PaymentStateType.Paid);
        reservation.setUser(user);
        reservation.setCreated(cal.getTime());

        reservationDTO = new ReservationDTO();
        reservationDTO.setUser(userDTO);
        reservationDTO.setCreated(cal.getTime());

        reservationNew = new Reservation();
        reservationNew.setPaymentState(PaymentStateType.NotPaid);
        reservationNew.setUser(userNew);
        reservationNew.setCreated(cal.getTime());

        reservationNewDTO = new ReservationDTO();
        reservationNewDTO.setUser(userNewDTO);
        reservationNewDTO.setCreated(cal.getTime());


        Set<ReservationDTO> listReservationsDTO = new HashSet<>();
        listReservationsDTO.add(reservationDTO);
        userDTO.setReservations(listReservationsDTO);

        Set<Reservation> listReservations = new HashSet<>();
        listReservations.add(reservation);
        user.setReservation(listReservations);

        Set<ReservationDTO> listReservationsNewDTO = new HashSet<>();
        listReservationsDTO.add(reservationNewDTO);
        userNewDTO.setReservations(listReservationsNewDTO);

        Set<Reservation> listReservationsNew = new HashSet<>();
        listReservationsNew.add(reservationNew);
        userNew.setReservation(listReservationsNew);

        Trip trip = new Trip();
        trip.setName("tripName");
        trip.setAvailableSpots(2);
        cal.set(2017, 10, 10);
        trip.setDateFrom(cal.getTime());
        cal.set(2017, 10, 20);
        trip.setDateTo(cal.getTime());
        trip.setDestination("tripDestination");
        trip.setPrice(new BigDecimal("150"));
        reservation.setTrip(trip);
        reservationNew.setTrip(trip);

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
        reservation.setTrip(trip);
        reservationNew.setTrip(trip);
        reservationDTO.setTrip(tripDTO);
        reservationNewDTO.setTrip(tripDTO);
        user.addReservation(reservation);

        userDTOList = new ArrayList<>();
        userDTOList.add(userDTO);
        userDTOList.add(userNewDTO);

        userList = new ArrayList<>();
        userList.add(user);
        userList.add(userNew);
    }

    @Test
    public void registerUserTest() {
        when(beanMappingService.mapTo(userDTO, User.class)).thenReturn(user);
        userFacade.registerUser(userDTO, "pw");
        verify(userService).registerUser(user, "pw");
    }

    @Test
    public void updateUserTest() {
        user.setId(1L);
        userDTO.setId(1L);
        when(beanMappingService.mapTo(userDTO, User.class)).thenReturn(user);
        userFacade.updateUser(userDTO);
        verify(userService).updateUser(user);
    }

    @Test
    public void removeUserTest() {
        user.setId(1L);
        userDTO.setId(1L);
        when(beanMappingService.mapTo(userDTO, User.class)).thenReturn(user);
        userFacade.removeUser(userDTO);
        verify(userService).removeUser(user);
    }

    @Test
    public void getTotalPriceUsersReservationsUserTest() {
        when(beanMappingService.mapTo(userDTO, User.class)).thenReturn(user);
        when(userService.getTotalPriceUsersReservations(user)).thenReturn(new BigDecimal("300"));
        Assert.assertEquals(userFacade.getTotalPriceUsersReservations(userDTO), new BigDecimal("300")
                , "returned value should be 300");
    }

    @Test
    public void changeUserOnReservationUserTest() {
        when(beanMappingService.mapTo(userDTO, User.class)).thenReturn(user);
        when(beanMappingService.mapTo(reservationDTO, Reservation.class)).thenReturn(reservation);
        when(userService.changeUserOnReservation(user, reservation)).thenReturn(userNew);
        when(beanMappingService.mapTo(userNew, UserDTO.class)).thenReturn(userNewDTO);
        UserDTO obj = userFacade.changeUserOnReservation(userDTO, reservationDTO);
        Assert.assertNotNull(obj, "object is not null");
        Assert.assertEquals(obj.getId(), userNewDTO.getId());
        userEqualsTest(obj, userNewDTO);
    }

    @Test
    public void findAllUserTest() {
        when(beanMappingService.mapTo(Matchers.anyCollection(), Matchers.eq(UserDTO.class)))
                .thenReturn(userDTOList);
        when(userService.findAll()).thenReturn(userList);

        Assert.assertEquals(userFacade.findAll().size(), userList.size(), "foundList contains 2 elements");
    }

    @Test
    public void findByEmailUserTest() {
        when(beanMappingService.mapTo(user, UserDTO.class)).thenReturn(userDTO);
        when(userService.findByEmail(Matchers.anyString())).thenReturn(user);
        UserDTO foundDTO = userFacade.findByEmail("email@email.com");
        Assert.assertNotNull(foundDTO);
        userEqualsTest(foundDTO, userDTO);
    }

    @Test
    public void findByIdUserTest() {
        user.setId(1L);
        userDTO.setId(1L);
        when(userService.findById(1L)).thenReturn(user);
        when(beanMappingService.mapTo(user, UserDTO.class)).thenReturn(userDTO);
        UserDTO foundDTO = userFacade.findById(1L);
        Assert.assertNotNull(foundDTO);
        userEqualsTest(foundDTO, userDTO);
    }

    @Test
    public void findByIdCardNumberUserTest() {
        when(userService.findByIdCardNumber("idCardNumber")).thenReturn(user);
        when(beanMappingService.mapTo(user, UserDTO.class)).thenReturn(userDTO);
        UserDTO foundDTO = userFacade.findByIdCardNumber("idCardNumber");
        Assert.assertNotNull(foundDTO);
        userEqualsTest(foundDTO, userDTO);
    }

    @Test
    public void findByReservationUserTest() {
        when(beanMappingService.mapTo(reservationDTO, Reservation.class)).thenReturn(reservation);
        when(userService.findByReservation(reservation)).thenReturn(user);
        when(beanMappingService.mapTo(user, UserDTO.class)).thenReturn(userDTO);
        UserDTO foundDTO = userFacade.findByReservation(reservationDTO);
        Assert.assertNotNull(foundDTO);
        userEqualsTest(foundDTO, userDTO);
    }

    public void userEqualsTest(UserDTO userNew, UserDTO userExpected) {
        Assert.assertEquals(userNew.getIdCardNumber(), userExpected.getIdCardNumber(),"IdCardNumber Equality");
        Assert.assertEquals(userNew.getPhoneNumber(), userExpected.getPhoneNumber(), "PhoneNumber Equality");
        Assert.assertEquals(userNew.getBirthDate(), userExpected.getBirthDate(), "BirthDate Equality");
        Assert.assertEquals(userNew.getEmail(), userExpected.getEmail(), "Email Equality");
        Assert.assertEquals(userNew.getSurname(), userExpected.getSurname(), "Surname Equality");
        Assert.assertEquals(userNew.getName(), userExpected.getName(), "Name Equality");
        Assert.assertEquals(userNew.getReservations(), userExpected.getReservations(), "Reservations Equality");
    }
}
