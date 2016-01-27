package com.xst.bigwhite.daos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xst.bigwhite.models.Device;
import com.xst.bigwhite.models.VerifyMessage;

@Repository
public interface VerifyMessageRepository  extends JpaRepository<VerifyMessage, Long> {
	Optional<VerifyMessage> findByMobileno(String mobileno);
	Optional<VerifyMessage> findByMobilenoAndVerifycode(String mobileno,String verifycode);
}
