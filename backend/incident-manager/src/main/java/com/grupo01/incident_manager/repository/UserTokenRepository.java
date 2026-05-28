package com.grupo01.incident_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo01.incident_manager.model.UserToken;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

}
