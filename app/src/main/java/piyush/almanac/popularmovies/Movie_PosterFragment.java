package piyush.almanac.popularmovies;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Movie_PosterFragment extends Fragment {

    RequestQueue requestQueue;
    GridView gridView;
    String mainurl;
    String[] movieposters;
    String[] movieID;
    JSONArray moviedetail;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie__poster, container, false);
        setHasOptionsMenu(true);

        gridView=(GridView)view.findViewById(R.id.gridView);

        requestQueue = Volley.newRequestQueue(getActivity());
        MovieDetailReceive("popular");

        return view;
    }

    public void MovieDetailReceive(final String parturl)
    {

        mainurl= "http://api.themoviedb.org/3/movie/"+parturl+"?api_key=a9921873d439557b59941c8130548062";
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.POST, mainurl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    moviedetail=response.getJSONArray("results");
                    movieposters= new String[moviedetail.length()];
                    movieID = new String[moviedetail.length()];
                    for(int i=0;i<moviedetail.length();i++)
                    {
                        movieID[i] = moviedetail.getJSONObject(i).getString("id");
                        movieposters[i]=MovieUtils.BASE_PICTURE_URL+MovieUtils.PICTURE_SIZE1+moviedetail.getJSONObject(i).getString("poster_path");
                    }
                    imageadapter adapter = new imageadapter(getActivity(),movieposters);
                    gridView.setAdapter(adapter);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            if(MainActivity.isSinglePane)
                            {
                                MovieDetail myDetailFragment = new MovieDetail();
                                Bundle bundle = new Bundle();
                                bundle.putString("pos", String.valueOf(i));
                                bundle.putString("url",parturl);
                                bundle.putString("movieID",movieID[i]);
                                myDetailFragment.setArguments(bundle);
                                FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.phone_container1, myDetailFragment);
                                fragmentTransaction.isAddToBackStackAllowed();
                                fragmentTransaction.addToBackStack("movieDetail");
                                fragmentTransaction.commit();
                            }
                            else
                            {
                                MovieDetail myDetailFragment = new MovieDetail();
                                Bundle bundle = new Bundle();
                                bundle.putString("pos",String.valueOf(i));
                                bundle.putString("url",parturl);
                                bundle.putString("movieID",movieID[i]);
                                myDetailFragment.setArguments(bundle);
                                FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.detail_fragment, myDetailFragment);
                                fragmentTransaction.commit();
                            }
                        }
                    });
                    }
                catch (JSONException e)
                {
                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), "Internet Not Available", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);






    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_popular) {
            MovieDetailReceive("popular");

            return true;
        }
        else if(id == R.id.action_toprated){

            MovieDetailReceive("top_rated");
            return true;
        }
        else if(id==R.id.action_fav){
            startActivity(new Intent(getActivity(), Favourite.class));
        }
        return super.onOptionsItemSelected(item);
}
    }
