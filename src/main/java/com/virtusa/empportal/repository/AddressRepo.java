package com.virtusa.empportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.virtusa.empportal.model.Address;

public interface AddressRepo extends JpaRepository<Address, Integer>{

}
