package com.uka.gettit;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
public class AddNewPost extends AppCompatActivity {

    EditText mEdittextTitle;
    EditText mEdittextContent;
    EditText mEditTextTag;

    private static String TAG = ">>>>>>>>>>";

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_post);

        SharedPreferences sharedPreferences = getSharedPreferences("com.uka.gettit.prefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "test");
        String password = sharedPreferences.getString("password", "123");

        user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole("USER");

        mEdittextTitle = findViewById(R.id.mEdittextTitle);
        mEdittextContent = findViewById(R.id.mEdittextContent);
        mEditTextTag = findViewById(R.id.mEdittextTag);
    }

    private void addNewPost() {
        if (mEdittextTitle.getText().toString().length() > 0
                && mEdittextContent.getText().length() > 0
                && mEditTextTag.getText().length() > 0) {
            String title = mEdittextTitle.getText().toString();
            String content = mEdittextContent.getText().toString();
            String tag = mEditTextTag.getText().toString();

            title = title.trim();
            content = content.trim();
            tag = tag.trim();

            Post p = new Post(title, content, tag, user.getUsername());

            new PostCommentTask().execute(p);
        } else {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_LONG).show();
        }
    }

    private class PostCommentTask extends AsyncTask<Post, Void, Message> {

        @Override
        protected Message doInBackground(Post... args) {

            Post p = args[0];

            final String url = getString(R.string.base_uri) + "/addpost";
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
                ResponseEntity<Message> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(p, requestHeaders), Message.class);
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

            showMessageResult(message);
        }
    }

    private void showMessageResult(Message message) {
        if (message != null) {
            Toast.makeText(this, message.getSubject(), Toast.LENGTH_LONG).show();

            if (message.getSubject().equals("Success")) {
                finish();
            }
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post_comment, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_comment) {
            addNewPost();
        }

        return super.onOptionsItemSelected(item);
    }
}
