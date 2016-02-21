package com.ricardogarfe.rsu.app.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
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

    private static final SQLiteQueryBuilder containerByTypeSettingsQueryBuilder;

    static {
        containerByTypeSettingsQueryBuilder = new SQLiteQueryBuilder();
        containerByTypeSettingsQueryBuilder.setTables(
                 RSUContract.ContainerEntry.TABLE_NAME + " INNER JOIN " +
                         RSUContract.TypeEntry.TABLE_NAME +
                         " ON " + RSUContract.ContainerEntry.TABLE_NAME +
                         "." + RSUContract.ContainerEntry.COLUMN_TYPE_KEY +
                         " = " + RSUContract.TypeEntry.TABLE_NAME +
                         "." + RSUContract.TypeEntry._ID);
    }

    private static final String containerTypeByNameSelection =
            RSUContract.TypeEntry.TABLE_NAME +
                    "." + RSUContract.TypeEntry.COLUMN_NAME + " = ? ";

    /**
     *
     * Create query to find containers by type
     *
     * @param uri
     * @param projection
     * @param sortOrder
     * @return
     */
    private Cursor getContainerByTypeSettings(Uri uri, String[] projection, String sortOrder) {
        String typeSetting = RSUContract.ContainerEntry.getTypeSettingFromUri(uri);

        // Type value ti search
        String[] selectionArgs = new String[]{typeSetting};

        // Where case for type.
        String selection = containerTypeByNameSelection;

        return containerByTypeSettingsQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                                                                  projection,
                                                                  selection,
                                                                  selectionArgs,
                                                                  null,
                                                                  null,
                                                                  sortOrder
        );
    }

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
        mOpenHelper = new RSUDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (uriMatcher.match(uri)) {
            // "container"
            case CONTAINER: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                                                                           RSUContract.ContainerEntry.TABLE_NAME,
                                                                           projection,
                                                                           selection,
                                                                           selectionArgs,
                                                                           null,
                                                                           null,
                                                                           sortOrder
                );
                break;
            }
            // "container/*"
            case CONTAINER_WITH_TYPE: {
                retCursor = getContainerByTypeSettings(uri, projection, sortOrder);
                break;
            }
            // "type"
            case TYPE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                                                                           RSUContract.TypeEntry.TABLE_NAME,
                                                                           projection,
                                                                           selection,
                                                                           selectionArgs,
                                                                           null,
                                                                           null,
                                                                           sortOrder
                );
                break;
            }
            // "location"
            case LOCATION: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                                                                           RSUContract.LocationEntry.TABLE_NAME,
                                                                           projection,
                                                                           selection,
                                                                           selectionArgs,
                                                                           null,
                                                                           null,
                                                                           sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        final int match = uriMatcher.match(uri);

        switch (match) {
            case CONTAINER:
                return RSUContract.ContainerEntry.CONTENT_ITEM_TYPE;
            case TYPE:
                return RSUContract.TypeEntry.CONTENT_ITEM_TYPE;
            case LOCATION:
                return RSUContract.LocationEntry.CONTENT_ITEM_TYPE;
            case CONTAINER_WITH_TYPE:
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
