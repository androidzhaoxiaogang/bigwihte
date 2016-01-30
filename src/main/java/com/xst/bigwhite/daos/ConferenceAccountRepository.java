package com.xst.bigwhite.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.xst.bigwhite.models.ConferenceAccount;

@SuppressWarnings("rawtypes")
@Repository
public interface ConferenceAccountRepository extends JpaRepository<ConferenceAccount, Long>, 
			QueryDslPredicateExecutor<ConferenceAccount>{

}
