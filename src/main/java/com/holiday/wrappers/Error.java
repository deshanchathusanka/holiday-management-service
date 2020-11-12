package com.holiday.wrappers;

/**
 * @author : dchat
 * @since : 11/12/2020, Thu
 **/
public class Error
{
    private int code;
    private String message;

    public Error()
    {
    }

    public Error( int code, String message )
    {
        this.code = code;
        this.message = message;
    }

    public long getCode()
    {
        return ( long ) this.code;
    }

    public void setCode( int code )
    {
        this.code = code;
    }

    public String getMessage()
    {
        return this.message;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }
}
