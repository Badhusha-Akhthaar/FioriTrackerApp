package com.novigo.fiori.trackerapp.logon;

import android.content.Context;

import com.sap.cloud.mobile.foundation.authentication.OAuth2Configuration;

/**
 * This class provides the OAuth configuration object for the application.
 *
 */
public class SAPOAuthConfigProvider {

    private final static String OAUTH_REDIRECT_URL = "https://oauthasservices-s0021300906trial.hanatrial.ondemand.com";
    private final static String OAUTH_CLIENT_ID = "c03f63c9-a551-4ac0-afcc-c6d5820ba56d";
    private final static String AUTH_END_POINT = "https://oauthasservices-s0021300906trial.hanatrial.ondemand.com/oauth2/api/v1/authorize";
    private final static String TOKEN_END_POINT = "https://oauthasservices-s0021300906trial.hanatrial.ondemand.com/oauth2/api/v1/token";

    public static OAuth2Configuration getOAuthConfiguration(Context context) {

        OAuth2Configuration oAuth2Configuration = new OAuth2Configuration.Builder(context)
                .clientId(OAUTH_CLIENT_ID)
                .responseType("code")
                .authUrl(AUTH_END_POINT)
                .tokenUrl(TOKEN_END_POINT)
                .redirectUrl(OAUTH_REDIRECT_URL)
                .build();

        return oAuth2Configuration;
    }
}
