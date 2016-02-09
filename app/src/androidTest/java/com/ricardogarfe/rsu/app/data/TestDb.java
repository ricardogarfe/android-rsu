/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ricardogarfe.rsu.app.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(RSUDbHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(RSUContract.LocationEntry.TABLE_NAME);
        tableNameHashSet.add(RSUContract.ContainerEntry.TABLE_NAME);

        mContext.deleteDatabase(RSUDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new RSUDbHelper(
                                                   this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                          c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without both the location, type container entry tables",
                          tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + RSUContract.LocationEntry.TABLE_NAME + ")",
                               null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                          c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(RSUContract.LocationEntry._ID);
        locationColumnHashSet.add(RSUContract.LocationEntry.COLUMN_CITY_NAME);
        locationColumnHashSet.add(RSUContract.LocationEntry.COLUMN_LAT_DEST);
        locationColumnHashSet.add(RSUContract.LocationEntry.COLUMN_LONG_DEST);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                          locationColumnHashSet.isEmpty());
        db.close();
    }

    /**
     * Test Location Entry Database insert operations.
     */
    public void testLocationTable() {
        insertLocation();
    }

    /**
     * Test Type Entry Database insert operations.
     */
    public void testTypeTable() {
        insertType();
    }

    /**
     * Test Container Entry Database insert operations.
     */
    public void testContainerTable() {

        // First insert the location and type, and then use the locationRowId and typeRowId to insert
        // the container. Make sure to cover as many failure cases as you can.
        long locationRowId = insertLocation();

        // Make sure we have a valid row ID.
        assertFalse("Error: Location Not Inserted Correctly", locationRowId == -1L);

        long typeRowId = insertType();

        // Make sure we have a valid row ID.
        assertFalse("Error: Container Type Not Inserted Correctly", typeRowId == -1L);

        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        RSUDbHelper dbHelper = new RSUDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step Container: Create container values
        ContentValues containerValues = TestUtilities.createContainerValues(locationRowId, typeRowId);

        // Third Step (Weather): Insert ContentValues into database and get a row ID back
        long containerRowId = db.insert(RSUContract.ContainerEntry.TABLE_NAME, null, containerValues);
        assertTrue(containerRowId != -1);

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor containerCursor = db.query(
                                                 RSUContract.ContainerEntry.TABLE_NAME,  // Table to Query
                                                 null, // leaving "columns" null just returns all the columns.
                                                 null, // cols for "where" clause
                                                 null, // values for "where" clause
                                                 null, // columns to group by
                                                 null, // columns to filter by row groups
                                                 null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue("Error: No Records returned from container query", containerCursor.moveToFirst());

        // Fifth Step: Validate the location Query
        TestUtilities.validateCurrentRecord("testInsertReadDb ContainerEntry failed to validate",
                                                   containerCursor, containerValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from container query",
                           containerCursor.moveToNext());

        // Sixth Step: Close cursor and database
        containerCursor.close();
        dbHelper.close();
    }

    /*
        Insert LocationEntry to Database
     */
    public long insertLocation() {
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        RSUDbHelper dbHelper = new RSUDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Create ContentValues of what you want to insert
        ContentValues testValues = TestUtilities.createValenciaLocationValues();

        // Third Step: Insert ContentValues into database and get a row ID back
        long locationRowId;
        locationRowId = db.insert(RSUContract.LocationEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                                        RSUContract.LocationEntry.TABLE_NAME,  // Table to Query
                                        null, // all columns
                                        null, // Columns for the "where" clause
                                        null, // Values for the "where" clause
                                        null, // columns to group by
                                        null, // columns to filter by row groups
                                        null // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertTrue("Error: No Records returned from location query", cursor.moveToFirst());

        // Fifth Step: Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: Location Query Validation Failed",
                                                   cursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from location query",
                           cursor.moveToNext());

        // Sixth Step: Close Cursor and Database
        cursor.close();
        db.close();
        return locationRowId;
    }

    /*
        Insert TypeEntry to Database
     */
    private long insertType() {

        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        RSUDbHelper dbHelper = new RSUDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Create ContentValues of what you want to insert
        ContentValues testValues = TestUtilities.createTypeValues();

        // Third Step: Insert ContentValues into database and get a row ID back
        long typeRowId;
        typeRowId = db.insert(RSUContract.TypeEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue(typeRowId != -1);

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                                        RSUContract.TypeEntry.TABLE_NAME,  // Table to Query
                                        null, // all columns
                                        null, // Columns for the "where" clause
                                        null, // Values for the "where" clause
                                        null, // columns to group by
                                        null, // columns to filter by row groups
                                        null // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertTrue("Error: No Records returned from type query", cursor.moveToFirst());

        // Fifth Step: Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: Type Query Validation Failed",
                                                   cursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from type query",
                           cursor.moveToNext());

        // Sixth Step: Close Cursor and Database
        cursor.close();
        db.close();
        return typeRowId;

    }
}
