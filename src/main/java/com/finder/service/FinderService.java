package com.finder.service;

import java.util.List;
import com.finder.model.FinderInfo;

public interface FinderService {
	Iterable<FinderInfo> findAll();

    List<FinderInfo> search(int q);

    FinderInfo findOne(int id);

    void save(FinderInfo contact);

    void delete(int id);
}
