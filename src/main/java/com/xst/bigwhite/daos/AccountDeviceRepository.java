package com.xst.bigwhite.daos;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.xst.bigwhite.models.AccountDevice;

@SuppressWarnings("rawtypes")
@Repository
public interface AccountDeviceRepository extends JpaRepository<AccountDevice, Long>, 
												 QueryDslPredicateExecutor<AccountDevice> {

}
