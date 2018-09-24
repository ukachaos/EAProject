package com.uka.gettit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CommentAdapter extends BaseAdapter {

    List<Comment> comments;

    Context context;

    LayoutInflater inflater;

    public CommentAdapter(Context context, List<Comment> comments){
        this.context = context;
        this.comments = comments;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int i) {
        return comments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null){

            Comment c = comments.get(i);

            View mView = inflater.inflate(R.layout.ticket_comment, null);

            TextView mTextComment = mView.findViewById(R.id.mTextComment);
            mTextComment.setText(c.getComment());

            TextView mTextAuthor = mView.findViewById(R.id.mTextAuthor);
            mTextAuthor.setText(c.getAuthor());

            view = mView;
        }

        return view;
    }
}
