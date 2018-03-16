package com.finder.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.finder.model.*;

public interface FinderRepository extends CrudRepository<FinderInfo, Integer> {

    List<FinderInfo> findByIdIs(int q);

}
