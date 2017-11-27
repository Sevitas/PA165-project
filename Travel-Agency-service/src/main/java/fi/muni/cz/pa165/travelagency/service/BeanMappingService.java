package fi.muni.cz.pa165.travelagency.service;


import org.dozer.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * Mapper class
 * @author Martin Sevcik <422365>
 */
public interface BeanMappingService {

    /**
     * MapTo method for mapping collection
     * @param objects collection of objects
     * @param mapToClass desired class
     * @param <T> Type
     * @return mapped collection

     */
    <T> List<T> mapTo(Collection<?> objects, Class<T> mapToClass);

    /**
     * MapTo method for mapping objects
     * @param u object
     * @param mapToClass desired class
     * @param <T> Type
     * @return mapped object, or null if u is null
     */
    <T> T mapTo(Object u, Class<T> mapToClass);

    /**
     * Basic getter
     * @return Mapper
     */
    Mapper getMapper();
}
