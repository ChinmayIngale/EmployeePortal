package com.virtusa.empportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.virtusa.empportal.model.Messages;

public interface MessagesRepo extends JpaRepository<Messages, Integer>{

}
