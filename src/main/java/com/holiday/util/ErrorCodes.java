package com.holiday.util;

/**
 * @author : dchat
 * @since : 11/12/2020, Thu
 **/
public enum ErrorCodes
{
    COVERING_USER_ERROR( 1000 ),
    AVAILABILITY_ERROR( 1001 );
    private int code;

    ErrorCodes( int code )
    {
        this.code = code;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode( int code )
    {
        this.code = code;
    }
}
