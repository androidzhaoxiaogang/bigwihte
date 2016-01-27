package com.xst.bigwhite.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xst.bigwhite.models.Conference;


@SuppressWarnings("rawtypes")
@Repository
public interface ConferenceRepository  extends JpaRepository<Conference, Long>{

}
