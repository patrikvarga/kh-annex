package org.khannex.action;

import org.khannex.io.Request;

public class CommandFactory {

    public Command createCommand( Request request ) {
        Command retval = null;

        if ( "setParam".equals( request.getCommand( ) ) ) {
            retval = new SetParam( );
        } else if ( "getLocation".equals( request.getCommand( ) ) ) {
            retval = new GetLocation( );
        } else if ( "setDeviceType".equals( request.getCommand( ) ) ) {
            retval = new SetDeviceType( );
        } else if ( "getDeviceType".equals( request.getCommand( ) ) ) {
            retval = new GetDeviceType( );
        } else if ( "makeSign".equals( request.getCommand( ) ) ) {
            retval = new MakeSign( );
            retval.addParam( request.getParam1( ) );
        } else if ( "getLastError".equals( request.getCommand( ) ) ) {
            retval = new GetLastError( );
        } else if ( "getNonstandardChars".equals( request.getCommand( ) ) ) {
            retval = new getNonstandardChars( );
        } else {
            throw new IllegalArgumentException( String.format( "unknown command [%s]", request.getCommand( ) ) );
        }

        return retval;
    }

}
