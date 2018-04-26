package com.finder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.finder.model.*;

@Repository("validationRepository")
public interface ValidationRepository extends JpaRepository<FinderInfo, Integer> {
	FinderInfo findByRecepti(String recepti);
}
