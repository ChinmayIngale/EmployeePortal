package com.virtusa.empportal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
public class LeaveMaster {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leavemaster_ID")
	private int leavemasterID;

	private String leaveType;
	private int maxLeaveNumbers;
	
	@Transient
	private int leavesRemaining;
	
}
