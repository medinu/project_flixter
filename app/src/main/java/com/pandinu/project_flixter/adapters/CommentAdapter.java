package com.pandinu.project_flixter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pandinu.project_flixter.R;
import com.pandinu.project_flixter.models.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    Context context;
    List<Comment> allComments;

    public CommentAdapter(Context context, List<Comment> comments){
        this.context = context;
        this.allComments = comments;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View commentView = LayoutInflater.from(context).inflate(R.layout.comment_view, parent, false);
        return new ViewHolder(commentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(allComments.get(position).getAuthor(), allComments.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return allComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout mContainer;
        TextView mAuthor;
        TextView mComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mContainer = itemView.findViewById(R.id.rlComment);
            mAuthor = itemView.findViewById(R.id.comment_author_text);
            mComment = itemView.findViewById(R.id.comment_comment);
        }

        public void bind(String author, String comment){
            mAuthor.setText(author);
            mComment.setText(comment);
        }
    }
}
