package fi.muni.cz.pa165.travelagency.service.facade;

import fi.muni.cz.pa165.travelagency.dto.ExcursionDTO;
import fi.muni.cz.pa165.travelagency.dto.TripCreateDTO;
import fi.muni.cz.pa165.travelagency.dto.TripDTO;
import fi.muni.cz.pa165.travelagency.entity.Excursion;
import fi.muni.cz.pa165.travelagency.entity.Trip;
import fi.muni.cz.pa165.travelagency.facade.TripFacade;
import fi.muni.cz.pa165.travelagency.service.BeanMappingService;
import fi.muni.cz.pa165.travelagency.service.ExcursionService;
import fi.muni.cz.pa165.travelagency.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Pavel Kotala
 */
@Service
@Transactional
public class TripFacadeImpl implements TripFacade {

    @Autowired
    private TripService tripService;

    @Autowired
    private ExcursionService excursionService;

    @Autowired
    private BeanMappingService beanMappingService;

    @Override
    public Long createTrip(TripCreateDTO trip) {
        Trip mapped = beanMappingService.mapTo(trip, Trip.class);

        mapped = tripService.createTrip(mapped);
        return mapped.getId();
    }

    @Override
    public void addExcursion(Long tripId, Long excursionId) {
        tripService.addExcursion(tripService.findTripWithId(tripId),
                excursionService.findById(excursionId));
    }

    @Override
    public void addAllExcursions(Long tripId, Set<Long> excursionIds) {
        Set<Excursion> excs = new HashSet<>();
        for(Long id : excursionIds) {
            excs.add(excursionService.findById(id));
        }
        tripService.addAllExcursions(tripService.findTripWithId(tripId),
                excs);
    }

    @Override
    public void removeExcursion(Long tripId, Long excursionId) {
        tripService.removeExcursion(tripService.findTripWithId(tripId),
                excursionService.findById(excursionId));
    }

    @Override
    public void updateTrip(TripDTO trip) {
        Trip mapped = beanMappingService.mapTo(trip, Trip.class);

        tripService.updateTrip(mapped);
    }

    @Override
    public void deleteTrip(Long tripId) {
        tripService.deleteTrip(tripService.findTripWithId(tripId));
    }

    @Override
    public List<TripDTO> getAllTrips() {
        return beanMappingService.mapTo(tripService.findAllTrips(), TripDTO.class);
    }

    @Override
    public List<TripDTO> getTripsByName(String tripName) {
        return beanMappingService.mapTo(tripService.findTripsByName(tripName), TripDTO.class);
    }

    @Override
    public List<TripDTO> getTripsBetween(Date start, Date end) {
        return beanMappingService.mapTo(tripService.findTripsBetween(start, end), TripDTO.class);
    }

    @Override
    public TripDTO getTripWithId(Long id) {
        return beanMappingService.mapTo(tripService.findTripWithId(id), TripDTO.class);
    }

    @Override
    public List<ExcursionDTO> getAllSuitableExcursions(Long tripId) {
        return beanMappingService.mapTo(tripService.findAllSuitableExcursions(tripService.findTripWithId(tripId)),
                ExcursionDTO.class);
    }

    @Override
    public List<TripDTO> getNextTrips(Long tripId, int n) {
        return beanMappingService.mapTo(tripService.findNextTrips(tripService.findTripWithId(tripId), n),
                TripDTO.class);
    }
}
