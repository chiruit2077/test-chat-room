package com.example.testchatroom;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;

/**
 * The compatibility class CookieManager that support < Level 9.
 * 
 * @author Chris.Z <hasszhao@gmail.com>
 */
public class CookieManager extends CookieHandler {
    private static final String       SET_COOKIE             = "Set-Cookie";
    private static final char         NAME_VALUE_SEPARATOR   = '=';
    private static final String       COOKIE_VALUE_DELIMITER = ";";
    private static final char         DOT                    = '.';
    private static CookieManager      sInstance;
    private URI                       mURI;
    private Map<String, List<String>> mHeaderFields;
    private Map<String, List<String>> mCookieFields;
    private CookieStore               mCookieStore           = new BasicCookieStore();

    public static CookieManager getInstance() {
        if( sInstance == null ) {
            synchronized( CookieManager.class ) {
                sInstance = new CookieManager();
            }
        }
        return sInstance;
    }

    private CookieManager() {
    }

    @Override
    public Map<String, List<String>> get( URI _arg0, Map<String, List<String>> _arg1 ) throws IOException {
        put( _arg0, _arg1 );
        return mCookieFields;
    }

    @Override
    public void put( URI _arg0, Map<String, List<String>> _arg1 ) throws IOException {
        mURI = _arg0;
        mHeaderFields = _arg1;
        readCookies();
    }

    private String getDomain( String host ) {
        if( host.indexOf( DOT ) != host.lastIndexOf( DOT ) ) {
            return host.substring( host.indexOf( DOT ) + 1 );
        }
        else {
            return host;
        }
    }

    private void readCookies() throws IOException {
        String domain = getDomain( mURI.getHost() );
        Set<String> keys = mHeaderFields.keySet();
        for( String key : keys ) {
            if( key != null && key.equalsIgnoreCase( SET_COOKIE ) ) {
                mCookieFields = new HashMap<String, List<String>>();
                List<String> list = mHeaderFields.get( key );
                mCookieFields.put( key, list );
                for( String str : list ) {
                    StringTokenizer st = new StringTokenizer( str, COOKIE_VALUE_DELIMITER );
                    if( st.hasMoreTokens() ) {
                        String token = st.nextToken();
                        String name = token.substring( 0, token.indexOf( NAME_VALUE_SEPARATOR ) );
                        String value = token.substring( token.indexOf( NAME_VALUE_SEPARATOR ) + 1, token.length() );
                        org.apache.http.impl.cookie.BasicClientCookie cookie = new org.apache.http.impl.cookie.BasicClientCookie( name, value );
                        cookie.setDomain( domain );
                        mCookieStore.addCookie( cookie );
                    }
                    while( st.hasMoreTokens() ) {
                        String token = st.nextToken();
                        String name = token.substring( 0, token.indexOf( NAME_VALUE_SEPARATOR ) ).toLowerCase();
                        String value = token.substring( token.indexOf( NAME_VALUE_SEPARATOR ) + 1, token.length() );
                        org.apache.http.impl.cookie.BasicClientCookie cookie = new org.apache.http.impl.cookie.BasicClientCookie( name, value );
                        cookie.setDomain( domain );
                        mCookieStore.addCookie( cookie );
                    }
                }
            }
        }
    }

    public CookieStore getCookieStore() {
        return mCookieStore;
    }
}
