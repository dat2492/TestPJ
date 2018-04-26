package com.finder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finder.model.FinderInfo;
import com.finder.model.Location;
import com.finder.repository.LocationRepository;

@Service
public class LocationServiceImpl implements LocationService {
	@Autowired
    private LocationRepository locationRepository;

    @Override
    public Iterable<Location> findAll() {
        return locationRepository.findAll();
    }
    @Override
    public List<Location> search(int q) {
        return locationRepository.findByIdIs(q);
    }

}

