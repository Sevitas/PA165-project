package fi.muni.cz.pa165.travelagency.service;

import fi.muni.cz.pa165.travelagency.dao.ExcursionDao;
import fi.muni.cz.pa165.travelagency.entity.Excursion;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.dozer.inject.Inject;
import org.springframework.stereotype.Service;

/**
 * Excursion service Implementation for the Travel Angecy project.
 * 
 * @author (name = "Nermin Jukan", UCO = "<473370>")
 */
@Service
public class ExcursionServiceImpl implements ExcursionService{
    
    @Inject
    private ExcursionDao excursionDao;

    @Override
    public Excursion create(Excursion excursion) {
        excursionDao.create(excursion);
        return excursion;
    }

    @Override
    public Excursion findById(Long id) {
        return excursionDao.findById(id);
    }

    @Override
    public List<Excursion> getAllExcursions() {
        return excursionDao.findAll();
    }

    @Override
    public List<Excursion> findByDestination(String destination) {
        return excursionDao.findByDestination(destination);
    }

    @Override
    public void deleteExcursion(Excursion excursion) {
        excursionDao.delete(excursion);
    }

    @Override
    public Excursion updateExcursion(Excursion excursion) {
        return excursionDao.update(excursion);
    }

    @Override
    public List<Excursion> findByPriceLowerThanOrEqual(Integer price) {
        List<Excursion> excursionsFound = new ArrayList<>();
        
        List<Excursion> findAll = excursionDao.findAll();
        
        BigDecimal priceBD = new BigDecimal(price);
        int res;
        for(Excursion exc : findAll){
            res = exc.getPrice().compareTo(priceBD);
            if(res == 0 || res == -1){
                excursionsFound.add(exc);
            }
        }
        return excursionsFound;
    }
    
    @Override
    public List<Excursion> findByPriceHigherThanOrEqual(Integer price) {
        List<Excursion> excursionsFound = new ArrayList<>();
                
        BigDecimal priceBD = new BigDecimal(price);
        int res;
        for(Excursion exc : excursionDao.findAll()){
            res = exc.getPrice().compareTo(priceBD);
            if(res == 0 || res == 1){
                excursionsFound.add(exc);
            }
        }
        return excursionsFound;
    }

    @Override
    public List<Excursion> findByDuration(Integer duration) {
        List<Excursion> excursionsFound = new ArrayList<>();
                
        for(Excursion exc : excursionDao.findAll()){
            if(exc.getDuration() == duration){
                excursionsFound.add(exc);
            }
        }
        return excursionsFound;
    }
    
}
