package com.holiday.controllers;

import com.holiday.models.LeaveDetails;
import com.holiday.models.UserInfo;
import com.holiday.service.HolidayManageService;
import com.holiday.service.UserInfoService;
import com.holiday.util.ErrorCodes;
import com.holiday.util.LeaveValidator;
import com.holiday.wrappers.Error;
import com.holiday.wrappers.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * @author : dchat
 * @since : 11/11/2020, Wed
 **/
@Controller
public class HolidayManageController
{

    @Autowired
    private HolidayManageService holidayManageService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private LeaveValidator leaveValidator;

    @RequestMapping( value = "/user/apply-leave", method = RequestMethod.POST )
    public ResponseEntity<ResponseWrapper<LeaveDetails>> applyLeave( @RequestBody LeaveDetails leaveDetails )
    {
        UserInfo employee = userInfoService.findUserByEmail( leaveDetails.getUsername() );
        UserInfo coveringEmployee = userInfoService.findUserByEmail( leaveDetails.getCoveringEmployee() );

        if( coveringEmployee == null )
        {
            return new ResponseEntity<>( new ResponseWrapper<>( ResponseWrapper.ERROR,
                    new Error( ErrorCodes.COVERING_USER_ERROR.getCode(), "Invalid Covering Employee Details" ) ),
                    HttpStatus.BAD_REQUEST );
        }

        if( !leaveValidator.validateWithCoveringUser( leaveDetails, coveringEmployee ) )
        {
            return new ResponseEntity<>( new ResponseWrapper<>( ResponseWrapper.ERROR,
                    new Error( ErrorCodes.COVERING_USER_ERROR.getCode(), "Overlapping periods with covering user" ) ),
                    HttpStatus.BAD_REQUEST );
        }

        long duration = DAYS.between( leaveDetails.getFromDate(), leaveDetails.getToDate() );
        updateFields( leaveDetails, employee, duration );

        if( duration > 20 )
        {
            return new ResponseEntity<>( new ResponseWrapper<>( ResponseWrapper.ERROR,
                    new Error( ErrorCodes.AVAILABILITY_ERROR.getCode(), "Exceed maximum duration : 20 days" ) ),
                    HttpStatus.BAD_REQUEST );
        }

        if( !leaveValidator.validateAvailability( employee, leaveDetails ) )
        {
            return new ResponseEntity<>( new ResponseWrapper<>( ResponseWrapper.ERROR,
                    new Error( ErrorCodes.AVAILABILITY_ERROR.getCode(), "Requested leaves exceed availability" ) ),
                    HttpStatus.BAD_REQUEST );
        }

        holidayManageService.applyLeave( leaveDetails );
        return new ResponseEntity<>( new ResponseWrapper<LeaveDetails>( ResponseWrapper.SUCCESS, Arrays.asList( leaveDetails ) ), HttpStatus.OK );
    }

    /**
     * update fields in leave details
     *
     * @param leaveDetails {@link LeaveDetails}
     * @param employee     {@link UserInfo}
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
    public ResponseEntity<ResponseWrapper<LeaveDetails>> getMyLeaves( @RequestParam( required = true ) String username )
    {
        UserInfo userInfo = userInfoService.findUserByEmail( username );
        List<LeaveDetails> leavesList = holidayManageService.getAllLeavesOfUser( userInfo.getEmail() );
        return new ResponseEntity<>( new ResponseWrapper<>( ResponseWrapper.SUCCESS, leavesList ), HttpStatus.OK );
    }
}
