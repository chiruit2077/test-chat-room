package com.gmail.hasszhao.chatroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

public final class Util {
    private static final String TAG                 = Util.class.getName();
    public static final String  MIME_TYPE_TEXT_HTML = "text/html";
    public static final String  UTF_8               = "UTF-8";
    private static final int    STREAM_CACHE        = 1024 * 1024;

    @SuppressWarnings("serial")
    private static class ConnectorPostConnectException extends IOException {
        private int mStatus;

        public ConnectorPostConnectException( int _status ) {
            super();
            mStatus = _status;
        }

        public int getStatus() {
            return mStatus;
        }
    }

    public static class Connector extends AsyncTask<String, Object, Object> {
        private static final int DEFAULT_CNN_TIME_OUT = 1000 * 20;
        private static String    USER_AGENT;
        private String           mUrl;
        private byte[]           mBody;

        public Connector( Context _cxt ) {
            super();
            if( TextUtils.isEmpty( USER_AGENT ) ) {
                // Without this, the default provided by API will be like "Dalvik/1.6.0 (Linux; U; Android 4.0.4; BASE_Lutea_3 Build/IMM76D)" .
                USER_AGENT = new WebView( _cxt ).getSettings().getUserAgentString();
            }
        }

        /*
         * Convenient function to execute the connecting task.
         */
        public void submit( String _url ) {
            this.execute( _url );
        }

        @Override
        protected Object doInBackground( String... _params ) {
            mUrl = _params[0];
            Object ret = null;
            HttpURLConnection conn = null;
            try {
                synchronized( Connector.class ) {
                    InputStream in = null;
                    conn = connect( mUrl );
                    // if we don't do conn.setRequestProperty( "Accept-Encoding", "gzip" ), the wrapper GZIPInputStream can be removed.
                    onConnectorInputStream( in = new GZIPInputStream( conn.getInputStream() ) );
                    in.close();
                    in = null;
                }
            }
            catch( Exception _e ) {
                ret = _e;
            }
            finally {
                if( conn != null ) {
                    conn.disconnect();
                    conn = null;
                }
            }
            return ret;
        }

        @Override
        protected void onPostExecute( Object _result ) {
            if( _result instanceof SocketTimeoutException ) {
                onConnectorConnectTimout();
            } else if( _result instanceof ConnectorPostConnectException ) {
                onConnectorError( ((ConnectorPostConnectException) _result).getStatus() );
            } else if( _result instanceof Exception ) {
                onConnectorInvalidConnect( (Exception) _result );
            } else if( _result == null ) {
                onConnectorFinished();
            }
            handleEstablishedConnection();
        }

        /*
         * Internal help and test function.
         */
        private static void handleEstablishedConnection() {
            try {
                Log.v( TAG, "Connection is established." );
                CookieStore cs = CookieManager.getInstance().getCookieStore();
                if( cs != null ) {
                    Log.v( TAG, "------------cookies------------" );
                    List<Cookie> list = cs.getCookies();
                    if( list != null && list.size() > 0 ) {
                        StringBuilder cookieBuilder = new StringBuilder();
                        for( Cookie c : list ) {
                            cookieBuilder
                                    .append( c.getName().trim() )
                                    .append( "=>" )
                                    .append( c.getValue().trim() )
                                    .append( "=>" )
                                    .append( c.getDomain() );
                            Log.v( TAG, cookieBuilder.toString() );
                            cookieBuilder.delete( 0, cookieBuilder.length() - 1 );
                        }
                        cookieBuilder = null;
                    } else {
                        Log.v( TAG, "Empty cookies." );
                    }
                    cs = null;
                    list = null;
                }
            }
            catch( Exception _e ) {
                Log.e( TAG, "Error in handleEstablishedConnection: " + _e.getMessage() );
            }
            finally {
            }
        }

        private HttpURLConnection connect( String _urlStr ) throws Exception {
            URL url = null;
            HttpURLConnection conn = null;
            try {
                try {
                    url = new URL( _urlStr );
                }
                catch( MalformedURLException e ) {
                    throw new IllegalArgumentException( "Invalid url: " + _urlStr );
                }
                conn = preConnect( url );
                doConnect( conn );
                conn = postConnect( conn );
            }
            catch( Exception _e ) {
                throw _e;
            }
            finally {
                url = null;
            }
            return conn;
        }

        private HttpURLConnection preConnect( URL url ) throws Exception {
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches( false );
            // http://www.aswinanand.com/2009/01/httpurlconnectionsetfollowredirects-bug/comment-page-1/#comment-13330
            // see the url to learn more about the problem of redirect
            conn.setInstanceFollowRedirects( false );
            conn.setDoOutput( true );// allows body
            mBody = getBody();
            if( hasBody() ) {
                conn.setFixedLengthStreamingMode( mBody.length );
            }
            conn.setRequestMethod( "POST" );
            conn.setRequestProperty( "Connection", "Keep-Alive" );
            conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded;charset=UTF-8" );
            conn.setRequestProperty( "User-Agent", USER_AGENT );
            conn.setRequestProperty( "Accept-Encoding", "gzip" );// force server to send in Content-Encoding: gzip .
            String cookies = onCookie();
            if( !TextUtils.isEmpty( cookies ) ) {
                conn.setRequestProperty( "Cookie", onCookie() );
                cookies = null;
            }
            conn.setConnectTimeout( onSetConnectTimeout() );
            return conn;
        }

        /*
         * Convenient function to check the exiting of body.
         */
        private boolean hasBody() {
            return mBody != null && mBody.length > 0;
        }

        private void doConnect( HttpURLConnection conn ) throws Exception {
            OutputStream out; // the outgoing stream
            out = conn.getOutputStream();
            if( hasBody() ) {
                out.write( mBody );
            }
            out.close();
            out = null;
        }

        private HttpURLConnection postConnect( HttpURLConnection conn ) throws Exception {
            int status = conn.getResponseCode();
            if( status != HttpURLConnection.HTTP_OK ) {
                throw new ConnectorPostConnectException( status );
            } else {
                CookieManager.getInstance().put( conn.getURL().toURI(), conn.getHeaderFields() );
                return conn;
            }
        }

        private byte[] getBody() {
            byte[] body = null;
            String bodyString = onSetBody();
            if( !TextUtils.isEmpty( bodyString ) ) {
                body = bodyString.getBytes();
            }
            return body;
        }

        // ------------------------------------------------
        // Overrides methods here
        // ------------------------------------------------

        protected int onSetConnectTimeout() {
            return DEFAULT_CNN_TIME_OUT;
        }

        protected String onCookie() {
            return null;
        }

        protected String onSetBody() {
            return null;
        }

        protected void onConnectorConnectTimout() {
            Log.e( TAG, "Handling connector timeout gracefully." );
        }

        protected void onConnectorError( int _status ) {
            Log.e( TAG, "Handling connector error(responsed) gracefully: " + _status );
        }

        protected void onConnectorInvalidConnect( Exception _e ) {
            Log.e( TAG, "Handling connector invalid connect(crash) gracefully: " + _e.toString() );
        }

        /*
         * Read data here. The function runs in thread. To hook on UI thread use onConnectorFinished()
         */
        protected void onConnectorInputStream( InputStream _in ) {
        }

        /*
         * Last handler for a success connection
         */
        protected void onConnectorFinished() {
        }
    }

    public static String streamToString( InputStream _in ) {
        BufferedReader reader = null;
        String line = null;
        StringBuilder content = null;
        try {
            reader = new BufferedReader( new InputStreamReader( _in ), STREAM_CACHE );
            content = new StringBuilder();
            while( (line = reader.readLine()) != null ) {
                content.append( line );
            }
        }
        catch( Exception _e ) {
            Log.e( TAG, "Paser inputstream to string error: " + _e.toString() );
        }
        finally
        {
            try {
                _in.close();
            }
            catch( IOException _e ) {
                _in = null;
            }
            if( reader != null )
            {
                try {
                    reader.close();
                }
                catch( IOException _e ) {
                    reader = null;
                }
            }
            line = null;
        }
        if( content != null )
            return content.toString();
        return null;
    }
}