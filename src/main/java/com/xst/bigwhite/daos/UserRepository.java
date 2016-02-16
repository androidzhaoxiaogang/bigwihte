package com.xst.bigwhite.daos;

import org.springframework.data.repository.CrudRepository;

import com.xst.bigwhite.models.User;

public interface UserRepository extends CrudRepository<User, Long> {

	User findByLogin(String login);
}
