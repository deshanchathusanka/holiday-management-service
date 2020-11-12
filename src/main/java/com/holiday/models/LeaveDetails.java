package com.holiday.models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author : dchat
 * @since : 11/11/2020, Wed
 **/
@Entity
@Table(name = "leave_details")
public class LeaveDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "covering_emp_username")
    private String coveringEmployee;

    @Column(name = "resolved_by")
    private String resolvedBy;

    @NotNull(message = "Please provide start date!")
    @Column(name = "from_date")
    private LocalDate fromDate;

    @NotNull(message = "Please provide end date!")
    @Column(name = "to_date")
    private LocalDate toDate;

    @NotEmpty(message = "Please select type of leave!")
    @Column(name = "leave_type")
    private String leaveType;

    @NotEmpty(message = "Please provide a reason for the leave!")
    @Column(name = "reason")
    private String reason;

    @Column(name = "duration")
    private int duration;

    @Column(name = "status")
    private String status;

    @Column(name = "active")
    private boolean active;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getEmployeeName() {
	return employeeName;
    }

    public void setEmployeeName(String employeeName) {
	this.employeeName = employeeName;
    }

    public LocalDate getFromDate()
    {
        return fromDate;
    }

    public void setFromDate( LocalDate fromDate )
    {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate()
    {
        return toDate;
    }

    public void setToDate( LocalDate toDate )
    {
        this.toDate = toDate;
    }

    public String getReason() {
	return reason;
    }

    public void setReason(String reason) {
	this.reason = reason;
    }

    public String getLeaveType() {
	return leaveType;
    }

    public void setLeaveType(String leaveType) {
	this.leaveType = leaveType;
    }

    public int getDuration() {
	return duration;
    }

    public void setDuration(int duration) {
	this.duration = duration;
    }

    public boolean isActive() {
	return active;
    }

    public void setActive(boolean active) {
	this.active = active;
    }

    public String getResolvedBy()
    {
        return resolvedBy;
    }

    public void setResolvedBy( String resolvedBy )
    {
        this.resolvedBy = resolvedBy;
    }

    public String getCoveringEmployee()
    {
        return coveringEmployee;
    }

    public void setCoveringEmployee( String coveringEmployee )
    {
        this.coveringEmployee = coveringEmployee;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus( String status )
    {
        this.status = status;
    }

    /**
     * this enum is used to keep the leave status
     */
    public enum Status { PENDING, APPROVED, REJECT }
}
