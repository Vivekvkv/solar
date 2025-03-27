package com.solcare.solar.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solcare.solar.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

//	Role findByName(String name);
	public Optional<Role> findByName(String name);
}
