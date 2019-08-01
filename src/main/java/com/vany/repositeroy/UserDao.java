package com.vany.repositeroy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vany.model.DAOUser;

@Repository
public interface UserDao extends JpaRepository<DAOUser, Long> {
	DAOUser findByUsername(String username);
}
