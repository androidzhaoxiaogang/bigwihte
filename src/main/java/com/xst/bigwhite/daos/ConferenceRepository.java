package com.xst.bigwhite.daos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xst.bigwhite.models.Conference;


@SuppressWarnings("rawtypes")
@Repository
public interface ConferenceRepository  extends JpaRepository<Conference, Long>{
	Optional<Conference> findTop1BySessionId(String sessionId);
}
