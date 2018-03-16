package com.finder.service;


import java.util.List;


import com.finder.model.Location;

public interface LocationService {
	Iterable<Location> findAll();
	List<Location> search(int q);
}
