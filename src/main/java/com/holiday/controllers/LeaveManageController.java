package com.holiday.controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.holiday.util.LeaveValidator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.holiday.models.LeaveDetails;
import com.holiday.models.UserInfo;
import com.holiday.service.LeaveManageService;
import com.holiday.service.UserInfoService;

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

        int duration = leaveDetails.getToDate().getDate() - leaveDetails.getFromDate().getDate();
        leaveDetails.setDuration( duration + 1 );
        leaveDetails.setActive( true );
        leaveDetails.setUsername( employee.getEmail() );
        leaveDetails.setEmployeeName( employee.getFirstName() + " " + employee.getLastName() );
        leaveDetails.setStatus( LeaveDetails.Status.PENDING.name() );

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

    @RequestMapping( value = "/user/my-leaves", method = RequestMethod.GET )
    public ResponseEntity<List<LeaveDetails>> showMyLeaves( @RequestParam( required = true ) String username )
    {
        UserInfo userInfo = userInfoService.findUserByEmail( username );
        List<LeaveDetails> leavesList = leaveManageService.getAllLeavesOfUser( userInfo.getEmail() );
        return new ResponseEntity<>( leavesList, HttpStatus.OK );
    }
}
