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
import fi.muni.cz.pa165.travelagency.service.config.ServiceConfiguration;
import org.assertj.core.api.SoftAssertions;
import org.hibernate.service.spi.ServiceException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

@ContextConfiguration(classes = ServiceConfiguration.class)
public class TripFacadeTest extends AbstractTestNGSpringContextTests {
    @Mock
    private TripService tripService;

    @Mock
    private ExcursionService excursionService;

    @Mock
    private BeanMappingService beanMappingService;

//    @Autowired
    @InjectMocks
    private TripFacade tripFacade = new TripFacadeImpl();

    private Trip t1;
    private Trip t2;
    private Excursion e;
    private ExcursionDTO eDTO;
    private TripDTO d1;
    private TripCreateDTO c1;
    private Long eId;
    private Long tId;
    private Set<Excursion> eSet;
    private List<Trip> tList;
    private List<TripDTO> dList;
    private String name;
    private List<Excursion> eList;
    private List<ExcursionDTO> eDTOList;
    private int n;
    private Date end;
    private Date start;

    @BeforeClass
    public void setup() throws ServiceException {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeMethod
    public void parametersSetup() {
        Calendar calendar =  Calendar.getInstance();
        calendar.set(2012, 4, 20);
        start = calendar.getTime();
        calendar.set(2012, 4, 24);
        end = calendar.getTime();

        String dest = "dest";
        int available = 10;
        eId = 1L;
        tId = 2L;
        name = "name";
        String desc = "desc";
        int dur = 1;
        BigDecimal price = BigDecimal.TEN;
        n = 4;

        t1 = new Trip(start, end, dest, available,
                new HashSet<>(), name, price);

        tList = Arrays.asList(t1);
        e = new Excursion(desc, dest, dur, start, price);
        e.setId(eId);

        eSet = new HashSet<>();
        eSet.add(e);
        eList = Arrays.asList(e);

        eDTO = new ExcursionDTO();
        eDTO.setDescription(desc);
        eDTO.setDestination(dest);
        eDTO.setDuration(dur);
        eDTO.setExcursionDate(start);
        eDTO.setPrice(price);
        eDTO.setId(eId);

        Set<ExcursionDTO> eDTOSet = new HashSet<>();
        eDTOSet.add(eDTO);
        eDTOList = Arrays.asList(eDTO);

        t2 = new Trip(start, end, dest, available,
                eSet, name, price);

        c1 = new TripCreateDTO(start, end, dest, available, name, price);
        d1 = new TripDTO(start, end, dest, available, eDTOSet, name, price);
        d1.setId(tId);

        dList = Arrays.asList(d1);

        when(beanMappingService.mapTo(c1, Trip.class)).thenReturn(t1);
        when(beanMappingService.mapTo(d1, Trip.class)).thenReturn(t2);
        when(beanMappingService.mapTo(tList, TripDTO.class)).thenReturn(dList);
        when(beanMappingService.mapTo(t1, TripDTO.class)).thenReturn(d1);
        when(beanMappingService.mapTo(eList, ExcursionDTO.class)).thenReturn(eDTOList);
        when(excursionService.findById(eId)).thenReturn(e);
        when(tripService.findTripWithId(tId)).thenReturn(t1);
        when(tripService.findAllTrips()).thenReturn(tList);
        when(tripService.findTripsByName(name)).thenReturn(tList);
        when(tripService.findTripsBetween(start, end)).thenReturn(tList);
        when(tripService.findTripWithId(tId)).thenReturn(t1);
        when(tripService.findAllSuitableExcursions(t1)).thenReturn(eList);
        when(tripService.findNextTrips(t1, n)).thenReturn(tList);
        when(tripService.createTrip(t2)).thenReturn(t2);
    }

    @Test
    public void testCreateTrip() {
        tripFacade.createTrip(c1);
        verify(tripService).createTrip(t2);
    }

    @Test
    public void testAddExcursion() {
        tripFacade.addExcursion(tId, eId);
        verify(tripService).addExcursion(t1, e);
    }

    @Test
    public void testAddAllExcursions() {
        Set<Long> set = new HashSet<>();
        set.add(eId);
        tripFacade.addAllExcursions(tId, set);
        verify(tripService).addAllExcursions(t1, eSet);
    }

    @Test
    public void testRemoveExcursion() {
        tripFacade.removeExcursion(tId, eId);
        verify(tripService).removeExcursion(t1, e);
    }

    @Test
    public void testUpdateTrip() {
        tripFacade.updateTrip(d1);
        verify(tripService).updateTrip(t2);
    }

    @Test
    public void testDeleteTrip() {
        tripFacade.deleteTrip(tId);
        verify(tripService).deleteTrip(t1);
    }

    @Test
    public void testGetAllTrips() {
        assertThat(tripFacade.getAllTrips()).containsExactly(d1);
    }

    @Test
    public void testGetTripsByName() {
        assertThat(tripFacade.getTripsByName(name)).containsExactly(d1);
    }

    @Test
    public void testGetTripsBetween() {
        assertThat(tripFacade.getTripsBetween(start, end)).containsExactly(d1);
    }

    @Test
    public void testGetTripWithId() {
        assertThat(tripFacade.getTripWithId(tId)).isNotNull().isEqualTo(d1);
    }

    @Test
    public void testGetAllSuitableExcursions() {
        assertThat(tripFacade.getAllSuitableExcursions(tId)).isNotNull().containsExactly(eDTO);
    }

    @Test
    public void testGetNextTrips() {
        assertThat(tripFacade.getNextTrips(tId, n)).isNotNull().containsExactly(d1);
    }
}