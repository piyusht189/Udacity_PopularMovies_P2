package piyush.almanac.popularmovies;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class favourite_poster extends Fragment {

    GridView gridView;

    String movieID[],Poster[],Title[];

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_favourite_poster, container, false);
        setHasOptionsMenu(true);

        gridView=(GridView)view.findViewById(R.id.gridViewFav);
        FetchFav();
        return view;
    }

    public void FetchFav()
    {
        Cursor cursor = getActivity().getContentResolver().query(MovieDBContentProvider.CONTENT_URI, null, null, null, null);

        if (!cursor.moveToFirst()) {
            Toast.makeText(getActivity(), " no record yet!", Toast.LENGTH_SHORT).show();
        } else {
            int i=0;
            int length = cursor.getCount();
            movieID = new String[length];
            Poster = new String[length];
            Title = new String[length];
            do {
                movieID[i] = cursor.getString(cursor.getColumnIndex(DBParameters.MOVIE_ENTRY.COLUMN_MOVIE_ID));
                Poster[i] = cursor.getString(cursor.getColumnIndex(DBParameters.MOVIE_ENTRY.COLUMN_POSTER));
                Title[i] = cursor.getString(cursor.getColumnIndex(DBParameters.MOVIE_ENTRY.COLUMN_TITLE));
                i++;
            } while (cursor.moveToNext());
            Fav_List_Implement adapter = new Fav_List_Implement(getActivity(),Poster);
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(MainActivity.isSinglePane)
                    {
                        favourite_detail favDetailFragment = new favourite_detail();
                        Bundle bundle = new Bundle();
                        bundle.putString("movieID",movieID[i]);
                        favDetailFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.phone_container2, favDetailFragment);
                        fragmentTransaction.isAddToBackStackAllowed();
                        fragmentTransaction.addToBackStack("moviefavDetail");
                        fragmentTransaction.commit();
                    }
                    else
                    {
                        favourite_detail favDetailFragment = new favourite_detail();
                        Bundle bundle = new Bundle();
                        bundle.putString("movieID",movieID[i]);
                        favDetailFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.detail_fav_fragment, favDetailFragment);
                        fragmentTransaction.commit();
                    }
                }
            });
        }
    }
}
