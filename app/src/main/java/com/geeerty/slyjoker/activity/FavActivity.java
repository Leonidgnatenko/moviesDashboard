package com.geeerty.slyjoker.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.geeerty.slyjoker.R;
import com.geeerty.slyjoker.Utils.Constants;
import com.geeerty.slyjoker.fragments.MoviesDetailsFavFragment;
import com.geeerty.slyjoker.fragments.MoviesFavoriteListFragment;


public class FavActivity extends AppCompatActivity implements MoviesFavoriteListFragment.OnHeadLineSelectedListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_fav);
        if (findViewById(R.id.content1) != null) {
            if (savedInstanceState != null) {
                return;
            }
            //Create an instance of Fragment Class which has the list i.e. HeadlinesFragment
            MoviesFavoriteListFragment favoriteFragment = new MoviesFavoriteListFragment();
            //In the case Activity was started with special instruction from an intent.
            //Pass the Intent's extras to the fragment as arguments
            favoriteFragment.setArguments(getIntent().getExtras());
            //Ask the FragmentManager to add it to main activity's FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content1, favoriteFragment)
                    .commit();
        }
    }
    @Override
    public void onArticleSelected(int position) {
        //Capture the article's fragment from the activity's dual-pane layout
        MoviesDetailsFavFragment articleFragment = (MoviesDetailsFavFragment) getSupportFragmentManager().findFragmentById(R.id.articlesfragment1);
        //if we dont find one we must not be in two pane mode
        //lets swap the fragments intead
        if (articleFragment != null) {
            //we must be in two pane layout
            articleFragment.updateArticleView(position);
        } else {
            //we must be in one pane layout
            //Create fragment and give it an argument for the selected article
            MoviesDetailsFavFragment swapFragment = new MoviesDetailsFavFragment();
            Bundle args = new Bundle();
            args.putInt(Constants.ARG_POSITION, position);
            swapFragment.setArguments(args);
            //now fragment is prepared swap it
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content1, swapFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
