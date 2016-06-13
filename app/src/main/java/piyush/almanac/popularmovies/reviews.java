package piyush.almanac.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Super-Nova on 09-06-2016.
 */
public class reviews  extends ArrayAdapter<String> {

    Activity context;
    TextView ReviewText,ReviewByText;
    String[] Review,ReviewBy;

    public reviews(Activity context,String[] Review,String[] ReviewBy) {
        super(context,R.layout.reviews,ReviewBy);
        this.context=context;
        this.Review=Review;
        this.ReviewBy=ReviewBy;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView= inflater.inflate(R.layout.reviews, null, true);

        ReviewByText = (TextView)rowView.findViewById(R.id.ReviewBy);
        ReviewText=(TextView)rowView.findViewById(R.id.Review);

        ReviewByText.setText(ReviewBy[position]);
        ReviewText.setText(Review[position]);

        return rowView;
    }

}
