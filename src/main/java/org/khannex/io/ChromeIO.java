package org.khannex.io;

import java.io.IOException;
import java.io.InputStreamReader;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChromeIO {
    private static final Logger LOG = LoggerFactory.getLogger( ChromeIO.class );

    private InputStreamReader stream;
    private ObjectMapper mapper;

    public ChromeIO( ) {
        this.stream = new InputStreamReader( System.in );
        this.mapper = new ObjectMapper( );
    }

    public Request getRequest() throws IOException {
        String msg = getMessage( );

        LOG.debug( String.format( "in=[%s]", msg ) );

        return fromJson( msg, Request.class );
    }

    public void sendResponse( Response response ) throws IOException {
        String msg = toJson( response );

        LOG.debug( String.format( "out=[%s]", msg ) );

        sendMessage( msg );
    }

    private String getMessage() throws IOException {
        BufferBuilder buffer = new BufferBuilder( 4 );
        int input = 0;
        while ( ( input = stream.read( ) ) != -1 ) {
            buffer.put( ( byte ) input );

            if ( buffer.isFull( ) ) {
                int inputLength = buffer.buildInt( );
                char[ ] chars = new char[ inputLength ];
                stream.read( chars );

                return String.valueOf( chars );
            }
        }

        return null;
    }

    private <T> T fromJson( String json, Class<T> type ) throws IOException {
        if ( json != null && !json.isEmpty( ) ) {
            return mapper.readValue( json, type );
        }

        return null;
    }

    private void sendMessage( String message ) throws IOException {
        System.out.write( encodeMessageBytes( message ) );
    }

    private byte[ ] encodeMessageBytes( String message ) throws IOException {
        byte[ ] msg = message.getBytes( );
        byte[ ] length = new BufferBuilder( 4 ).put( msg.length ).buildBytes( );

        byte[ ] retval = new byte[ length.length + msg.length ];
        System.arraycopy( length, 0, retval, 0, 4 );
        System.arraycopy( msg, 0, retval, 4, msg.length );

        return retval;
    }

    private String toJson( Object object ) throws IOException {
        return mapper.writeValueAsString( object );
    }

}