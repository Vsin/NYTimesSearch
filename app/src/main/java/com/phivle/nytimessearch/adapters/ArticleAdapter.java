package com.phivle.nytimessearch.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.phivle.nytimessearch.R;
import com.phivle.nytimessearch.activities.ArticleActivity;
import com.phivle.nytimessearch.models.Article;

import org.parceler.Parcels;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    final private List<Article> mArticles;
    final private Context mContext;

    public ArticleAdapter(Context context, List<Article> articles) {
        mContext = context;
        mArticles = articles;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View articleView = inflater.inflate(R.layout.item_article_result, parent, false);

        return new ViewHolder(articleView);
    }

    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder viewHolder, int position) {
        Article article = mArticles.get(position);
        String articleThumbnail = article.getThumbnail();
        String articleTitle = article.getHeadline();

        TextView tvTitle = viewHolder.tvTitle;
        tvTitle.setText(articleTitle);

        Glide.with(getContext()).load(articleThumbnail).placeholder(R.drawable.ic_broken_image_black_24dp).into(viewHolder.ivThumbnail);

    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.clear(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView ivThumbnail;
        final TextView tvTitle;

        ViewHolder(View itemView) {
            super(itemView);

            ivThumbnail = itemView.findViewById(R.id.ivImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent viewArticleIntent = new Intent(mContext, ArticleActivity.class);
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Article article = mArticles.get(position);
                viewArticleIntent.putExtra("article", Parcels.wrap(article));

                getContext().startActivity(viewArticleIntent);
            }
        }

    }

}
