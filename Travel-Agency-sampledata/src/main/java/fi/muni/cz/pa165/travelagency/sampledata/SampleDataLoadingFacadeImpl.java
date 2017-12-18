package fi.muni.cz.pa165.travelagency.sampledata;

import fi.muni.cz.pa165.travelagency.entity.Excursion;
import fi.muni.cz.pa165.travelagency.entity.Reservation;
import fi.muni.cz.pa165.travelagency.entity.Trip;
import fi.muni.cz.pa165.travelagency.entity.User;
import fi.muni.cz.pa165.travelagency.enums.PaymentStateType;
import fi.muni.cz.pa165.travelagency.service.ExcursionService;
import fi.muni.cz.pa165.travelagency.service.ReservationService;
import fi.muni.cz.pa165.travelagency.service.TripService;
import fi.muni.cz.pa165.travelagency.service.UserService;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * SampleDataLoadingFacade implementation - data creation and insertion.
 *
 * @author (name = "Nermin Jukan", UCO = "<473370>")
 */
@Component
@Transactional
public class SampleDataLoadingFacadeImpl implements SampleDataLoadingFacade{
        
    @Autowired
    private ExcursionService excursionService;
            
    @Autowired
    private TripService tripService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ReservationService reservationService;

    @Override
    @SuppressWarnings("unused")
    public void loadData() throws ParseException{
        
        
        Set<Excursion> excursionSet = new HashSet();
        Set<Reservation> reservationSet = new HashSet();
            
        Excursion castle = excursion(date("05/06/2018"), 5, "Visiting the castle", "Paris",
                bigDecimal(60));
        Excursion hill = excursion(date("11/06/2018"), 2, "Visiting the hill viewpoint", "Paris",
                bigDecimal(25));
        Excursion lake = excursion(date("14/06/2018"), 1, "Visiting the lake", "Paris",
                bigDecimal(30));
        Excursion museum = excursion(date("12/07/2018"), 1, "Visiting the museum", "Ljubljana",
                bigDecimal(10));
        Excursion vineCellar = excursion(date("13/07/2018"), 1, "Visiting the vine cellar", "Ljubljana",
                bigDecimal(25));
        Excursion spa = excursion(date("14/07/2018"), 4, "Visiting the spa", "Ljubljana",
                bigDecimal(120));
        Excursion ossuary = excursion(date("30/09/2018"), 1, "Visiting the St. James' Ossuary", "Brno",
                bigDecimal(10));
        Excursion brewery = excursion(date("01/10/2018"), 1, "Visiting the brewery", "Brno",
                bigDecimal(15));
        Excursion zoo = excursion(date("02/10/2018"), 1, "Visiting the Zoo", "Brno",
                bigDecimal(8));
        Excursion prague = excursion(date("03/10/2018"), 3, "Visiting Prague", "Prague",
                bigDecimal(100));
        Excursion caves = excursion(date("06/10/2018"), 1, "Visiting the caves", "Brno",
                bigDecimal(15));
        
        Trip paris = trip(date("04/06/2018"), date("14/06/2018"), "Paris", 50, excursionSet,
                "Paris", bigDecimal(2400));
        paris.addExcursion(castle);
        paris.addExcursion(hill);
        paris.addExcursion(lake);
        
        Trip ljubljana = trip(date("12/07/2018"), date("18/07/2018"), "Ljubljana", 30, excursionSet,
                "Ljubljana", bigDecimal(250));
        ljubljana.addExcursion(museum);
        ljubljana.addExcursion(vineCellar);
        ljubljana.addExcursion(spa);
        
        Trip brno = trip(date("30/09/2018"), date("06/10/2018"), "Brno", 40, excursionSet,
                "Brno", bigDecimal(400));
        brno.addExcursion(ossuary);
        brno.addExcursion(brewery);
        brno.addExcursion(zoo);
        brno.addExcursion(prague);
        brno.addExcursion(caves);
        
        User helga = user("Helga", "Steinke", "000", "helga@user.com", "000000",
            reservationSet, date("26/01/1994"), "000000000", false);
        User mark = user("Mark", "Smith", "001", "mark@user.com", "001001",
            reservationSet, date("11/05/1985"), "001001001", false);
        User janez = user("Janez", "Novak", "010", "janez@user.com", "010010",
            reservationSet, date("26/02/1992"), "010010010", false);
        User michal = user("Michal", "Rozycki", "011", "michal@user.com", "011011",
            reservationSet, date("06/09/1976"), "011011011", false);
        User adminJakov = user("Jakov", "Boss", "100", "jakov@user.com", "100100",
            reservationSet, date("03/07/1974"), "100100100", true);
        User adminEva = user("Eva", "Boss", "101", "eva@user.com", "101101",
            reservationSet, date("17/11/1984"), "100100100", true);
        User pepa = user("Pepa", "Petak", "111", "pepa@user.com", "111111",
            reservationSet, date("03/09/1995"), "111111111", false);
        
        Reservation markParis = reservation(paris, excursionSet, mark, date("10/12/2017"),
            PaymentStateType.Paid);
        mark.addReservation(markParis);
        Reservation pepaParis = reservation(paris, excursionSet, pepa, date("16/12/2017"),
            PaymentStateType.Paid);
        pepa.addReservation(pepaParis);
        Reservation helgaParis = reservation(paris, excursionSet, helga, date("15/12/2017"),
            PaymentStateType.Paid);
        helga.addReservation(helgaParis);
        helgaParis.addReservedExcursion(castle);
        helgaParis.addReservedExcursion(hill);
        Reservation janezLjubljana = reservation(ljubljana, excursionSet, janez, date("11/12/2017"),
            PaymentStateType.NotPaid);
        janez.addReservation(janezLjubljana);
        Reservation markLjubljana = reservation(ljubljana, excursionSet, mark, date("02/03/2017"),
            PaymentStateType.Paid);
        mark.addReservation(markLjubljana);
        Reservation helgaLjubljana = reservation(ljubljana, excursionSet, helga, date("08/03/2017"),
            PaymentStateType.NotPaid);
        helga.addReservation(helgaLjubljana);
        Reservation michalLjubljana = reservation(ljubljana, excursionSet, michal, date("10/03/2017"),
            PaymentStateType.Paid);
        michal.addReservation(michalLjubljana);
        Reservation markBrno = reservation(brno, excursionSet, mark, date("30/09/2017"),
            PaymentStateType.NotPaid);
        mark.addReservation(markBrno);
        Reservation pepaBrno = reservation(brno, excursionSet, pepa, date("29/09/2017"),
            PaymentStateType.Paid);
        pepa.addReservation(pepaBrno);
        Reservation michalBrno = reservation(brno, excursionSet, michal, date("30/09/2017"),
            PaymentStateType.NotPaid);
        michalBrno.addReservedExcursion(museum);
        michalBrno.addReservedExcursion(spa);
        michal.addReservation(michalBrno);

    }
    
    private Excursion excursion(Date date, int duration, String description,
            String destination, BigDecimal price){
        
        Excursion excursion = new Excursion();
        excursion.setDescription(description);
        excursion.setDestination(destination);
        excursion.setDuration(duration);
        excursion.setExcursionDate(date);
        excursion.setPrice(price);
        
        excursionService.create(excursion);
        return excursion;
    }
    
    private Trip trip(Date dateFrom, Date dateTo, String destination, int availableTrips,
            Set<Excursion> excursions, String name, BigDecimal price){
        
        Trip trip = new Trip();
        trip.setAvailableSpots(availableTrips);
        trip.setDateFrom(dateFrom);
        trip.setDateTo(dateTo);
        trip.setDestination(destination);
        trip.setName(name);
        trip.setPrice(price);
        trip.addAllExcursions(excursions);
        
        tripService.createTrip(trip);
        return trip;
    }
    
    private Reservation reservation(Trip reservedTrip, Set<Excursion> excursions, User user,
            Date created, PaymentStateType paymentState){
        
        Reservation reservation = new Reservation();
        reservation.setCreated(created);
        reservation.setPaymentState(paymentState);
        reservation.setUser(user);
        reservation.setTrip(reservedTrip);
        reservation.addAllReservedExcursions(excursions);
        
        reservationService.createReservation(reservation);
        return reservation;
    }
    
    private User user(String name, String surname, String idCardNumber, String email,
            String phoneNumber, Set<Reservation> reservations, Date birthDate, String password,
            Boolean isAdmin){
        
        User user = new User();
        user.setBirthDate(birthDate);
        user.setEmail(email);
        user.setIdCardNumber(idCardNumber);
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        user.setReservation(reservations);
        user.setSurname(surname);
        user.setIsAdmin(isAdmin);
        
        userService.registerUser(user, password);
        return user;
    }
    
    private Date date(String date) throws ParseException{
        Date parsedDate = new Date();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        parsedDate = sdf.parse(date);
        
        return parsedDate;
    }
    
    private BigDecimal bigDecimal(int transform){
        return BigDecimal.valueOf(transform);
    }
    
}
