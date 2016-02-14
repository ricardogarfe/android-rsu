package com.ricardogarfe.rsu.app.data;

import android.test.AndroidTestCase;

public class TestRSUProvider extends AndroidTestCase {

    public void testGetType() {

        // content://com.ricardogarfe.rsu.app/container/
        String type = mContext.getContentResolver().getType(RSUContract.ContainerEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.ricardogarfe.rsu.app/weather
        assertEquals("Error: the ContainerEntry CONTENT_URI should return ContainerEntry.CONTENT_TYPE",
                            RSUContract.ContainerEntry.CONTENT_TYPE, type);

        // content://com.example.android.sunshine.app/type/
        type = mContext.getContentResolver().getType(RSUContract.TypeEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.ricardogarfe.rsu.app/type
        assertEquals("Error: the TypeEntry CONTENT_URI should return LocationEntry.CONTENT_TYPE",
                            RSUContract.TypeEntry.CONTENT_TYPE, type);

        // content://com.example.android.sunshine.app/location/
        type = mContext.getContentResolver().getType(RSUContract.LocationEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.ricardogarfe.rsu.app/location
        assertEquals("Error: the LocationEntry CONTENT_URI should return LocationEntry.CONTENT_TYPE",
                            RSUContract.LocationEntry.CONTENT_TYPE, type);
    }
}
