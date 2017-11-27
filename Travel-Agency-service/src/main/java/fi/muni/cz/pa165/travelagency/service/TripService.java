package fi.muni.cz.pa165.travelagency.service;

import fi.muni.cz.pa165.travelagency.entity.Excursion;
import fi.muni.cz.pa165.travelagency.entity.Trip;

import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Pavel Kotala
 */
@Service
public interface TripService {
    
    /**
     * Adds given trip to database.
     * @param trip trip to be added
     * @return added trip
     */
    Trip createTrip(Trip trip);
    
    /**
     * Adds given excursion to given trip.
     * @param trip trip to add excursion to
     * @param excursion excursion to be added to trip
     */
    void addExcursion(Trip trip, Excursion excursion);
    
    /**
     * Adds all given excursions to given trip.
     * @param trip trip to add excursions to
     * @param excursions excursions to be added to trip
     */
    void addAllExcursions(Trip trip, Set<Excursion> excursions);
    
    /**
     * Removes given excursion from given trip.
     * @param trip trip to removeCustomer excursion from
     * @param excursion excursion to be removed from trip
     */
    void removeExcursion(Trip trip, Excursion excursion);
    
    /**
     * Deletes given trip.
     * @param trip trip to be deleted
     */
    void deleteTrip(Trip trip);

    /**
     * Updates given trip.
     * @param trip trip to be updated
     */
    void updateTrip(Trip trip);
    
    /**
     * Returns all trips.
     * @return all trips
     */
    List<Trip> findAllTrips();
    
    /**
     * Returns all trips with given name.
     * @param tripName name of the trips
     * @return all trips with given name
     */
    List<Trip> findTripsByName(String tripName);
    
    /**
     * Returns all trips between given dates.
     * @param start start date
     * @param end end date
     * @return all trips between given dates.
     */
    List<Trip> findTripsBetween(Date start, Date end);
    
    /**
     * Returns trip with given id.
     * @param id id of desired trip
     * @return trip with given id
     */
    Trip findTripWithId(Long id);

    /**
     * Returns all suitable excursions for given trip.
     * Suitable excursion has the same destination as given trip and
     * date of excursion is between start and end dates of given trip
     * @param trip trip to find suitable excursions for
     * @return all suitable excursions
     */
    List<Excursion> findAllSuitableExcursions(Trip trip);

    /**
     * Returns next n trips in the sequence after this trip.
     * The trip sequence is sorted lexicographically by start date, end date and name, all in ascending order.
     * Trips that take place later than one month from the start of given trip will be disregarded.
     * @param trip trip to find trips after
     * @param n number of trips to return, positive integer
     * @return next 10 trips
     */
    List<Trip> findNextTrips(Trip trip, int n);
}
