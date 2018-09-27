package com.uka.gettit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
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

import java.util.Arrays;
import java.util.Collections;

/**
 * @author Uka
 */
public class MainActivity extends AppCompatActivity {

    static String TAG = ">>>>>>>";

    ListView mListPosts;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("com.uka.gettit.prefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "test");
        String password = sharedPreferences.getString("password", "123");

        Log.e(TAG, "----->" + username + " - " + password);

        user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole("ROLE_USER");

        mListPosts = findViewById(R.id.mListPosts);
    }

    public void executeUpvoteTask(int id) {
        new UpdateUpvoteTask().execute(id);
    }

    public void executeDownvoteTask(int id) {
        new UpdateDownvoteTask().execute(id);
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
            Log.e(TAG, result.toString());
        }

    }

    private class GetPostTask extends AsyncTask<Void, Void, Post[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Post[] doInBackground(Void... voids) {
            final String url = getString(R.string.base_uri) + "/getposts";
            // Populate the HTTP Basic Authentitcation header with the username and password
            HttpAuthentication authHeader = new HttpBasicAuthentication(user.getUsername(), user.getPassword());
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

            try {
                // Make the network request
                Log.d(TAG, url);
                ResponseEntity<Post[]> response = restTemplate.exchange(url,
                        HttpMethod.GET, new HttpEntity<Object>(requestHeaders), Post[].class);
                Post[] p = response.getBody();
                Log.e(TAG, "Length" + p.length);
                for (Post o : p) {
                    Log.e(TAG, o.getTitle() + " - " + o.getContent() + " -> " + o.getComments().size());
                }

                return p;
            } catch (HttpClientErrorException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return null;
            } catch (ResourceAccessException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Post[] post) {
            super.onPostExecute(post);
            updateListView(post);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


    private class UpdateUpvoteTask extends AsyncTask<Integer, Void, Message> {

        @Override
        protected Message doInBackground(Integer... integers) {

            final String url = getString(R.string.base_uri) + "/upvote/" + integers[0];
            // Populate the HTTP Basic Authentitcation header with the username and password
            HttpAuthentication authHeader = new HttpBasicAuthentication(user.getUsername(), user.getPassword());
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

            try {
                ResponseEntity<Message> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(requestHeaders), Message.class);

                return response.getBody();

            } catch (HttpClientErrorException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            } catch (ResourceAccessException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Message message) {
            super.onPostExecute(message);
            showMessageResult(message);
        }
    }

    private class UpdateDownvoteTask extends AsyncTask<Integer, Void, Message> {

        @Override
        protected Message doInBackground(Integer... integers) {

            final String url = getString(R.string.base_uri) + "/downvote/" + integers[0];
            // Populate the HTTP Basic Authentitcation header with the username and password
            HttpAuthentication authHeader = new HttpBasicAuthentication(user.getUsername(), user.getPassword());
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

            try {
                ResponseEntity<Message> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(requestHeaders), Message.class);

                return response.getBody();

            } catch (HttpClientErrorException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return null;
            } catch (ResourceAccessException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Message message) {
            super.onPostExecute(message);
            if (message != null)
                showMessageResult(message);
        }
    }

    private void showMessageResult(Message message) {
        if (message != null) {
            Toast.makeText(this, message.getSubject(), Toast.LENGTH_LONG).show();

            if (message.getSubject().equals("Success")) {
                new GetPostTask().execute();
            }
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    private void updateListView(Post[] posts) {
        if (posts != null) {
            PostsListAdapter adapter = new PostsListAdapter(this, Arrays.asList(posts));
            mListPosts.setAdapter(adapter);
        } else
            Toast.makeText(getApplicationContext(), "Error while getting data!", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_new) {
            Intent intent = new Intent(this, AddNewPost.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new GetPostTask().execute();
    }
}