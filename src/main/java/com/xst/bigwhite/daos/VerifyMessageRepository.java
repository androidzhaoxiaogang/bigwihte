package com.xst.bigwhite.daos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xst.bigwhite.models.Device;
import com.xst.bigwhite.models.VerifyMessage;

@Repository
public interface VerifyMessageRepository  extends JpaRepository<VerifyMessage, Long> {
	Optional<VerifyMessage> findTop1ByMobileno(String mobileno);
	Optional<VerifyMessage> findTop1ByMobilenoAndVerifycode(String mobileno,String verifycode);
}
