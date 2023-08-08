package com.virtusa.empportal.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.virtusa.empportal.model.LeaveMaster;
import com.virtusa.empportal.service.LeavesService;
import com.virtusa.empportal.utils.Response;

import jakarta.annotation.Nullable;

@RestController
@RequestMapping(path = "/leaveMaster")
public class LeaveMasterController {
	
	@Autowired
	private LeavesService leavesService;

	@GetMapping
	public Response getAllLeaveType() {
		return leavesService.getLeaveMaster();
	}
	
	@PostMapping("/addLeave")
	public Response addLeave(@RequestBody LeaveMaster leaveMaster) {
		return leavesService.addLeaveType(leaveMaster);
	}
	
	@PatchMapping("/update")
	public Response updateLeave(@RequestParam String numberOfLeaves, @RequestParam String leaveID) {
		try {
			int numberOfLeavesInt = Integer.parseInt(numberOfLeaves);
			int leaveIDInt = Integer.parseInt(leaveID);
			return leavesService.updateLeaveDays(numberOfLeavesInt, leaveIDInt);
		} catch(NumberFormatException exception) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, exception.getMessage(), "Can not convert to integer");
		}
	}
	
	@GetMapping("/leaveReport")
	public Response getLeaveReport(@RequestParam @Nullable String from, @RequestParam @Nullable String to, @RequestBody @Nullable LeaveMaster leaveType) {
		try {
			LocalDate fromDate = null,toDate = null;
			if(from != null) {
			fromDate = LocalDate.parse(from, DateTimeFormatter.ISO_LOCAL_DATE);
			}

			if(from != null) {
			toDate = LocalDate.parse(to, DateTimeFormatter.ISO_LOCAL_DATE);
			}
			return leavesService.generateLeaveReport(fromDate,toDate,leaveType);
		} catch(DateTimeParseException exception) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "can not parse "+exception.getParsedString(), "Date should be in 'yyyy-MM-dd' format");
		}
	}
	
	@GetMapping("/overboundReport")
	public Response getOverBoundReport() {
		return leavesService.generateOverboundReport();
	}
}
