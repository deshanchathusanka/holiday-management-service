package com.holiday.util;

import com.holiday.models.LeaveDetails;
import com.holiday.models.UserInfo;
import com.holiday.service.HolidayManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author : dchat
 * @since : 11/11/2020, Wed
 **/
@Service
public class LeaveValidator
{
    @Autowired
    private HolidayManageService holidayManageService;

    /**
     * validate overlapping leave periods with covering employee
     *
     * @param leaveDetails     {@link LeaveDetails}
     * @param coveringEmployee {@link UserInfo}
     * @return
     */
    public boolean validateWithCoveringUser( LeaveDetails leaveDetails, UserInfo coveringEmployee )
    {
        List<LeaveDetails> leaveDetailsList = holidayManageService.getAllLeavesOfUser( coveringEmployee.getEmail() );
        if( leaveDetailsList == null )
        {
            return true;
        }
        for( LeaveDetails coveringEmpRange : leaveDetailsList )
        {
            if( isOverlap( leaveDetails, coveringEmpRange ) )
            {
                return false;
            }
        }
        return true;
    }

    /**
     * check overlapping leave periods
     *
     * @param empRange         {@link LeaveDetails}
     * @param coveringEmpRange {@link LeaveDetails}
     * @return is overlapping duration
     */
    private boolean isOverlap( LeaveDetails empRange, LeaveDetails coveringEmpRange )
    {
        return !empRange.getToDate().isBefore( coveringEmpRange.getFromDate() ) &&
                       !coveringEmpRange.getToDate().isBefore( empRange.getFromDate() );
    }

    /**
     * check whether requested leave period is eligible for the employee
     *
     * @param employee     {@link UserInfo}
     * @param leaveDetails {@link LeaveDetails}
     * @return true if requested leave period is eligible for the employee
     */
    public boolean validateAvailability( UserInfo employee, LeaveDetails leaveDetails )
    {
        List<LeaveDetails> leaveDetailsList = holidayManageService.getAllLeavesOfUser( employee.getEmail() );
        int exhaustedLeaves = Optional.ofNullable( leaveDetailsList )
                                      .orElse( Collections.emptyList() )
                                      .stream()
                                      .mapToInt( LeaveDetails::getDuration )
                                      .sum();

        int remainingLeaves = 30 - exhaustedLeaves;

        return remainingLeaves >= leaveDetails.getDuration();
    }
}
