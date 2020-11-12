package com.holiday.wrappers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * @author : dchat
 * @since : 11/12/2020, Thu
 **/
@JsonInclude( JsonInclude.Include.NON_NULL )
@JsonPropertyOrder( { "status", "error", "data" } )
public class ResponseWrapper<T>
{
    @JsonProperty( "status" )
    private Status status;

    @JsonProperty( "error" )
    private Error error;

    @JsonProperty( "data" )
    List<T> data;

    public static final int SUCCESS = 1;
    public static final int ERROR = -1;
    public static final int WARNING = 0;

    public ResponseWrapper()
    {
        this.data = null;
        this.status = new Status();
    }

    public ResponseWrapper( int statusCode, List<T> data )
    {
        this( statusCode );
        this.setData( data );
    }

    public ResponseWrapper( int statusCode, Error error )
    {
        this( statusCode );
        this.setError( error );
    }

    public ResponseWrapper( int statusCode )
    {
        this();
        this.setStatusCode( ( long ) statusCode );
    }

    public void setStatusCode( long code )
    {
        if( this.status == null )
        {
            this.status = new Status();
        }

        this.status.setCode( Math.toIntExact( code ) );
        if( code == 1L )
        {
            this.status.setMessage( "SUCCESS" );
        }
        else if( code == 0L )
        {
            this.status.setMessage( "WARNING" );
        }
        else if( code == -1L )
        {
            this.status.setMessage( "ERROR" );
        }
        else
        {
            this.status.setMessage( ( String ) null );
        }

    }

    public Status getStatus()
    {
        return status;
    }

    public void setStatus( Status status )
    {
        this.status = status;
    }

    public List<T> getData()
    {
        return data;
    }

    public void setData( List<T> data )
    {
        this.data = data;
    }

    public Error getError()
    {
        return error;
    }

    public void setError( Error error )
    {
        this.error = error;
    }
}
