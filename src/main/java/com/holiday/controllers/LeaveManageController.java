package com.holiday.controllers;

import com.holiday.models.LeaveDetails;
import com.holiday.models.UserInfo;
import com.holiday.service.LeaveManageService;
import com.holiday.service.UserInfoService;
import com.holiday.util.LeaveValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * @author : dchat
 * @since : 11/11/2020, Wed
 **/
@Controller
public class LeaveManageController
{

    @Autowired
    private LeaveManageService leaveManageService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private LeaveValidator leaveValidator;

    @RequestMapping( value = "/user/apply-leave", method = RequestMethod.POST )
    public ResponseEntity<LeaveDetails> submitApplyLeave( @RequestBody LeaveDetails leaveDetails )
    {
        UserInfo employee = userInfoService.findUserByEmail( leaveDetails.getUsername() );
        UserInfo coveringEmployee = userInfoService.findUserByEmail( leaveDetails.getCoveringEmployee() );

        if( coveringEmployee == null )
        {
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
        }

        if( !leaveValidator.validateWithCoveringUser( leaveDetails, coveringEmployee ) )
        {
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
        }

        long duration = DAYS.between( leaveDetails.getFromDate(), leaveDetails.getToDate() );
        updateFields( leaveDetails, employee, duration );

        if( duration > 20 )
        {
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
        }

        if( !leaveValidator.validateAvailability( employee, leaveDetails ) )
        {
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
        }

        leaveManageService.applyLeave( leaveDetails );
        return new ResponseEntity<>( leaveDetails, HttpStatus.OK );
    }

    /**
     * update fields in leave details
     * @param leaveDetails {@link LeaveDetails}
     * @param employee {@link UserInfo}
     * @param duration
     */
    private void updateFields( @RequestBody LeaveDetails leaveDetails, UserInfo employee, long duration )
    {
        leaveDetails.setDuration( ( int ) ( duration + 1 ) );
        leaveDetails.setActive( true );
        leaveDetails.setUsername( employee.getEmail() );
        leaveDetails.setEmployeeName( employee.getFirstName() + " " + employee.getLastName() );
        leaveDetails.setStatus( LeaveDetails.Status.PENDING.name() );
    }

    @RequestMapping( value = "/user/my-leaves", method = RequestMethod.GET )
    public ResponseEntity<List<LeaveDetails>> showMyLeaves( @RequestParam( required = true ) String username )
    {
        UserInfo userInfo = userInfoService.findUserByEmail( username );
        List<LeaveDetails> leavesList = leaveManageService.getAllLeavesOfUser( userInfo.getEmail() );
        return new ResponseEntity<>( leavesList, HttpStatus.OK );
    }
}
