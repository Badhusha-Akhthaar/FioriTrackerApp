package com.novigo.fiori.trackerapp;

import android.os.AsyncTask;

import com.github.sendgrid.SendGrid;

import java.util.Hashtable;

public class SendGridAsyncTask extends AsyncTask<Hashtable<String,String>,Void,String> {
    @Override
    protected String doInBackground(Hashtable<String, String>... hashtables) {
        Hashtable<String,String> hashtable = hashtables[0];
        SendGridCredentials sendGridCredentials = new SendGridCredentials();
        SendGrid sendGrid  = new SendGrid(sendGridCredentials.getUsername(),sendGridCredentials.getPassword());
        sendGrid.addTo(hashtable.get("to"));
        sendGrid.setFrom(hashtable.get("from"));
        sendGrid.setSubject(hashtable.get("subject"));
        sendGrid.setText(hashtable.get("text"));
        String response = sendGrid.send();
        return response;
    }
}
