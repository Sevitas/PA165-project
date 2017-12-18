package fi.muni.cz.pa165.travelagency.service.facade;

import fi.muni.cz.pa165.travelagency.dto.ReservationCreateDTO;
import fi.muni.cz.pa165.travelagency.dto.ReservationDTO;
import fi.muni.cz.pa165.travelagency.entity.User;
import fi.muni.cz.pa165.travelagency.entity.Excursion;
import fi.muni.cz.pa165.travelagency.entity.Reservation;
import fi.muni.cz.pa165.travelagency.entity.Trip;
import fi.muni.cz.pa165.travelagency.exceptions.TravelAgencyServiceException;
import fi.muni.cz.pa165.travelagency.facade.ReservationFacade;
import fi.muni.cz.pa165.travelagency.service.BeanMappingService;
import fi.muni.cz.pa165.travelagency.service.UserService;
import fi.muni.cz.pa165.travelagency.service.ExcursionService;
import fi.muni.cz.pa165.travelagency.service.ReservationService;
import fi.muni.cz.pa165.travelagency.service.TripService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Radoslav Micko <445611>
 */
@Service
@Transactional
public class ReservationFacadeImpl implements ReservationFacade {

    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private TripService tripService;

    @Autowired
    private ExcursionService excursionService;
    
    @Autowired
    private BeanMappingService beanMappingService;
    
    @Override
    public Long createReservation(ReservationCreateDTO reservation) {
        
        User user = userService.findById(reservation.getUserId());
        if (user == null ) {
            throw new TravelAgencyServiceException("Not existing user");
        } 
        
        Trip trip = tripService.findTripWithId(reservation.getTripId());
        if (trip == null) {
            throw new TravelAgencyServiceException("Not existing trip");
        }
        
        Reservation newReservation = new Reservation();
        newReservation.setUser(user);
        newReservation.setTrip(trip);
        newReservation.setCreated(reservation.getDate());
        if (reservation.getExcursionsId() != null) {
            for (Long excursionId : reservation.getExcursionsId()) {
                Excursion excursion = excursionService.findById(excursionId);
                if (excursion != null) {
                    newReservation.addReservedExcursion(excursion);
                }
            }
        }
        
        user.addReservation(newReservation);
        userService.updateUser(user);
        reservationService.createReservation(newReservation);
        return newReservation.getId();
    }

    @Override
    public void updateReservation(ReservationDTO reservation) {
        Reservation newReservation = beanMappingService.mapTo(
                reservation, Reservation.class);

        reservationService.updateReservation(newReservation);
    }

    @Override
    public void removeReservation(Long reservationId) {
        reservationService.removeReservation(reservationService.findById(reservationId));
    }

    @Override
    public void addExcursionToReservation(Long reservationId, Long excursionId) {
        Reservation r = reservationService.findById(reservationId);  
        Excursion e = excursionService.findById(excursionId);
        reservationService.addExcursionToReservation(r, e);
    }

    @Override
    public void addExcrusionsToReservation(Long reservationId, List<Long> excursionsId) {
        Reservation reservation = reservationService.findById(reservationId);
        
        List<Excursion> excursions = new ArrayList<>();
                
        for (Long excursionId : excursionsId) {
            Excursion excursion = excursionService.findById(excursionId); 
            if (excursion != null) {
                    excursions.add(excursion);
            }
        }
        
        reservationService.addExcrusionsToReservation(reservation, excursions);
    }

    @Override
    public List<ReservationDTO> findAllReservations() {
        List<ReservationDTO> reservations = beanMappingService.mapTo(
                        reservationService.findAll(),
                        ReservationDTO.class);
        return setPrice(reservations);
    }

    @Override
    public ReservationDTO findReservationById(Long id) {
        ReservationDTO reservation = beanMappingService.mapTo(
                        reservationService.findById(id), ReservationDTO.class);
        return setPrice(reservation);
    }

    @Override
    public List<ReservationDTO> findReservationByUser(Long userId) {
        List<ReservationDTO> reservations = beanMappingService.mapTo(
                reservationService.findByUser(
                        userService.findById(userId)),
                ReservationDTO.class);
        return setPrice(reservations);
    }

    @Override
    public List<ReservationDTO> findReservationByTrip(Long tripId) {
        List<ReservationDTO> reservations = beanMappingService.mapTo(
                reservationService.findByTrip(tripService.findTripWithId(tripId)),
                ReservationDTO.class);
        return setPrice(reservations);
    }
    
    @Override
    public List<ReservationDTO> findReservationsCreatedBetween(Date start, Date end) {
        List<ReservationDTO> reservations = beanMappingService.mapTo(
                reservationService.getReservationsCreatedBetween(start, end),
                ReservationDTO.class);
        return setPrice(reservations);
    }

    @Override
    public void setReservationPaid(Long reservationId) {
        reservationService.setPaidState(reservationService.findById(reservationId));
    }

    @Override
    public void setReservationNotPaid(Long reservationId) {
        reservationService.setNotPaidState(reservationService.findById(reservationId));
    }
    
    private List<ReservationDTO> setPrice(List<ReservationDTO> reservations) {
        for (ReservationDTO reservation: reservations) {
            setPrice(reservation);
        }
        return reservations;
    }
    
    private ReservationDTO setPrice(ReservationDTO reservation) {
         reservation.setTotalPrice(reservationService.getTotalPrice(
                beanMappingService.mapTo(reservation, Reservation.class)));
         return reservation;
    }

    @Override
    public List<ReservationDTO> findAllSortedByDate() {
        List<ReservationDTO> reservations = beanMappingService.mapTo(
                        reservationService.findAllSortedByDate(),
                        ReservationDTO.class);
        return setPrice(reservations);
    }

    @Override
    public List<ReservationDTO> findAllNotPaid() {
        List<ReservationDTO> reservations = beanMappingService.mapTo(
                        reservationService.findAllNotPaid(),
                        ReservationDTO.class);
        return setPrice(reservations);
    }
}
