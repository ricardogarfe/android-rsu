package com.ricardogarfe.rsu.app.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.test.AndroidTestCase;

public class TestRSUProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestRSUProvider.class.getSimpleName();

    public void testGetType() {

        // content://com.ricardogarfe.rsu.app/container/
        String type = mContext.getContentResolver().getType(RSUContract.ContainerEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.ricardogarfe.rsu.app/container
        assertEquals("Error: the ContainerEntry CONTENT_URI should return ContainerEntry.CONTENT_TYPE",
                            RSUContract.ContainerEntry.CONTENT_ITEM_TYPE, type);

        // content://com.ricardogarfe.rsu.app/type/
        type = mContext.getContentResolver().getType(RSUContract.TypeEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.ricardogarfe.rsu.app/type
        assertEquals("Error: the TypeEntry CONTENT_URI should return LocationEntry.CONTENT_TYPE",
                            RSUContract.TypeEntry.CONTENT_ITEM_TYPE, type);

        // content://com.ricardogarfe.rsu.app/location/
        type = mContext.getContentResolver().getType(RSUContract.LocationEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.ricardogarfe.rsu.app/location
        assertEquals("Error: the LocationEntry CONTENT_URI should return LocationEntry.CONTENT_TYPE",
                            RSUContract.LocationEntry.CONTENT_ITEM_TYPE, type);
    }

    public void testBasicTypeQueries() {
        // insert our test records into the database
        RSUDbHelper dbHelper = new RSUDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createTypeValues();
        long locationRowId = TestUtilities.insertTypeValues(mContext);

        // Test the basic content provider query
        Cursor typeCursor = mContext.getContentResolver().query(
                                                                           RSUContract.TypeEntry.CONTENT_URI,
                                                                           null,
                                                                           null,
                                                                           null,
                                                                           null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicTypeQueries, type query", typeCursor, testValues);

        // Has the NotificationUri been set correctly? --- we can only test this easily against API
        // level 19 or greater because getNotificationUri was added in API level 19.
        if (Build.VERSION.SDK_INT >= 19) {
            assertEquals("Error: Type Query did not properly set NotificationUri",
                                typeCursor.getNotificationUri(), RSUContract.TypeEntry.CONTENT_URI);
        }
    }

    public void testBasicLocationQueries() {
        // insert our test records into the database
        RSUDbHelper dbHelper = new RSUDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues locationValues = TestUtilities.createValenciaLocationValues();
        long locationID = TestUtilities.insertValenciaLocationValues(mContext);

        // Test the basic content provider query
        Cursor locationCursor = mContext.getContentResolver().query(
                                                                           RSUContract.LocationEntry.CONTENT_URI,
                                                                           null,
                                                                           null,
                                                                           null,
                                                                           null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicLocationQueries, location query", locationCursor, locationValues);

        // Has the NotificationUri been set correctly? --- we can only test this easily against API
        // level 19 or greater because getNotificationUri was added in API level 19.
        if (Build.VERSION.SDK_INT >= 19) {
            assertEquals("Error: Location Query did not properly set NotificationUri",
                                locationCursor.getNotificationUri(), RSUContract.LocationEntry.CONTENT_URI);
        }
    }

    public void testBasicContainerQueries() {
        // insert our test records into the database
        RSUDbHelper dbHelper = new RSUDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Type values
        ContentValues typeValues = TestUtilities.createTypeValues();
        long typeID = TestUtilities.insertTypeValues(mContext);

        // Location values
        ContentValues locationValues = TestUtilities.createValenciaLocationValues();
        long locationID = TestUtilities.insertValenciaLocationValues(mContext);


        ContentValues containerValues = TestUtilities.createContainerValues(locationID, typeID);

        long containerId = db.insert(RSUContract.ContainerEntry.TABLE_NAME, null, containerValues);
        assertTrue("Unable to Insert ContainerEntry into the Database", containerId != -1);

        db.close();

        // Test the basic content provider query
        Cursor containerCursor = mContext.getContentResolver().query(
                                                                            RSUContract.ContainerEntry.CONTENT_URI,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicContainerQueries, container query", containerCursor, containerValues);

        // Has the NotificationUri been set correctly? --- we can only test this easily against API
        // level 19 or greater because getNotificationUri was added in API level 19.
        if (Build.VERSION.SDK_INT >= 19) {
            assertEquals("Error: Container Query did not properly set NotificationUri",
                                containerCursor.getNotificationUri(), RSUContract.ContainerEntry.CONTENT_URI);
        }
    }


    public void testInsertReadProvider() {

        // Location Insert
        ContentValues locationValues = TestUtilities.createValenciaLocationValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(RSUContract.LocationEntry.CONTENT_URI, true, tco);
        Uri locationUri = mContext.getContentResolver().insert(RSUContract.LocationEntry.CONTENT_URI, locationValues);

        // Did our content observer get called?  Students:  If this fails, your insert location
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long locationRowId = ContentUris.parseId(locationUri);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor locationCursor = mContext.getContentResolver().query(
                                                                   RSUContract.LocationEntry.CONTENT_URI,
                                                                   null, // leaving "columns" null just returns all the columns.
                                                                   null, // cols for "where" clause
                                                                   null, // values for "where" clause
                                                                   null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating LocationEntry.",
                                            locationCursor, locationValues);

        // Type Insert
        ContentValues typeValues = TestUtilities.createTypeValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(RSUContract.TypeEntry.CONTENT_URI, true, tco);
        Uri typeUri = mContext.getContentResolver().insert(RSUContract.TypeEntry.CONTENT_URI, typeValues);

        // Did our content observer get called?  Students:  If this fails, your insert location
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long typeRowId = ContentUris.parseId(typeUri);

        // Verify we got a row back.
        assertTrue(typeRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor typeCursor = mContext.getContentResolver().query(
                                                                   RSUContract.TypeEntry.CONTENT_URI,
                                                                   null, // leaving "columns" null just returns all the columns.
                                                                   null, // cols for "where" clause
                                                                   null, // values for "where" clause
                                                                   null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating TypeEntry.",
                                            typeCursor, typeValues);


        // Container test values
        ContentValues containerValues = TestUtilities.createContainerValues(locationRowId, typeRowId);
        // The TestContentObserver is a one-shot class
        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(RSUContract.ContainerEntry.CONTENT_URI, true, tco);
        Uri containerInsertUri = mContext.getContentResolver()
                                       .insert(RSUContract.ContainerEntry.CONTENT_URI, containerValues);

        assertTrue(containerInsertUri != null);

        // Did our content observer get called?  Students:  If this fails, your insert location
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long containerRowId = ContentUris.parseId(containerInsertUri);

        // Verify we got a row back.
        assertTrue(containerRowId != -1);

        // A cursor is your primary interface to the query results.
        Cursor containerCursor = mContext.getContentResolver().query(
                                                                          RSUContract.ContainerEntry.CONTENT_URI,  // Table to Query
                                                                          null, // leaving "columns" null just returns all the columns.
                                                                          null, // cols for "where" clause
                                                                          null, // values for "where" clause
                                                                          null // columns to group by
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating ContainerEntry insert.",
                                            containerCursor, containerValues);

        // Get the joined Container and Type data
        containerCursor = mContext.getContentResolver().query(
                                                                     RSUContract.ContainerEntry.buildContainerWithType(TestUtilities.TEST_TYPE),
                                                                     null, // leaving "columns" null just returns all the columns.
                                                                     null, // cols for "where" clause
                                                                     null, // values for "where" clause
                                                                     null  // sort order
        );
        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Container type Data.",
                                            containerCursor, containerValues);

    }

    /*
       This helper function deletes all records from both database tables using the database
       functions only.  This is designed to be used to reset the state of the database until the
       delete functionality is available in the ContentProvider.
    */
    public void deleteAllRecordsFromDB() {
        RSUDbHelper dbHelper = new RSUDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(RSUContract.ContainerEntry.TABLE_NAME, null, null);
        db.delete(RSUContract.LocationEntry.TABLE_NAME, null, null);
        db.delete(RSUContract.TypeEntry.TABLE_NAME, null, null);
        db.close();
    }

    /*
        Student: Refactor this function to use the deleteAllRecordsFromProvider functionality once
        you have implemented delete functionality there.
     */
    public void deleteAllRecords() {
        deleteAllRecordsFromDB();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

}
