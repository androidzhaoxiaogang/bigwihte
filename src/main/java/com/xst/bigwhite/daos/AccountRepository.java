package com.xst.bigwhite.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xst.bigwhite.models.Account;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findTop1ByUsernameAndMobileno(String username,String mobileno);
    Optional<Account> findTop1ByMobilenoAndPassword(String mobileno,String password);
    Optional<Account> findTop1ByMobileno(String mobileno);
}