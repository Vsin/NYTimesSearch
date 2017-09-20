package com.phivle.nytimessearch.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.phivle.nytimessearch.R;
import com.phivle.nytimessearch.activities.ArticleActivity;
import com.phivle.nytimessearch.models.Article;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Vsin on 9/17/17.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private List<Article> mArticles;
    private Context mContext;

    private Context getContext() {
        return mContext;
    }


    public ArticleAdapter(Context context, List<Article> articles) {
        mContext = context;
        mArticles = articles;
    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View articleView = inflater.inflate(R.layout.item_article_result, parent, false);

        return new ViewHolder(articleView);
    }

    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder holder, int position) {
        Article article = mArticles.get(position);
        String articleThumbnail = article.getThumbnail();
        String articleTitle = article.getHeadline();

        TextView tvTitle = holder.tvTitle;
        tvTitle.setText(articleTitle);

        ImageView thumbnail = holder.thumbnail;

        thumbnail.setImageResource(0);

        if (!articleThumbnail.equals("")) {
            Picasso.with(mContext).load(articleThumbnail).into(thumbnail);
        }

    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView thumbnail;
        TextView tvTitle;
        Context context;

        ViewHolder(View itemView) {
            super(itemView);

            thumbnail = (ImageView) itemView.findViewById(R.id.ivImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            context = mContext;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent viewArticleIntent = new Intent(context, ArticleActivity.class);
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Article article = mArticles.get(position);
                viewArticleIntent.putExtra("article", article);

                context.startActivity(viewArticleIntent);
            }
        }
    }
}
