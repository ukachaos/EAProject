package com.uka.gettit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class PostsListAdapter extends BaseAdapter {

    Context context;

    LayoutInflater inflater;

    List<Post> posts;

    public PostsListAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int i) {
        return posts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null) {
            Post p = posts.get(i);

            View mView = inflater.inflate(R.layout.ticket, null);
            mView.setTag(p.getId());
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailedViewActivity.class);
                    intent.putExtra("postid", (int)view.getTag());
                    context.startActivity(intent);
                }
            });

            TextView title = mView.findViewById(R.id.mTextTitle);

            final TextView content = mView.findViewById(R.id.mTextContent);

            title.setText(p.getTitle());
            content.setText(p.getContent());

            TextView mTextVote = mView.findViewById(R.id.mTextVote);
            mTextVote.setText(p.getUpvote() + "/" + p.getDownvote());

            Button mButtonUpvote = mView.findViewById(R.id.mButtonUp);
            mButtonUpvote.setTag(p.getId());
            mButtonUpvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity)context).executeUpvoteTaskt((int)view.getTag());
                }
            });

            Button mButtonDownvote = mView.findViewById(R.id.mButtonDown);
            mButtonDownvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            view = mView;
        }

        return view;
    }
}
