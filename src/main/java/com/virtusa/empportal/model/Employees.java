package com.virtusa.empportal.model;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Entity
public class Employees {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_ID")
	private int empID;
	
	@Column(nullable = false, unique = true)
	@Email
	protected String email;
    
    @Column(nullable = false)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,}", message = "Password is not in correct format")
	protected String password;
    
    @Column(nullable = false)
    @NotBlank(message = "Employee name cannot be blank")
    @Size(min = 2, message = "Name should have atleast 2 characters")
	private String empName;
    
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Username cannot be blank")
	private String username;

    @ManyToOne
    @JoinColumn(referencedColumnName = "designation_ID", nullable = false)
    @JsonIgnoreProperties({"employees","recivedMessages"})
//    @ToString.Exclude
	private Designation designation;
    
    @Column(nullable = false)
    @NotBlank(message = "Employee gender cannot be blank")
	private String gender;
    
    @ManyToOne
    @JoinColumn(referencedColumnName = "department_ID", nullable = false)
    @JsonIgnoreProperties({"employees","recivedMessages"})
//    @ToString.Exclude
	private Department empDepartment;

    @Column(nullable = false)
	private boolean active;

    @Column(nullable = false)
	private LocalDate joiningDate;
    
    @Column(nullable = true)
	private LocalDate terminationDate;

    @Column(nullable = false)
	private int salary;

    @OneToMany(cascade = CascadeType.ALL)
	private List<Address> empAddress;

    @Column(nullable = false)
    private long phoneNumber;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "leaves_ID")
//    @ToString.Exclude
    private Leaves leaves;
    
    @ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "User_Roles",
				joinColumns = {@JoinColumn(name = "emp_ID")},
				inverseJoinColumns = {@JoinColumn(name = "role_ID")})
    private Set<Roles> roles;
	
    @OneToMany(mappedBy = "sender")
    private Set<Messages> sentMessages;
    
    @ManyToMany(mappedBy = "deliverToEmployees")
    private Set<Messages> recivedMessages;
}
