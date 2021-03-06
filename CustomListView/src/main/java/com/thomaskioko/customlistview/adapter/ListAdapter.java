/*
 * Copyright (c) 2015. Thomas Kioko.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.thomaskioko.customlistview.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.thomaskioko.customlistview.R;
import com.thomaskioko.customlistview.model.Movie;

import java.util.LinkedList;
import java.util.List;

/**
 * This class provides data to the list.
 *
 * @author <a href="kiokotomas@gmail.com">Thomas Kioko</a>
 * @version Version 1.0
 */


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MovieViewHolder> {

    private List<Movie> mMovieList;
    /**
     * This LinkedList is used to map the position of the item and the image
     */
    protected LinkedList<Integer> drawableLinkedList;
    private Context mContext;

    /**
     * Default Constructor
     *
     * @param context      Application context
     * @param followerList Follower List objects
     * @param drawableLinkedList      Drawable item .
     */
    public ListAdapter(Context context, List<Movie> followerList, LinkedList<Integer> drawableLinkedList) {
        this.mContext = context;
        this.mMovieList = followerList;
        this.drawableLinkedList = drawableLinkedList;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {

        Movie movie = mMovieList.get(position);
        final int actionDrawableId = this.drawableLinkedList.get(position);
        holder.title.setText(movie.getTitle());
        holder.rating.setText("Rating: " + String.valueOf(movie.getRating()));
        //Use Glide to load the Image
        Glide.with(mContext).load(movie.getThumbnailUrl()).centerCrop().into(holder.thumbNail);

        // genre
        String genreStr = "";
        for (String str : movie.getGenre()) {
            genreStr += str + ", ";
        }
        genreStr = genreStr.length() > 0 ? genreStr.substring(0,
                genreStr.length() - 2) : genreStr;
        holder.genre.setText(genreStr);
        holder.year.setText(String.valueOf(movie.getYear()));
        /**
         * Set OnClickListener on the Button.
         * We pass in 3 parameters:
         * @param position :Position of the object on the List
         * @param mMovieList Movie Object
         * @param actionDrawableId Drawable ID
         */
        holder.imageViewAddMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMemberClick(position, mMovieList, actionDrawableId);
            }
        });
        //Set the Image Resource
        holder.imageViewAddMovie.setImageResource(actionDrawableId);
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.movie_item_layout, viewGroup, false);

        return new MovieViewHolder(itemView);
    }


    /**
     * This class uses A ViewHolder object stores to each of the component views inside the tag field
     * of the Layout, so you can immediately access them without the need to look them up repeatedly.
     * <p/>
     * {@see <a href="http://developer.android.com/training/improving-layouts/smooth-scrolling.html</a>}
     */
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbNail;
        ImageView imageViewAddMovie;
        TextView title;
        TextView rating;
        TextView genre;
        TextView year;

        public MovieViewHolder(View itemView) {
            super(itemView);

            thumbNail = (ImageView) itemView.findViewById(R.id.thumbnail);
            title = (TextView) itemView.findViewById(R.id.title);
            rating = (TextView) itemView.findViewById(R.id.rating);
            genre = (TextView) itemView.findViewById(R.id.genre);
            year = (TextView) itemView.findViewById(R.id.releaseYear);
            imageViewAddMovie = (ImageView) itemView.findViewById(R.id.btnAddMovie);
        }
    }

    /**
     * This method handles onclick behaviour when a user clicks on the add movie button.
     * We get the Drawable Id on the clicked item and perform the related action. We use an AsyncTask
     * to simulate a long process.
     *
     * Once the AsyncTask is complete {@see onPostExecute}, we remove the drawable/image on the list
     * and add the another image mapping it to the position of the object.
     *
     * Use Case:
     * When a user clicks on add button, make a HTTP request and store the selected movie in the
     * watchlist table then get the response from the server and update the icon based on the response
     * from the server.
     *
     * We Use a Switch case to get the Drawable ID which will determine what action to perform.
     *
     * R.drawable.movie_add_touch: Add the selected movie to the DB
     * R.drawable.movie_added_touch: Remove the Movie from the DB
     * R.drawable.movie_error_touch: An error occurred performing your request. Retry.
     *
     *
     * @param position         clicked object position
     * @param followerList     Object List
     * @param actionDrawableId Drawable Id
     */
    protected void onMemberClick(final int position, final List<Movie> followerList,int actionDrawableId) {
        final Movie follower = followerList.get(position);
        switch (actionDrawableId) {
            case R.drawable.movie_add_touch:

                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        for (int i = 0; i < 1; i++) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Thread.interrupted();
                            }
                        }
                        return "Complete";
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                        if (result.equals("Complete")) {
                            Toast.makeText(mContext, follower.getTitle(), Toast.LENGTH_SHORT).show();
                            drawableLinkedList.remove(position);
                            drawableLinkedList.add(position, R.drawable.movie_added_touch);
                            notifyDataSetChanged();
                        } else {
                            drawableLinkedList.remove(position);
                            drawableLinkedList.add(position, R.drawable.movie_error_touch);
                            Toast.makeText(mContext, mContext.getString(R.string.text_something_went_wrong),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
                break;

            case R.drawable.movie_added_touch:
                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        for (int i = 0; i < 1; i++) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Thread.interrupted();
                            }
                        }
                        return "Complete";
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                        if (result.equals("Complete")) {
                            drawableLinkedList.remove(position);
                            drawableLinkedList.add(position, R.drawable.movie_add_touch);
                            notifyDataSetChanged();
                        } else {
                            drawableLinkedList.remove(position);
                            drawableLinkedList.add(position, R.drawable.movie_error_touch);
                            Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
                break;
            case R.drawable.movie_error_touch:
                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        for (int i = 0; i < 1; i++) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Thread.interrupted();
                            }
                        }
                        return "Complete";
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                        if (result.equals("Complete")) {
                            drawableLinkedList.remove(position);
                            drawableLinkedList.add(position, R.drawable.movie_added_touch);
                            notifyDataSetChanged();
                        } else {
                            drawableLinkedList.remove(position);
                            drawableLinkedList.add(position, R.drawable.movie_error_touch);
                            Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
                break;
        }
    }
}
