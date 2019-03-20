package com.derrick.popularmoviesstage2.utils;

import android.content.Context;

import com.derrick.popularmoviesstage2.data.MovieRepository;
import com.derrick.popularmoviesstage2.data.local.MovieDatabase;
import com.derrick.popularmoviesstage2.data.network.MovieNetworkDataSource;
import com.derrick.popularmoviesstage2.ui.details.DetailsViewModelFactory;
import com.derrick.popularmoviesstage2.ui.details.ReviewViewModelFactory;
import com.derrick.popularmoviesstage2.ui.main.MainViewModelFactory;

public class InjectorUtils {

    public static MovieNetworkDataSource provideNetworkDataSource(Context context) {
        // This call to provide repository is necessary if the app starts from a service - in this
        // case the repository will not exist unless it is specifically created.
        return MovieNetworkDataSource.getsInstance(context.getApplicationContext());
    }

    public static MovieRepository provideRepository(Context context) {

        MovieDatabase database = MovieDatabase.getInstance(context.getApplicationContext());

        MovieNetworkDataSource networkDataSource =
                MovieNetworkDataSource.getsInstance(context.getApplicationContext());

        AppExecutors executors = AppExecutors.getInstance();

        return MovieRepository.getsInstance(database, networkDataSource, executors, context);
    }

    public static MainViewModelFactory provideMainViewModelFactory(Context context, String sorting_value) {
        MovieRepository repository = provideRepository(context.getApplicationContext());
        return new MainViewModelFactory(repository, sorting_value);
    }

    public static DetailsViewModelFactory provideDetailViewModelFactory(Context context, long movie_id) {
        MovieRepository repository = provideRepository(context.getApplicationContext());
        return new DetailsViewModelFactory(repository, movie_id);
    }

    public static ReviewViewModelFactory provideReviewsViewModelFactory(Context context, long movie_id) {
        MovieRepository repository = provideRepository(context.getApplicationContext());
        return new ReviewViewModelFactory(repository, movie_id);
    }


}
