package piyush.almanac.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Super-Nova on 29-04-2016.
 */
public class imageadapter extends ArrayAdapter<String> {

    private final Activity activity;
    private final String[] MoviePoster;

    imageadapter(Activity activity, String[] MoviePoster) {
        super(activity,R.layout.movie_single_poster,MoviePoster);
        this.activity = activity;
        this.MoviePoster=MoviePoster;


    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();

        ViewHodler holder;

        if(convertView == null) {
            View rowView = inflater.inflate(R.layout.movie_single_poster, parent, true);
            holder = new ViewHodler();

            holder.someImageView = (ImageView) rowView.findViewById(R.id.singlePoster);
            rowView.setTag(holder);
        }
        else {
            holder = (ViewHodler) convertView.getTag();
        }

        if (MoviePoster[position] != null)
            Picasso.with(activity)
                    .load(MoviePoster[position])
                    .into(holder.someImageView);

        return convertView;
    }

    class ViewHodler {
        ImageView someImageView;
    }



    }



