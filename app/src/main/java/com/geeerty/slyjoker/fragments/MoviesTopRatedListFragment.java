package com.geeerty.slyjoker.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import com.geeerty.slyjoker.R;
import com.geeerty.slyjoker.Utils.Constants;
import com.geeerty.slyjoker.Utils.SpacesItemDecoration;
import com.geeerty.slyjoker.activity.FavActivity;
import com.geeerty.slyjoker.adapter.RecyclerAdapterGrid;
import com.geeerty.slyjoker.api.ApiClient;
import com.geeerty.slyjoker.api.ApiInterface;
import com.geeerty.slyjoker.model.Movies;
import com.geeerty.slyjoker.response.MovieResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.geeerty.slyjoker.Utils.Constants.API_KEY;
import static com.geeerty.slyjoker.Utils.Constants.GRID_TRHEE;
import static com.geeerty.slyjoker.Utils.Constants.GRID_TWO;


public class MoviesTopRatedListFragment extends Fragment implements RecyclerAdapterGrid.ClickListener {

    private static final String STATE_MOVIES = "state_movie";
    GridLayoutManager layoutManager;
    RecyclerAdapterGrid adapter;
    com.geeerty.slyjoker.fragments.MoviesPopularListFragment.OnHeadLineSelectedListener callback;
    private int scrollPosition;
    private ArrayList<Movies> movies;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.empty_view)
    View emptyView;
    @BindView(R.id.toolbar)
    Toolbar tb;
    private ArrayList<Movies> savedMovies;
    private static int count;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.movies_list_layout, container, false);

        ButterKnife.bind(this, v);

        emptyView.setVisibility(View.GONE);

        recyclerView.setHasFixedSize(true);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(getActivity(), GRID_TWO);
            recyclerView.setLayoutManager(layoutManager);
        } else {
            layoutManager = new GridLayoutManager(getActivity(), GRID_TRHEE);
            recyclerView.setLayoutManager(layoutManager);
        }
        recyclerView.addItemDecoration(new SpacesItemDecoration(1, 1, false));

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.material_orangeA700),
                ContextCompat.getColor(getActivity(),R.color.material_greenA700),
                ContextCompat.getColor(getActivity(),R.color.material_amberA400));

        ((AppCompatActivity) getActivity()).setSupportActionBar(tb);

        if (savedInstanceState != null) {
            savedMovies = savedInstanceState.getParcelableArrayList(STATE_MOVIES);
            adapter = new RecyclerAdapterGrid(savedMovies, getActivity());
            adapter.setClickListener(com.geeerty.slyjoker.fragments.MoviesTopRatedListFragment.this);
            recyclerView.setAdapter(adapter);
            count = 1;
        } else {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    if (Constants.isConnected(getActivity())) {
                        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        swipeRefreshLayout.setRefreshing(true);
                        showTopRatedList();
                        emptyView.setVisibility(View.GONE);
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
                    } else {
                        emptyView.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Network isn't available", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Constants.isConnected(getActivity())) {
                    showTopRatedList();
                    emptyView.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Network isn't available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (count == 1) {
            outState.putParcelableArrayList(STATE_MOVIES, savedMovies);
        } else {
            outState.putParcelableArrayList(STATE_MOVIES, movies);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (MoviesPopularListFragment.OnHeadLineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString().concat(" must implement OnHeadLineSelectedListener"));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_popular) {
            com.geeerty.slyjoker.fragments.MoviesPopularListFragment fragment = new com.geeerty.slyjoker.fragments.MoviesPopularListFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment).commit();
            return true;
        }
        if (id == R.id.action_sort_rating) {
            Toast.makeText(getActivity(), "Already Showing Top Rated List", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_fav) {
            startActivity(new Intent(getActivity(), FavActivity.class));
            return true;
        }

        return false;
    }

    void showTopRatedList() {
        swipeRefreshLayout.setRefreshing(true);
        ApiInterface apiService =
                ApiClient.getClient(getActivity()).create(ApiInterface.class);

        Call<MovieResponse> call = apiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (movies != null)
                    movies.clear();
                movies = response.body().getResults();
                adapter = new RecyclerAdapterGrid(movies, getActivity());
                adapter.setClickListener(com.geeerty.slyjoker.fragments.MoviesTopRatedListFragment.this);
                recyclerView.setAdapter(adapter);
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
            }
        });
    }

    @Override
    public void onItemClick(View v, int pos) {
        List<Integer> genreIds;
        String genre, posterPath, plot, releaseDate, id, title, rating, videoImage, vote_count;
        Movies currentMovies;
        if (count == 1) {
            currentMovies = savedMovies.get(pos);
        } else {
            currentMovies = movies.get(pos);
        }
        genreIds = currentMovies.getGenreIds();
        genre = Movies.changeGenreToString(genreIds);
        posterPath = currentMovies.getPosterURL();
        plot = currentMovies.getPlot();
        releaseDate = currentMovies.getDate();
        id = currentMovies.getId();
        title = currentMovies.getTitle();
        rating = currentMovies.getRating();
        videoImage = currentMovies.getVideoImage();
        vote_count = currentMovies.getVoteCount();

        MoviesDetailsFragment.newInstance(posterPath, plot, releaseDate, id, title, rating, videoImage, genre, vote_count);
        callback.onArticleSelected(pos);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (layoutManager != null) {
            scrollPosition = layoutManager.findFirstVisibleItemPosition();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (layoutManager != null) {
            recyclerView.scrollToPosition(scrollPosition);
        }
    }

}
