package com.holiday.util;

import com.holiday.models.LeaveDetails;
import com.holiday.models.UserInfo;
import com.holiday.service.LeaveManageService;
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
    private LeaveManageService leaveManageService;

    public boolean validateWithCoveringUser( LeaveDetails leaveDetails, UserInfo coveringEmployee )
    {
        List<LeaveDetails> leaveDetailsList = leaveManageService.getAllLeavesOfUser( coveringEmployee.getEmail() );
        if( leaveDetailsList == null )
        {
            return true;
        }
        for( LeaveDetails coveringEmpRange : leaveDetailsList )
        {
            if( isDuplicate( leaveDetails, coveringEmpRange ) )
            {
                return false;
            }
        }
        return true;
    }

    public boolean isDuplicate( LeaveDetails empRange, LeaveDetails coveringEmpRange )
    {
        return !empRange.getToDate().before( coveringEmpRange.getFromDate() ) &&
                       !coveringEmpRange.getToDate().before( empRange.getFromDate() );
    }

    public boolean validateAvailability( UserInfo employee, LeaveDetails leaveDetails )
    {
        List<LeaveDetails> leaveDetailsList = leaveManageService.getAllLeavesOfUser( employee.getEmail() );
        int exhaustedLeaves = Optional.ofNullable( leaveDetailsList )
                                      .orElse( Collections.emptyList() )
                                      .stream()
                                      .mapToInt( LeaveDetails::getDuration )
                                      .sum();

        int remainingLeaves = 30 - exhaustedLeaves;

        return remainingLeaves >= leaveDetails.getDuration();
    }
}
