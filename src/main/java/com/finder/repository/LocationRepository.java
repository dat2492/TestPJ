package com.finder.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.finder.model.*;

public interface LocationRepository extends CrudRepository<Location, Integer> {
	List<Location> findByIdIs(int q);

}
