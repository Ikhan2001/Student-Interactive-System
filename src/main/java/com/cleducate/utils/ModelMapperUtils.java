package com.cleducate.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ModelMapperUtils {

    private static final ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    }

    public static<D, T> D map(final T entity, Class<D> outClass){
        return modelMapper.map(entity, outClass);
    }

    public static<T, D> T maptoEntity(final D dto, Class<T> outClass){
        return modelMapper.map(dto, outClass);
    }

    public static<D, T> List<D> mapAllToList(final Collection<T> entityList, Class<D> outClass){
        return entityList.stream()
                .map((entity)-> map(entity, outClass))
                .collect(Collectors.toList());
    }

    public static<D, T> Set<D> mapAllToSet(final Collection<T> entitySet, Class<D> outClass){
        return entitySet.stream()
                .map((entity)-> map(entitySet, outClass))
                .collect(Collectors.toSet());
    }



}
