package fi.muni.cz.pa165.travelagency.service.facade;

import fi.muni.cz.pa165.travelagency.dto.*;
import fi.muni.cz.pa165.travelagency.dto.UserDTO;
import fi.muni.cz.pa165.travelagency.entity.User;
import fi.muni.cz.pa165.travelagency.entity.Excursion;
import fi.muni.cz.pa165.travelagency.entity.Reservation;
import fi.muni.cz.pa165.travelagency.entity.Trip;
import fi.muni.cz.pa165.travelagency.facade.ReservationFacade;
import fi.muni.cz.pa165.travelagency.service.BeanMappingService;
import fi.muni.cz.pa165.travelagency.service.UserService;
import fi.muni.cz.pa165.travelagency.service.TripService;
import fi.muni.cz.pa165.travelagency.service.ExcursionService;
import fi.muni.cz.pa165.travelagency.service.ReservationService;
import fi.muni.cz.pa165.travelagency.service.config.ServiceConfiguration;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.assertj.core.util.Sets;
import org.hibernate.service.spi.ServiceException;



import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;


import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Radoslav Micko <445611>
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
public class ReservationFacadeTest extends AbstractTestNGSpringContextTests {
    
    @Mock
    private UserService userService;
    
    @Mock
    private ReservationService reservationService;
    
    @Mock
    private TripService tripService;
    
    @Mock
    private ExcursionService excursionService;
    
    @Mock
    private BeanMappingService beanMappingService;
    
    @InjectMocks
    private ReservationFacade reservationFacade = new ReservationFacadeImpl();
    
   
    
    private ReservationDTO reservationDTO;
    private UserDTO userDTO;
    private TripDTO tripDTO;
    private ExcursionDTO excursionDTO;
            
    
    private User user;
    private Trip trip;
    private Excursion excursion;
    private Reservation reservation;
    
    @BeforeClass
    public void setUp() throws ServiceException {
        MockitoAnnotations.initMocks(this);
    }
    
    
    @BeforeMethod
    public void setUpMethod() {
        user = new User();
        user.setId(1l);
        user.setSurname("Name");
        user.setEmail("name@email.com");
        user.setBirthDate(Date.valueOf("2000-1-1"));
        user.setIdCardNumber("Id Card");
        
        trip = new Trip();
        trip.setId(1l);
        trip.setAvailableSpots(10);
        trip.setDateFrom(Date.valueOf("2015-1-1"));
        trip.setDateTo(Date.valueOf("2015-1-2"));
        trip.setDestination("Destination");
        trip.setName("Trip");
        trip.setPrice(new BigDecimal("1000"));
        
        excursion = new Excursion();
        excursion.setId(1l);
        excursion.setDescription("Description");
        excursion.setDestination("Destination");
        excursion.setDuration(1);
        excursion.setExcursionDate(Date.valueOf("2015-1-1"));
        excursion.setPrice(new BigDecimal("1000"));
        
        reservation = new Reservation();
        reservation.setId(1l);
        reservation.setCreated(Date.valueOf("2014-1-1"));
        reservation.setUser(user);
        reservation.addReservedExcursion(excursion);
        reservation.setTrip(trip);
        
        userDTO = new UserDTO();
        userDTO.setId(1l);
        userDTO.setSurname("Name");
        userDTO.setEmail("name@email.com");
        userDTO.setBirthDate(Date.valueOf("2000-1-1"));
        userDTO.setIdCardNumber("Id Card");
        
        tripDTO = new TripDTO();
        tripDTO.setId(1l);
        tripDTO.setAvailableSpots(10);
        tripDTO.setDateFrom(Date.valueOf("2015-1-1"));
        tripDTO.setDateTo(Date.valueOf("2015-1-2"));
        tripDTO.setDestination("Destination");
        tripDTO.setName("Trip");
        tripDTO.setPrice(new BigDecimal("1000"));
        
        excursionDTO = new ExcursionDTO();
        excursionDTO.setId(1l);
        excursionDTO.setDescription("Description");
        excursionDTO.setDestination("Destination");
        excursionDTO.setDuration(1);
        excursionDTO.setExcursionDate(Date.valueOf("2015-1-1"));
        excursionDTO.setPrice(new BigDecimal("1000"));
        
        reservationDTO = new ReservationDTO();
        reservationDTO.setId(1l);
        reservationDTO.setCreated(Date.valueOf("2014-1-1"));
        reservationDTO.setUser(userDTO);
        reservationDTO.setExcursions(Sets.newLinkedHashSet(excursionDTO));
        reservationDTO.setTrip(tripDTO);

    }
    
    @Test
    public void createReservationTest() {
        ReservationCreateDTO rcdto = new ReservationCreateDTO();
        Set<Long> excursionsId = new HashSet<>();
        excursionsId.add(1l);
        
        user.setId(1l);
        rcdto.setUserId(user.getId());
        rcdto.setExcursionsId(excursionsId);
        rcdto.setDate(Date.valueOf("2014-1-1"));
        trip.setId(1l);
        rcdto.setTripId(trip.getId());
        
        when(userService.findById(rcdto.getUserId())).thenReturn(user);
        when(tripService.findTripWithId(rcdto.getTripId())).thenReturn(trip);
        when(excursionService.findById(1l)).thenReturn(excursion);
        
        reservationFacade.createReservation(rcdto);
        verify(reservationService).createReservation(reservation);
    }
    
    @Test
    public void updateReservationTest() {
        when(beanMappingService.mapTo(reservationDTO, Reservation.class)).thenReturn(reservation);
        reservationFacade.updateReservation(reservationDTO);
        verify(reservationService).updateReservation(reservation);
    }
    
    @Test
    public void removeReservation() {
        reservationFacade.removeReservation(1l);
        verify(reservationService).removeReservation(reservation);
    }
    
    @Test
    public void addExcursionToReservationTest() {
        when(reservationService.findById(1l)).thenReturn(reservation);
        when(excursionService.findById(1l)).thenReturn(excursion);
        reservationFacade.addExcursionToReservation(reservation.getId()
                , excursion.getId());
        verify(reservationService).addExcursionToReservation(reservation, excursion);
    }
    
    @Test
    public void addExcursionsToReservationTest() {
        when(reservationService.findById(1l)).thenReturn(reservation);
        when(excursionService.findById(1l)).thenReturn(excursion);
        
        reservationFacade.addExcrusionsToReservation(reservation.getId()
                , Arrays.asList(excursion.getId()));
        
        
        verify(reservationService).addExcrusionsToReservation(reservation
                , Arrays.asList(excursion));
    }
    
    @Test
    public void findAllReservationsTest() {
        when(beanMappingService.mapTo(
                        reservationService.findAll(),
                        ReservationDTO.class)).thenReturn(Arrays.asList(reservationDTO));
        when(reservationService.getTotalPrice(beanMappingService
                .mapTo(reservationDTO, Reservation.class))).thenReturn(new BigDecimal("2000"));
        assertEquals(reservationFacade.findAllReservations().get(0).getCreated(), reservationDTO.getCreated());
        assertEquals(reservationFacade.findAllReservations().get(0).getUser(), reservationDTO.getUser());
        assertEquals(reservationFacade.findAllReservations().get(0).getExcursions().size(), reservationDTO.getExcursions().size());
        assertEquals(reservationFacade.findAllReservations().get(0).getTotalPrice(), reservationDTO.getTotalPrice());
        assertEquals(reservationFacade.findAllReservations().get(0).getTrip(), reservationDTO.getTrip());
    }
    
    @Test
    public void findReservationByIdTest() {
        when(beanMappingService.mapTo(
                        reservationService.findById(1l), ReservationDTO.class))
                .thenReturn(reservationDTO);
        assertEquals(reservationFacade.findReservationById(1l), reservationDTO); 
    }
    
    @Test
    public void findReservationByUserTest() {
        
        when(beanMappingService.mapTo(
                reservationService.findByUser(
                        userService.findById(1l)), ReservationDTO.class))
                .thenReturn(Arrays.asList(reservationDTO));
        when(reservationService.getTotalPrice(
               beanMappingService.mapTo(reservationDTO, Reservation.class))).thenReturn(new BigDecimal("2000"));
        assertEquals(reservationFacade.findReservationByUser(1l).get(0), reservationDTO);
        assertEquals(reservationFacade.findReservationByUser(1l).get(0).getTotalPrice(), new BigDecimal("2000"));
    }
    
    @Test
    public void findReservationByTripTest() {
        
        when(beanMappingService.mapTo(
                reservationService.findByTrip(
                        tripService.findTripWithId(1l)), ReservationDTO.class))
                .thenReturn(Arrays.asList(reservationDTO));
        when(reservationService.getTotalPrice(
               beanMappingService.mapTo(reservationDTO, Reservation.class))).thenReturn(new BigDecimal("2000"));
        assertEquals(reservationFacade.findReservationByTrip(1l).get(0), reservationDTO);
        assertEquals(reservationFacade.findReservationByTrip(1l).get(0).getTotalPrice(), new BigDecimal("2000"));
    }
    
    @Test
    public void findReservationsCreatedBetweenTest() {
        Date start = Date.valueOf("2014-1-1");
        Date end = Date.valueOf("2014-1-2");
        when(beanMappingService.mapTo(
                reservationService.getReservationsCreatedBetween(start, end),
                ReservationDTO.class))
                .thenReturn(Arrays.asList(reservationDTO));
        when(reservationService.getTotalPrice(
               beanMappingService.mapTo(reservationDTO, Reservation.class))).thenReturn(new BigDecimal("2000"));
        assertEquals(reservationFacade.findReservationsCreatedBetween(start, end).get(0), reservationDTO);
        assertEquals(reservationFacade.findReservationsCreatedBetween(start, end).get(0).getTotalPrice(), new BigDecimal("2000"));
    }
    
    @Test
    public void setReservationPaidTest() {
        when(reservationService.findById(1l)).thenReturn(reservation);
        reservationFacade.setReservationPaid(1l);
        verify(reservationService).setPaidState(reservation);
    }
    
    @Test
    public void setReservationNotPaidTest() {
        when(reservationService.findById(1l)).thenReturn(reservation);
        reservationFacade.setReservationNotPaid(1l);
        verify(reservationService).setNotPaidState(reservation);
    }
    
    @Test
    public void findAllSortedByDateTest() {
        when(beanMappingService.mapTo(
                reservationService.findAllSortedByDate(), ReservationDTO.class))
                .thenReturn(Arrays.asList(reservationDTO));
        when(reservationService.getTotalPrice(
               beanMappingService.mapTo(reservationDTO, Reservation.class))).thenReturn(new BigDecimal("2000"));
        assertEquals(reservationFacade.findAllSortedByDate().get(0), reservationDTO);
        assertEquals(reservationFacade.findAllSortedByDate().get(0).getTotalPrice(), new BigDecimal("2000"));
    }
    
    @Test
    public void findAllNotPaidTest() {
        when(beanMappingService.mapTo(
                reservationService.findAllNotPaid(), ReservationDTO.class))
                .thenReturn(Arrays.asList(reservationDTO));
        when(reservationService.getTotalPrice(
               beanMappingService.mapTo(reservationDTO, Reservation.class))).thenReturn(new BigDecimal("2000"));
        assertEquals(reservationFacade.findAllNotPaid().get(0), reservationDTO);
        assertEquals(reservationFacade.findAllNotPaid().get(0).getTotalPrice(), new BigDecimal("2000"));
    }
    
    
    
}
