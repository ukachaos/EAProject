package com.uka.gettit;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class DetailedViewActivity extends AppCompatActivity {

    static String TAG = ">>>>>>>>";

    boolean showMenu = false;

    private int post_id = 0;

    TextView mTextTitle;
    TextView mTextContent;
    TextView mTextVote;
    Button mButtonUpvote;
    Button mButtonDownvote;

    EditText mEditTextAddComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);

        post_id = getIntent().getExtras().getInt("postid", 0);

        mTextTitle = findViewById(R.id.mTextTitle);
        mTextContent = findViewById(R.id.mTextContent);
        mTextVote = findViewById(R.id.mTextVote);

        mButtonUpvote = findViewById(R.id.mButtonUp);
        mButtonUpvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UpdateUpvoteTask().execute(post_id);
            }
        });

        mButtonDownvote = findViewById(R.id.mButtonDown);
        mButtonDownvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO downvote
            }
        });

        mEditTextAddComment = findViewById(R.id.mEditTextAddComment);
        mEditTextAddComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    showMenu = true;
                    invalidateOptionsMenu();
                }
            }
        });

        new GetPostTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (showMenu)
            getMenuInflater().inflate(R.menu.menu_post_comment, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_comment) {
           addComment();
        }

        return super.onOptionsItemSelected(item);
    }

    private void addComment(){

        mEditTextAddComment.setText("");
        mEditTextAddComment.clearFocus();

        showMenu = false;
        invalidateOptionsMenu();

        hideKeyboardFrom(this, mEditTextAddComment);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private class PostCommentTask extends AsyncTask<Void, Void, Message>{

        Comment comment;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            comment = new Comment();
            comment.setComment(mEditTextAddComment.getText().toString());
            comment.setAuthor("'");
            comment.setId(111);
        }

        @Override
        protected Message doInBackground(Void... voids) {
            final String url = getString(R.string.base_uri) + "/getpost/" + post_id;
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
                ResponseEntity<Message> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(comment, requestHeaders), Message.class);
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
        }
    }

    private class GetPostTask extends AsyncTask<Void, Void, Post> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Post doInBackground(Void... voids) {
            final String url = getString(R.string.base_uri) + "/getpost/" + post_id;
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
        protected void onPostExecute(Post post) {
            super.onPostExecute(post);
            updateViews(post);
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
            HttpAuthentication authHeader = new HttpBasicAuthentication("roy", "spring");
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

    private void showMessageResult(Message message) {
        Toast.makeText(this, message.getSubject(), Toast.LENGTH_LONG).show();
    }

    public void updateViews(Post post) {
        if (post != null) {
            mTextTitle.setText(post.getTitle());
            mTextContent.setText(post.getContent());

            mTextVote.setText(post.getUpvote() + "/" + post.getDownvote());
        } else {
            Toast.makeText(getApplicationContext(), "Error while getting data.", Toast.LENGTH_LONG).show();
        }
    }
}
