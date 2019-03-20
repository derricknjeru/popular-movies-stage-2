package com.derrick.popularmoviesstage2.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.derrick.popularmoviesstage2.BuildConfig;
import com.derrick.popularmoviesstage2.R;
import com.derrick.popularmoviesstage2.data.local.MoviePreferences;
import com.derrick.popularmoviesstage2.databinding.ActivityMainBinding;
import com.derrick.popularmoviesstage2.ui.details.DetailsActivity;
import com.derrick.popularmoviesstage2.utils.AppExecutors;
import com.derrick.popularmoviesstage2.utils.InjectorUtils;
import com.derrick.popularmoviesstage2.utils.LogUtils;

import java.io.IOException;

import static com.derrick.popularmoviesstage2.ui.details.DetailsActivity.EXTRA_MOVIE_DATA;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String SAVED_MOVIES_LIST = "saved_movies";
    private MovieAdapter adapter;
    private ActivityMainBinding mMainBinding;
    private MainActivityViewModel mViewModel;
    String query = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mMainBinding.toolbar);

        initializeViews();

        query = MoviePreferences.getSortingQuery(this, getString(R.string.pref_sorting_key));
        LogUtils.showLog(LOG_TAG, "@Movie query::" + query);

        setToolBarTitle(query);

        fetchMovies();

        adapter.setOnListClickLister(movie_id -> {

            Intent i = new Intent(MainActivity.this, DetailsActivity.class);
            i.putExtra(EXTRA_MOVIE_DATA, movie_id);
            startActivity(i);

        });

        listenToFavoriteEmptyResponse();
        listenToRetrofitError();

    }


    private void setToolBarTitle(String query) {
        getSupportActionBar().setTitle(query);
        //noinspection SimplifiableIfStatement
        if (query.contentEquals(getString(R.string.pref_sorting_default_value))) {
            getSupportActionBar().setTitle(getString(R.string.most_popular));
        }
        if (query.contentEquals(getString(R.string.pref_sorting_top_rated))) {
            getSupportActionBar().setTitle(getString(R.string.highest_rated));
        }
        if (query.contentEquals(getString(R.string.pref_sorting_favourite))) {
            getSupportActionBar().setTitle(getString(R.string.fav));
        }


    }

    private void fetchMovies() {

        if (BuildConfig.API_KEY.contentEquals(getString(R.string.add_key_message))) {
            mMainBinding.mainContent.errorTv.setVisibility(View.VISIBLE);
            mMainBinding.mainContent.errorTv.setText(getString(R.string.add_api_key_error_message));
            mMainBinding.mainContent.contentPbar.hide();
            return;
        }

        MainViewModelFactory factory = InjectorUtils.provideMainViewModelFactory(this, query);
        mViewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);


        mViewModel.getMoviesList().observe(this, results -> {
            adapter.setResults(null);
            adapter.setResults(results);
            // Show the movie list or the loading screen based on saved movies
            // and is loaded
            if (results != null && results.size() != 0) {
                showMovieDataView();
                LogUtils.showLog(LOG_TAG, "@Movie observe total::" + results.size());


            } else showLoading();
        });


    }


    private void showLoading() {
        if (!MoviePreferences.getSortingQuery(this, getString(R.string.pref_sorting_key)).contentEquals(getString(R.string.pref_sorting_favourite))) {
            mMainBinding.mainContent.contentPbar.show();
            mMainBinding.mainContent.errorTv.setVisibility(View.GONE);
            mMainBinding.mainContent.recyclerView.setVisibility(View.INVISIBLE);
        } else {
            mMainBinding.mainContent.errorTv.setVisibility(View.VISIBLE);
            mMainBinding.mainContent.errorTv.setText(getString(R.string.no_fav));
            mMainBinding.mainContent.contentPbar.hide();
            mMainBinding.mainContent.recyclerView.setVisibility(View.INVISIBLE);
        }
    }

    private void showMovieDataView() {
        mMainBinding.mainContent.errorTv.setVisibility(View.GONE);
        mMainBinding.mainContent.contentPbar.hide();
        mMainBinding.mainContent.recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);


    }


    private void initializeViews() {
        int numberOfColumns = 2;
        mMainBinding.mainContent.recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        mMainBinding.mainContent.recyclerView.setHasFixedSize(true);
        adapter = new MovieAdapter();
        mMainBinding.mainContent.recyclerView.setAdapter(adapter);
        mMainBinding.mainContent.contentPbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        initialToggle(menu);
        return true;
    }


    /**
     * Used when menu is created or recreated
     *
     * @param menu
     */
    private void initialToggle(Menu menu) {
        LogUtils.showLog(LOG_TAG,"@Movie query toggle:"+query);
        if (query.contentEquals(getString(R.string.pref_sorting_top_rated))) {
            menu.findItem(R.id.action_top_rated).setChecked(true);
        }
        if (query.contentEquals(getString(R.string.pref_sorting_favourite))) {
            menu.findItem(R.id.action_favorite).setChecked(true);
        }
        if (query.contentEquals(getString(R.string.pref_sorting_default_value))) {
            menu.findItem(R.id.action_popular).setChecked(true);
        }
    }

    /**
     * Display an empty message when user has not added any movie to favorite
     */
    private void listenToFavoriteEmptyResponse() {
        mViewModel.getNoFav().observe(this, s -> {
            LogUtils.showLog(LOG_TAG, "@Movie nofav::" + s);
            adapter.setResults(null);
            mMainBinding.mainContent.errorTv.setVisibility(View.VISIBLE);
            mMainBinding.mainContent.errorTv.setText(s);
            mMainBinding.mainContent.contentPbar.hide();

        });
    }

    private void listenToRetrofitError() {
        mViewModel.getRetrofitError().observe(this, t -> {
            mMainBinding.mainContent.errorTv.setVisibility(View.VISIBLE);

            mMainBinding.mainContent.contentPbar.hide();

            if (t instanceof IOException) {
                mMainBinding.mainContent.errorTv.setText("Check your network connections");
            } else {
                mMainBinding.mainContent.errorTv.setText(t.getLocalizedMessage());
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_popular) {
            toggleMenu(item);
            MoviePreferences.setSortingQuery(this, getString(R.string.pref_sorting_default_value));
            return true;
        }

        if (id == R.id.action_top_rated) {
            toggleMenu(item);
            MoviePreferences.setSortingQuery(this, getString(R.string.pref_sorting_top_rated));
            return true;
        }

        if (id == R.id.action_favorite) {
            toggleMenu(item);
            MoviePreferences.setSortingQuery(this, getString(R.string.pref_sorting_favourite));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Used on click of a menu
     *
     * @param item
     */
    private void toggleMenu(MenuItem item) {
        if (item.isChecked()) item.setChecked(false);
        else item.setChecked(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        query = MoviePreferences.getSortingQuery(this, key);
        setToolBarTitle(query);
        adapter.setResults(null);
        mMainBinding.mainContent.contentPbar.show();
        AppExecutors.getInstance().diskIO().execute(() -> InjectorUtils.provideRepository(getApplicationContext()).fetchMovies(query));

    }


}
