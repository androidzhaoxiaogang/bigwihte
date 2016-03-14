package com.xst.bigwhite.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.xst.bigwhite.models.AccountNote;

@SuppressWarnings("rawtypes")
@Repository
public interface AccountNoteRepository extends JpaRepository<AccountNote, Long>, 
										   QueryDslPredicateExecutor<AccountNote> {

}

