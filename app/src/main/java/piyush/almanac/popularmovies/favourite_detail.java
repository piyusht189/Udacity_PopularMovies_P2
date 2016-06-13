package piyush.almanac.popularmovies;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class favourite_detail extends Fragment {

    String Title,Poster,BackDrop,User_Rating,Release_Date,Overview;
    TextView favTitle,favUser_Rating,favRelease_Date,favOverview;
    ImageView favPoster,favBackDrop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_favourite_detail, container, false);

        favTitle = (TextView)view.findViewById(R.id.favTitle);
        favPoster = (ImageView) view.findViewById(R.id.favPosterPic);
        favBackDrop = (ImageView) view.findViewById(R.id.favBackDrop);
        favUser_Rating= (TextView)view.findViewById(R.id.favUserRating);
        favRelease_Date = (TextView)view.findViewById(R.id.favReleaseDate);
        favOverview = (TextView)view.findViewById(R.id.favOverView);


        Bundle bundle = getArguments();
        if(bundle != null){
            String movieID = bundle.getString("movieID");
            setfavMovieDetails(movieID);
        }
        return view;
    }

    public void setfavMovieDetails(String movieID)
    {

        Cursor cursor = getActivity().getContentResolver().query(MovieDBContentProvider.CONTENT_URI, null, null, null, null);

        if (!cursor.moveToFirst()) {
            Toast.makeText(getActivity(), " no record found yet!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            do
            {
                int mid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBParameters.MOVIE_ENTRY.COLUMN_MOVIE_ID)));
                if(mid==Integer.parseInt(movieID))
                {
                    Title = cursor.getString(cursor.getColumnIndex(DBParameters.MOVIE_ENTRY.COLUMN_TITLE));
                    Poster = cursor.getString(cursor.getColumnIndex(DBParameters.MOVIE_ENTRY.COLUMN_POSTER));
                    BackDrop = cursor.getString(cursor.getColumnIndex(DBParameters.MOVIE_ENTRY.COLUMN_BACKDROP));
                    User_Rating = cursor.getString(cursor.getColumnIndex(DBParameters.MOVIE_ENTRY.COLUMN_USER_RATING));
                    Release_Date = cursor.getString(cursor.getColumnIndex(DBParameters.MOVIE_ENTRY.COLUMN_RELEASE_DATE));
                    Overview = cursor.getString(cursor.getColumnIndex(DBParameters.MOVIE_ENTRY.COLUMN_OVERVIEW));

                    favTitle.setText(Title);
                    favRelease_Date.setText(Release_Date);
                    favUser_Rating.setText(User_Rating);
                    favOverview.setText(Overview);
                    Picasso.with(getActivity()).load(Poster).into(favPoster);
                    Picasso.with(getActivity()).load(BackDrop).into(favBackDrop);

                    break;
                }
            }while (cursor.moveToNext());
        }
    }
}