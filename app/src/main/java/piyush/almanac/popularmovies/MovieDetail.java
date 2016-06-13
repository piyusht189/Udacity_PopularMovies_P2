package piyush.almanac.popularmovies;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetail extends Fragment{
    int pos;
    String url,first="http://api.themoviedb.org/3/movie/",end="?api_key=a9921873d439557b59941c8130548062",full,full2,youtubelink;
    RequestQueue requestQueue,requestQueue2,requestQueue3,requestQueue5;
    String fullyoutubelink,full3;
    String start="https://www.youtube.com/watch?v=";
    String key,posterpath,BackdropPath;
    Boolean mFavorited;
    String[] ReviewBy, Review;
    TextView title,ratings,votes,description,reldate;
    ImageView poster,backdrop;
boolean result;
    ImageButton trailerbtn;
    Toolbar toolbar;
    FloatingActionButton fab;
    private ListView listViewReview;
    int id2,id,movieID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_movie_detail, container, false);
        listViewReview=(ListView) view.findViewById(R.id.reviewlistview);
        requestQueue2 = Volley.newRequestQueue(getActivity());
        requestQueue3 = Volley.newRequestQueue(getActivity());
        requestQueue5 = Volley.newRequestQueue(getActivity());
        Bundle extra =getArguments();
        if(extra!=null){
            pos=Integer.parseInt(extra.getString("pos"));
            url=extra.getString("url");
            movieID=Integer.parseInt(extra.getString("movieID").toString());
            full=first+url+end;
        }

        requestQueue = Volley.newRequestQueue(getActivity());
        // Toast.makeText(MovieDetail.this,pos,Toast.LENGTH_SHORT).show();
        title = (TextView) view.findViewById(R.id.titleofmovie);
        trailerbtn=(ImageButton) view.findViewById(R.id.trailerplay);
        ratings = (TextView) view.findViewById(R.id.ratings);
        votes = (TextView) view.findViewById(R.id.totalvote);
        description = (TextView) view.findViewById(R.id.desc);
        reldate = (TextView) view.findViewById(R.id.releasedate);
        poster=(ImageView) view.findViewById(R.id.poster);
        backdrop = (ImageView) view.findViewById(R.id.backdrop);


        fab=(FloatingActionButton) view.findViewById(R.id.fab);




return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, full,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Hello", "Hi");
                        try {
                            JSONArray results = response.getJSONArray("results");

                            JSONObject poster_image = results.getJSONObject(pos);

                            description.setText(poster_image.getString("overview"));
                            reldate.setText(poster_image.getString("release_date"));
                            title.setText(poster_image.getString("original_title"));
                            id2=Integer.parseInt(poster_image.getString("id"));
                            id=id2;
                            votes.setText(poster_image.getString("vote_count"));
                            ratings.setText(poster_image.getString("vote_average"));
                            posterpath="http://image.tmdb.org/t/p/w185"+poster_image.getString("poster_path");
                            BackdropPath="http://image.tmdb.org/t/p/w500"+poster_image.getString("backdrop_path");
                            Picasso.with(getActivity()).load(posterpath).into(poster);
                            Picasso.with(getActivity()).load(BackdropPath).into(backdrop);
                            full2=String.valueOf(id);
                            reviews(full2);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
        trailerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trailer(full2);

            }
        });

        fab=(FloatingActionButton)view.findViewById(R.id.fab);
        mFavorited=isFavourite();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setImageResource(toggleFavoriteButton());
            }
        });
        if(mFavorited){
            fab.setImageResource(R.drawable.ic_favorite_selected);
        }
        else{
            fab.setImageResource(R.drawable.ic_favorite_not_selected);
        }

    }

    public int toggleFavoriteButton()
    {
        mFavorited = !mFavorited;
        if(!mFavorited){
            deletefavorite();
            Toast.makeText(getActivity(),"Movie Unfavorited",Toast.LENGTH_SHORT).show();
            return R.drawable.ic_favorite_not_selected;
        }
        else{
            savefavorite();
            Toast.makeText(getActivity(),"Movie Favorited",Toast.LENGTH_SHORT).show();
            return  R.drawable.ic_favorite_selected;
        }
    }

    public Uri savefavorite()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBParameters.MOVIE_ENTRY.COLUMN_MOVIE_ID,id);
        contentValues.put(DBParameters.MOVIE_ENTRY.COLUMN_POSTER,posterpath);
        contentValues.put(DBParameters.MOVIE_ENTRY.COLUMN_BACKDROP,BackdropPath);
        contentValues.put(DBParameters.MOVIE_ENTRY.COLUMN_TITLE,title.getText().toString());
        contentValues.put(DBParameters.MOVIE_ENTRY.COLUMN_OVERVIEW,description.getText().toString());
        contentValues.put(DBParameters.MOVIE_ENTRY.COLUMN_RELEASE_DATE,reldate.getText().toString());
        contentValues.put(DBParameters.MOVIE_ENTRY.COLUMN_USER_RATING,ratings.getText().toString());
        return getActivity().getContentResolver().insert(MovieDBContentProvider.CONTENT_URI,contentValues);
    }

    public int deletefavorite()
    {
        ContentResolver contentResolver = getActivity().getContentResolver();
        String selection = DBParameters.MOVIE_ENTRY.COLUMN_MOVIE_ID + " = \"" + String.valueOf(id) + "\"";
        return contentResolver.delete(MovieDBContentProvider.CONTENT_URI,selection,null);
    }


    public boolean isFavourite()
    {

        int flag=0;
        Cursor cursor = getActivity().getContentResolver().query(MovieDBContentProvider.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext())
        {
            int mid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBParameters.MOVIE_ENTRY.COLUMN_MOVIE_ID)));
            if(mid==movieID)
            {

                flag=1;
                break;
            }
        }
        if(flag==1)
        {
             return true;
        }
        else
        {
            return false;
        }

    }



    void trailer(String iid){
        full2=first+iid+"/videos"+end;
        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET, full2,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Hello", "Hi");
                        try {
                            JSONArray results = response.getJSONArray("results");

                            JSONObject poster_image = results.getJSONObject(0);
                            key=poster_image.getString("key");

                            fullyoutubelink=start+key;
                            Log.d("link",fullyoutubelink);
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fullyoutubelink));
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("Hell","Error");
            }
        });

        requestQueue2.add(jsonObj);

    }


    void reviews(String iid){
        full3=first+iid+"/reviews"+end;
        JsonObjectRequest jsonObje = new JsonObjectRequest(Request.Method.GET, full3,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Hello", "Hi");
                        try {
                            JSONArray results = response.getJSONArray("results");
                            if(results.length()<1)
                            {
                                Review = new String[1];
                                ReviewBy = new String[1];
                                ReviewBy[0] = "No reviews Present.";
                                Review[0] = "";
                            }
                            else
                            {
                                ReviewBy = new String[results.length()];
                                Review = new String[results.length()];
                                for(int i=0;i<results.length();i++)
                                {
                                    JSONObject reviewsingledetail = results.getJSONObject(i);
                                    ReviewBy[i] = "Reviewed By: "+reviewsingledetail.getString("author");
                                    Review[i] = reviewsingledetail.getString("content");
                                }
                            }
                            reviews adapter = new reviews(getActivity(), Review, ReviewBy);
                            listViewReview.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("Hell","Error");
            }
        });

        requestQueue3.add(jsonObje);

    }
    }



