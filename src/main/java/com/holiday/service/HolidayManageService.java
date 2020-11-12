package com.holiday.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holiday.models.LeaveDetails;
import com.holiday.repository.HolidayManageNativeSqlRepo;
import com.holiday.repository.HolidayManageRepository;

/**
 * @author : dchat
 * @since : 11/11/2020, Wed
 **/
@Service( value = "leaveManageService" )
public class HolidayManageService
{

    @Autowired
    private HolidayManageRepository holidayManageRepository;

    @Autowired
    private HolidayManageNativeSqlRepo leaveManageNativeRepo;

    @SuppressWarnings( "deprecation" )
    public void applyLeave( LeaveDetails leaveDetails )
    {
        holidayManageRepository.save( leaveDetails );
    }

    public List<LeaveDetails> getAllLeaves()
    {

        return holidayManageRepository.findAll();
    }

    public LeaveDetails getLeaveDetailsOnId( int id )
    {

        return holidayManageRepository.findOne( id );
    }

    public void updateLeaveDetails( LeaveDetails leaveDetails )
    {

        holidayManageRepository.save( leaveDetails );

    }

    public List<LeaveDetails> getAllActiveLeaves()
    {

        return holidayManageRepository.getAllActiveLeaves();
    }

    public List<LeaveDetails> getAllLeavesOfUser( String username )
    {

        return holidayManageRepository.getAllLeavesOfUser( username );

    }

    public List<LeaveDetails> getAllLeavesOnStatus( boolean pending, boolean accepted, boolean rejected )
    {

        StringBuffer whereQuery = new StringBuffer();
        if( pending )
            whereQuery.append( "active=true or " );
        if( accepted )
            whereQuery.append( "(active=false and accept_reject_flag=true) or " );
        if( rejected )
            whereQuery.append( "(active=false and accept_reject_flag=false) or " );

        whereQuery.append( " 1=0" );

        return leaveManageNativeRepo.getAllLeavesOnStatus( whereQuery );
    }
}
