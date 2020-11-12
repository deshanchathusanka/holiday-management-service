package com.holiday.controllers;

import com.holiday.models.LeaveDetails;
import com.holiday.models.UserInfo;
import com.holiday.service.HolidayManageService;
import com.holiday.service.UserInfoService;
import com.holiday.util.LeaveValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author : dchat
 * @since : 11/12/2020, Thu
 **/
public class HolidayManageControllerTest
{

    private static MockMvc mockMvc;

    @InjectMocks
    private HolidayManageController holidayManageController;

    @Spy
    private HolidayManageService holidayManageService;

    @Spy
    private UserInfoService userInfoService;

    @Spy
    private LeaveValidator leaveValidator;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks( this );
        mockMvc = MockMvcBuilders.standaloneSetup( holidayManageController ).build();
    }

    @Test
    public void getMyLeaves() throws Exception
    {
        LeaveDetails leaveDetails = new LeaveDetails();
        leaveDetails.setFromDate( LocalDate.of( 2021, 1, 1 ) );
        leaveDetails.setToDate( LocalDate.of( 2021, 1, 10 ) );
        leaveDetails.setUsername( "deshan@gmail.com" );
        leaveDetails.setCoveringEmployee( "prasanna@gmail.com" );
        leaveDetails.setReason( "leave" );
        leaveDetails.setLeaveType( "CASUAL LEAVE" );

        doReturn( new UserInfo() )
                .when( userInfoService )
                .findUserByEmail( anyString() );

        doReturn( Arrays.asList( leaveDetails ) )
                .when( holidayManageService )
                .getAllLeavesOfUser( anyString() );

        mockMvc.perform( MockMvcRequestBuilders
                                 .get( "/user/my-leaves" )
                                 .param( "username", "deshan@gmail.com" ) )
               .andExpect( status().isOk() );
    }
}