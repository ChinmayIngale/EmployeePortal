package com.virtusa.empportal.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;
import com.virtusa.empportal.model.Employees;
import com.virtusa.empportal.model.EmployeesLeaves;
import com.virtusa.empportal.model.LeaveMaster;
import com.virtusa.empportal.model.LeaveReport;
import com.virtusa.empportal.model.Leaves;
import com.virtusa.empportal.model.Messages;
import com.virtusa.empportal.model.OverboundReport;
import com.virtusa.empportal.model.Reference;
import com.virtusa.empportal.repository.EmployeeRepo;
import com.virtusa.empportal.repository.EmployeesLeavesRepo;
import com.virtusa.empportal.repository.LeaveMasterRepo;
import com.virtusa.empportal.repository.LeavesRepo;
import com.virtusa.empportal.utils.Response;

@Service
public class LeavesService {

	@Autowired
	private LeaveMasterRepo leaveMasterRepo;
	@Autowired
	private EmployeeRepo employeeRepo;
	@Autowired
	private LeavesRepo leavesRepo;
	@Autowired
	private EmployeesLeavesRepo employeesLeavesRepo;
	
	final int outboundlimit = 15;
	LocalDate currentYearStart = LocalDate.of(LocalDate.now().isAfter(LocalDate.of(LocalDate.now().getYear(),3,31)) ? LocalDate.now().getYear() : LocalDate.now().getYear()-1, 4, 1);
	LocalDate currentYearEnd = LocalDate.of(LocalDate.now().isBefore(LocalDate.of(LocalDate.now().getYear(),4,1)) ? LocalDate.now().getYear() : LocalDate.now().getYear()+1, 3, 31);
	

	public Response getLeaveMaster() {
		List<LeaveMaster> leaveMaster = leaveMasterRepo.findAll();
		return new Response(LocalDateTime.now(), HttpStatus.OK, "All leaves type fetched", leaveMaster);
	}

	public Response addLeaveType(LeaveMaster leaveMaster) {
		if (leaveMasterRepo.existsByLeaveType(leaveMaster.getLeaveType())) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST,
					"Leave type " + leaveMaster.getLeaveType() + " already exists", null);
		}
		LeaveMaster newLeaveMaster = leaveMasterRepo.save(leaveMaster);
		return new Response(LocalDateTime.now(), HttpStatus.CREATED, "new Leave type created", newLeaveMaster);
	}

	public Response getEmployeeLeavesInfo(int id) {
		Employees emp = employeeRepo.findById(id).orElse(null);
		if (emp != null) {
			Leaves empLeaves = updateEmployeeLeaveData(emp);
			return new Response(LocalDateTime.now(), HttpStatus.OK, "Leave details", empLeaves);
		} else {
			return new Response(LocalDateTime.now(), HttpStatus.NOT_FOUND, "Employee not found", null);
		}
	}

	public Leaves updateEmployeeLeaveData(Employees emp) {
		Leaves empLeaves = emp.getLeaves();
		List<LeaveMaster> leaveMaster = leaveMasterRepo.findAll();
		int leavesTakenInCurrentYear = 0;
		for (int i = 0; i < leaveMaster.size(); i++) {
			LeaveMaster leave = leaveMaster.get(i);
			int sum = empLeaves.getLeavesTaken().stream().filter(leavesTaken -> leavesTaken.getLeaveMaster() == leave)
					.filter(leaves -> leaves.getFromDate().isAfter(currentYearStart) && leaves.getFromDate().isBefore(currentYearEnd))
					.mapToInt(leaveTaken -> leaveTaken.getDays()).sum();
			leavesTakenInCurrentYear += sum;
			leaveMaster.get(i).setLeavesRemaining(leaveMaster.get(i).getMaxLeaveNumbers() - sum);

		}
		empLeaves.setLeavesTakenInCurrentYear(leavesTakenInCurrentYear);
		empLeaves.setLeavesRemaining(leaveMaster);
		return empLeaves;
	}

	public Response applyForLeaves(int id, EmployeesLeaves employeesLeaves) {
		Employees emp = employeeRepo.findById(id).orElse(null);
		if (emp != null) {
			Leaves empLeaves = updateEmployeeLeaveData(emp);
			LeaveMaster targetLeaveType = null;
			for (LeaveMaster leaveType : empLeaves.getLeavesRemaining()) {
				if (leaveType.getLeavemasterID() == employeesLeaves.getLeaveMaster().getLeavemasterID()) {
					targetLeaveType = leaveType;
				}
			}

			if (employeesLeaves.getDays() > targetLeaveType.getLeavesRemaining()) {
				return new Response(
						LocalDateTime.now(), HttpStatus.BAD_REQUEST, "ERROR! -> Leaves Remaining: "
								+ targetLeaveType.getLeavesRemaining() + " Leaves asked: " + employeesLeaves.getDays(),
						null);
			}
			

			empLeaves.setTotalLeaveTaken(empLeaves.getTotalLeaveTaken()+employeesLeaves.getDays());


			employeesLeaves.setLeaves(empLeaves);
			employeesLeaves.setLeaveMaster(targetLeaveType);
			EmployeesLeaves newLeave= employeesLeavesRepo.save(employeesLeaves);
			
			leavesRepo.save(empLeaves);

			return new Response(LocalDateTime.now(), HttpStatus.OK, "Leave added", newLeave);
		} else {
			return new Response(LocalDateTime.now(), HttpStatus.NOT_FOUND, "Employee not found", null);
		}
	}

	public Response updateLeaveDays(int numberOfLeaves, int leaveID) {
		LeaveMaster leave = leaveMasterRepo.findById(leaveID).orElse(null);
		if(leave != null) {
			leave.setMaxLeaveNumbers(numberOfLeaves);
			LeaveMaster newLeave = leaveMasterRepo.save(leave);
			return new Response(LocalDateTime.now(), HttpStatus.OK, "Leave updated", newLeave);
		} else {
			return new Response(LocalDateTime.now(), HttpStatus.NOT_FOUND, "Leave type not found", null);
		}
	}

	public Response generateLeaveReport(LocalDate fromDate, LocalDate toDate, LeaveMaster leaveType) {
		if(fromDate == null ^ toDate == null) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST,"Both 'fromDate and 'toDate' must be present", null);
		}
		
		List<EmployeesLeaves> employeesLeavesByType;
		if(leaveType != null) {
			LeaveMaster leaveMaster = leaveMasterRepo.findById(leaveType.getLeavemasterID()).orElse(null);
			employeesLeavesByType = employeesLeavesRepo.findAllByLeaveMaster(leaveMaster);
		}
		else {
			employeesLeavesByType = employeesLeavesRepo.findAll();
			
		}
		
		List<EmployeesLeaves> employeesLeaves = new ArrayList<>();
		
		if(fromDate != null) {
			if(!fromDate.isBefore(toDate) && !fromDate.isEqual(toDate)) {
				return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST,"fromDate should be before toDate", null);
			}
			
			for(EmployeesLeaves leave : employeesLeavesByType) {
				if(leave.getFromDate().isAfter(fromDate) && leave.getFromDate().isBefore(toDate)) {
					employeesLeaves.add(leave);
				}
			}
		} else {
			employeesLeaves.addAll(employeesLeavesByType);
		}
		

		Map<Employees, Set<EmployeesLeaves>> report = new HashMap<>();
		for(EmployeesLeaves leave : employeesLeaves) {
			Employees emp = employeeRepo.findByLeaves(leave.getLeaves()).orElse(null);
					
			if(report.containsKey(emp)) {
				report.get(emp).add(leave);
			} else {
				Set<EmployeesLeaves> result = new HashSet<>();
				result.add(leave);
				report.put(emp, result);
			}
			
		}

		List<LeaveReport> reportList = new ArrayList<>();
		for(Map.Entry<Employees, Set<EmployeesLeaves>> entry : report.entrySet()) {
			reportList.add(new LeaveReport(entry.getKey(), entry.getValue()));
		}

		return new Response(LocalDateTime.now(), HttpStatus.OK,"Report fetched", reportList);
	}


	public Response generateOverboundReport() {
		List<Employees> employees = employeeRepo.findAll();
		List<OverboundReport> report = new ArrayList<>();

		final int totalLeaves = leaveMasterRepo.sumOfAllLeaves();
		
		employees.forEach(emp -> {
			emp.setLeaves(updateEmployeeLeaveData(emp));
			int leavesTaken = emp.getLeaves().getLeavesTaken().stream().filter(leave -> leave.getFromDate().isAfter(currentYearStart) && leave.getFromDate().isBefore(currentYearEnd)).mapToInt(leave -> leave.getDays()).sum();
			int leavesRemaining = totalLeaves - leavesTaken;
			int overbound = (leavesRemaining - outboundlimit < 0) ? 0 : leavesRemaining - outboundlimit;
			report.add(new OverboundReport(emp, leavesTaken, leavesRemaining, overbound));
		});
		return new Response(LocalDateTime.now(), HttpStatus.OK,"Report fetched", report);
		
	}

	public Response generateEmployeeOverboundReport(int id) {
		Employees employee = employeeRepo.findById(id).orElse(null);
		if(employee == null) {
			return new Response(LocalDateTime.now(), HttpStatus.NOT_FOUND, "Employee not found", null);
		}

		final int totalLeaves = leaveMasterRepo.sumOfAllLeaves();
		
		employee.setLeaves(updateEmployeeLeaveData(employee));
		int leavesTaken = employee.getLeaves().getLeavesTaken().stream().filter(leave -> leave.getFromDate().isAfter(currentYearStart) && leave.getFromDate().isBefore(currentYearEnd)).mapToInt(leave -> leave.getDays()).sum();
		
		int leavesRemaining = totalLeaves - leavesTaken;
		int overbound = (leavesRemaining - outboundlimit < 0) ? 0 : leavesRemaining - outboundlimit;
		OverboundReport overboundReport = new OverboundReport(employee, leavesTaken, leavesRemaining, overbound);
		return new Response(LocalDateTime.now(), HttpStatus.OK,"Report fetched", overboundReport);
	}

	
	

}
