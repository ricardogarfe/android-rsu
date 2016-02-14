package com.ricardogarfe.rsu.app.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

public class RSUProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher uriMatcher = buildUriMatcher();
    private RSUDbHelper mOpenHelper;

    static final int CONTAINER = 100;
    static final int CONTAINER_WITH_TYPE = 101;
    static final int CONTAINER_WITH_LOCATION = 102;
    static final int CONTAINER_WITH_TYPE_AND_LOCATION  = 103;
    static final int LOCATION = 200;
    static final int TYPE = 300;

    /**
     * Create URIMatcher to expose API to database.
     *
     * @return
     */
    static UriMatcher buildUriMatcher() {

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RSUContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, RSUContract.PATH_CONTAINER, CONTAINER);
        matcher.addURI(authority, RSUContract.PATH_CONTAINER + "/*", CONTAINER_WITH_TYPE);

        // Location
        matcher.addURI(authority, RSUContract.PATH_LOCATION, LOCATION);

        // Type
        matcher.addURI(authority, RSUContract.PATH_TYPE, TYPE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        final int match = uriMatcher.match(uri);

        switch (match) {
            case CONTAINER:
                return RSUContract.ContainerEntry.CONTENT_TYPE;
            case TYPE:
                return RSUContract.TypeEntry.CONTENT_TYPE;
            case LOCATION:
                return RSUContract.LocationEntry.CONTENT_TYPE;
            case CONTAINER_WITH_TYPE:
                return RSUContract.ContainerEntry.CONTENT_TYPE;
            case CONTAINER_WITH_LOCATION:
                return RSUContract.ContainerEntry.CONTENT_TYPE;
            case CONTAINER_WITH_TYPE_AND_LOCATION:
                return RSUContract.ContainerEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri:\t" + uri);

        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
