package com.uka.gettit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * @author Uka
 */
public class LoginActivity extends AppCompatActivity {

    static String TAG = ">>>>>>>";

    EditText mEdittextUsername;
    EditText mEdittextPassword;

    Button mButtonLogin;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPreferences = getSharedPreferences("com.uka.gettit.prefs", MODE_PRIVATE);
        if (sharedPreferences.contains("username")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            finish();
        }

        mEdittextUsername = findViewById(R.id.mEdittextUsername);

        mEdittextPassword = findViewById(R.id.mEdittextPassword);

        mButtonLogin = findViewById(R.id.mButtonLogin);
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        if (mEdittextUsername.getText().length() > 0
                && mEdittextPassword.getText().length() > 0) {
            user = new User();
            user.setUsername(mEdittextUsername.getText().toString());
            user.setPassword(mEdittextPassword.getText().toString());
            user.setRole("USER");

            new CreateUserTask().execute(user);

        } else {
            Toast.makeText(this, "Enter username and password", Toast.LENGTH_LONG).show();
        }
    }

    private class CreateUserTask extends AsyncTask<User, Void, Message> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Message doInBackground(User... args) {

            User user = args[0];

            final String url = getString(R.string.base_uri) + "/registration";
            // Populate the HTTP Basic Authentitcation header with the username and password
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

            try {
                // Make the network request
                Log.d(TAG, url);
                ResponseEntity<Message> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(user, requestHeaders), Message.class);
                return response.getBody();

            } catch (HttpClientErrorException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return null;
            } catch (ResourceAccessException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return null;
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Message message) {
            super.onPostExecute(message);

            if (message.getText().equals("userexists")) {
                new FetchSecuredResourceTask().execute();
            } else {
                loginSuccess();
            }
        }
    }

    private class FetchSecuredResourceTask extends AsyncTask<Void, Void, Message> {

        private String username;

        private String password;

        @Override
        protected void onPreExecute() {

            // build the message object
            this.username = user.getUsername();

            this.password = user.getPassword();
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
            } catch (Exception e) {
                e.printStackTrace();
                return new Message(0, e.getClass().getSimpleName(), e.getLocalizedMessage());
            }
        }

        @Override
        protected void onPostExecute(Message result) {
            if (result != null) {
                if (result.getSubject().equals("Congratulations!"))
                    loginSuccess();
                else {
                    Message message = new Message();
                    message.setText("Wrong username/password");
                    message.setSubject("Wrong username/password");

                    showMessageResult(message);
                }
            }
        }

    }

    private void loginSuccess() {
        SharedPreferences sharedPreferences = getSharedPreferences("com.uka.gettit.prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Log.e(TAG, user.getUsername() + " - " + user.getPassword());

        editor.putString("username", user.getUsername());
        editor.putString("password", user.getPassword());
        editor.commit();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    private void showMessageResult(Message message) {
        if (message != null) {
            Toast.makeText(this, message.getText(), Toast.LENGTH_LONG).show();
        }
    }
}
