package com.uka.gettit;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static String TAG = ">>>>>>>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        new FetchSecuredResourceTask().execute();

        new GetPostTask1().execute();
    }

    // ***************************************
    // Private classes
    // ***************************************
    private class FetchSecuredResourceTask extends AsyncTask<Void, Void, Message> {

        private String username;

        private String password;

        @Override
        protected void onPreExecute() {

            // build the message object
            this.username = "roy";

            this.password = "spring";
        }

        @Override
        protected Message doInBackground(Void... params) {
            final String url = getString(R.string.base_uri) + "/getmessage";

            // Populate the HTTP Basic Authentitcation header with the username and password
            HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

            try {
                // Make the network request
                Log.d(TAG, url);
                ResponseEntity<Message> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(requestHeaders), Message.class);
                return response.getBody();
            } catch (HttpClientErrorException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new Message(0, e.getStatusText(), e.getLocalizedMessage());
            } catch (ResourceAccessException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new Message(0, e.getClass().getSimpleName(), e.getLocalizedMessage());
            }
        }

        @Override
        protected void onPostExecute(Message result) {
            Log.e(TAG, result.toString());
        }

    }

    private class GetPostTask extends AsyncTask<Void, Void, Post> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Post doInBackground(Void... voids) {
            final String url = getString(R.string.base_uri) + "/getpost";
            // Populate the HTTP Basic Authentitcation header with the username and password
            HttpAuthentication authHeader = new HttpBasicAuthentication("roy", "spring");
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

            try {
                // Make the network request
                Log.d(TAG, url);
                ResponseEntity<Post> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(requestHeaders), Post.class);
                Log.e(TAG, response.getBody().getTitle() + " - " + response.getBody().getContent());
            } catch (HttpClientErrorException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            } catch (ResourceAccessException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Post post) {
            super.onPostExecute(post);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    private class GetPostTask1 extends AsyncTask<Void, Void, Post> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Post doInBackground(Void... voids) {
            final String url = getString(R.string.base_uri) + "/getposts";
            // Populate the HTTP Basic Authentitcation header with the username and password
            HttpAuthentication authHeader = new HttpBasicAuthentication("roy", "spring");
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

            try {
                // Make the network request
                Log.d(TAG, url);
                ResponseEntity<Post[]> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(requestHeaders), Post[].class);
                Post[] p = response.getBody();
                Log.e(TAG, "Length" + p.length);
                for(Post o : p){
                    Log.e(TAG, o.getTitle() + " - " + o.getContent());
                }
//                Log.e(TAG, response.getBody().getTitle() + " - " + response.getBody().getContent());
            } catch (HttpClientErrorException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            } catch (ResourceAccessException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Post post) {
            super.onPostExecute(post);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}