package com.xst.bigwhite.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xst.bigwhite.models.Device;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface  DeviceRepository  extends JpaRepository<Device, Long> {
    Collection<Device> findByAccountUsername(String username);
    Optional<Device> findBySn(String sn);
    Optional<Device> findByno(String no);
}
