package com.holiday.util;

import com.holiday.models.LeaveDetails;
import com.holiday.models.UserInfo;
import com.holiday.service.HolidayManageService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

/**
 * @author : dchat
 * @since : 11/12/2020, Thu
 **/
public class LeaveValidatorTest
{
    @InjectMocks
    private LeaveValidator leaveValidator;

    @Spy
    private HolidayManageService holidayManageService;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateWithCoveringUser_withoutOverlap()
    {
        // mocks
        LeaveDetails leaveDetails = new LeaveDetails();
        leaveDetails.setFromDate( LocalDate.of( 2021,1,10 ) );
        leaveDetails.setToDate( LocalDate.of( 2021,1,20 ) );
        UserInfo coveringEmp = new UserInfo();

        LeaveDetails leavePeriod1 = new LeaveDetails();
        leavePeriod1.setFromDate( LocalDate.of( 2021,2,10 ) );
        leavePeriod1.setToDate( LocalDate.of( 2021,2,20 ) );
        LeaveDetails leavePeriod2 = new LeaveDetails();
        leavePeriod2.setFromDate( LocalDate.of( 2021,3,10 ) );
        leavePeriod2.setToDate( LocalDate.of( 2021,3,20 ) );
        List<LeaveDetails> leaveDetailsList=new ArrayList<>( Arrays.asList(leavePeriod1,leavePeriod2) );
        doReturn(leaveDetailsList)
                .when( holidayManageService )
                .getAllLeavesOfUser( anyString() );

        // method invocation
        boolean isValid = leaveValidator.validateWithCoveringUser( leaveDetails, coveringEmp );

        // assertions
        Assert.assertTrue( isValid );
    }

    @Test
    public void validateWithCoveringUser_withOverlap()
    {
        // mocks
        LeaveDetails leaveDetails = new LeaveDetails();
        leaveDetails.setFromDate( LocalDate.of( 2021,1,10 ) );
        leaveDetails.setToDate( LocalDate.of( 2021,1,20 ) );
        UserInfo coveringEmp = new UserInfo();

        LeaveDetails leavePeriod1 = new LeaveDetails();
        leavePeriod1.setFromDate( LocalDate.of( 2021,1,15 ) );
        leavePeriod1.setToDate( LocalDate.of( 2021,1,25 ) );
        LeaveDetails leavePeriod2 = new LeaveDetails();
        leavePeriod2.setFromDate( LocalDate.of( 2021,3,10 ) );
        leavePeriod2.setToDate( LocalDate.of( 2021,3,20 ) );
        List<LeaveDetails> leaveDetailsList=new ArrayList<>( Arrays.asList(leavePeriod1,leavePeriod2) );
        doReturn(leaveDetailsList)
                .when( holidayManageService )
                .getAllLeavesOfUser( anyString() );

        // method invocation
        boolean isValid = leaveValidator.validateWithCoveringUser( leaveDetails, coveringEmp );

        // assertions
        Assert.assertFalse( isValid );
    }

    @Test
    public void validateAvailability()
    {
        // mocks
        LeaveDetails leaveDetails = new LeaveDetails();
        leaveDetails.setFromDate( LocalDate.of( 2021,1,10 ) );
        leaveDetails.setToDate( LocalDate.of( 2021,1,20 ) );
        leaveDetails.setDuration( 10 );
        UserInfo employee = new UserInfo();

        LeaveDetails leavePeriod1 = new LeaveDetails();
        leavePeriod1.setFromDate( LocalDate.of( 2021,2,15 ) );
        leavePeriod1.setToDate( LocalDate.of( 2021,2,27 ) );
        leavePeriod1.setDuration( 12 );
        LeaveDetails leavePeriod2 = new LeaveDetails();
        leavePeriod2.setFromDate( LocalDate.of( 2021,3,10 ) );
        leavePeriod2.setToDate( LocalDate.of( 2021,3,15 ) );
        leavePeriod2.setDuration( 5 );
        List<LeaveDetails> leaveDetailsList=new ArrayList<>( Arrays.asList(leavePeriod1,leavePeriod2) );
        doReturn(leaveDetailsList)
                .when( holidayManageService )
                .getAllLeavesOfUser( anyString() );

        // method invocation
        boolean isAvailable=leaveValidator.validateAvailability( employee,leaveDetails );

        //assertions
        Assert.assertTrue( isAvailable );

    }
}