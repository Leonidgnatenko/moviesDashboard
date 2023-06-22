package com.geeerty.slyjoker.data;

import android.provider.BaseColumns;

public class MovieContract {

    private MovieContract(){

    }

    public class MovieEntry implements BaseColumns {

        public final static String TABLE_NAME = "movies";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_MOVIE_NAME = "name";
        public final static String COLUMN_MOVIE_GENRE = "genre";
        public final static String COLUMN_MOVIE_TRAILER = "trailer";
        public final static String COLUMN_MOVIE_POSTER = "posterImage";
        public final static String COLUMN_MOVIE_VIDEO_POSTER = "videoImage";
        public final static String COLUMN_MOVIE_RELEASE_DATE = "releaseDate";
        public final static String COLUMN_MOVIE_RATING = "rating";
        public final static String COLUMN_MOVIE_PLOT = "plot";
        public final static String COLUMN_MOVIE_VOTE_COUNT = "voteCount";

    }
}