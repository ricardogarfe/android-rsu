package com.ricardogarfe.rsu.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.ricardogarfe.rsu.app.R;
import com.ricardogarfe.rsu.app.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

public class TestUtilities extends AndroidTestCase {

    static final String TEST_TYPE = "pilas";
    static final long TEST_LAT = 222222 ;
    static final long TEST_LONG = -333333;

    static final String CONTAINER_TITLE = "CONTENEDORES DE PILAS";
    static final String CONTAINER_MESSAGE = "MERCADO ROJAS CLEMENTE\\nBotanico\\nNúmero de contenedores: 1";
    static final String CITY_NAME = "Valencia";
    static final long CONTAINER_DISTANCE = 257;
    static final long LAT_DEST = 39472993;
    static final long LONG_DEST = -385258;
    // intentionally includes a slash to make sure Uri is getting quoted correctly

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                                 "' did not match the expected value '" +
                                 expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    /*
        Create some default Container values for your database tests.
     */
    static ContentValues createContainerValues(long locationRowId, long typeId) {
        ContentValues containerValues = new ContentValues();
        containerValues.put(RSUContract.ContainerEntry.COLUMN_LOC_KEY, locationRowId);
        containerValues.put(RSUContract.ContainerEntry.COLUMN_TYPE_KEY, typeId);
        containerValues.put(RSUContract.ContainerEntry.COLUMN_TITLE, CONTAINER_TITLE);
        containerValues.put(RSUContract.ContainerEntry.COLUMN_MESSAGE, CONTAINER_MESSAGE);
        containerValues.put(RSUContract.ContainerEntry.COLUMN_DISTANCE, CONTAINER_DISTANCE);

        return containerValues;
    }

    /*
        Create some default Location values for your database tests.
     */
    static ContentValues createValenciaLocationValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(RSUContract.LocationEntry.COLUMN_CITY_NAME, CITY_NAME);
        testValues.put(RSUContract.LocationEntry.COLUMN_LAT_DEST, LAT_DEST);
        testValues.put(RSUContract.LocationEntry.COLUMN_LONG_DEST, LONG_DEST);

        return testValues;
    }

    /*
        Create some default Type values for your database tests.
     */
    static ContentValues createTypeValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(RSUContract.TypeEntry.COLUMN_ICON, R.drawable.low_battery);
        testValues.put(RSUContract.TypeEntry.COLUMN_NAME, TEST_TYPE);
        return testValues;
    }

    static long insertValenciaLocationValues(Context context) {
        // insert our test records into the database
        RSUDbHelper dbHelper = new RSUDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createValenciaLocationValues();

        long locationRowId;
        locationRowId = db.insert(RSUContract.LocationEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Valencia Container Location Values", locationRowId != -1);

        return locationRowId;
    }

    static long insertTypeValues(Context context) {
        // insert our test records into the database
        RSUDbHelper dbHelper = new RSUDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createTypeValues();

        long typeRowId;
        typeRowId = db.insert(RSUContract.TypeEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Container Type Values", typeRowId != -1);

        return typeRowId;
    }
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }

}
