package com.ricardogarfe.rsu.app.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

public class TestUriMatcher extends AndroidTestCase {
    private static final String TEST_TYPE = "pilas";
    private static final long TEST_LAT = 222222 ;
    private static final long TEST_LONG = -333333;

    // content://com.ricardogarfe.rsu.app/container
    private static final Uri TEST_CONTAINER_DIR = RSUContract.ContainerEntry.CONTENT_URI;
    private static final Uri TEST_CONTAINER_WITH_TYPE_DIR = RSUContract.ContainerEntry.buildContainerWithType(TEST_TYPE);
    private static final Uri TEST_CONTAINER_WITH_LOCATION_DIR = RSUContract.ContainerEntry.buildContainerWithLocation(TEST_LAT, TEST_LONG);
    private static final Uri TEST_CONTAINER_WITH_TYPE_AND_LOCATION_DIR = RSUContract.ContainerEntry.buildContainerWithTypeAndLocation(TEST_TYPE, TEST_LAT, TEST_LONG);

    // content://com.ricardogarfe.rsu.app/location
    private static final Uri TEST_LOCATION_DIR = RSUContract.LocationEntry.CONTENT_URI;

    // content://com.ricardogarfe.rsu.app/type
    private static final Uri TEST_TYPE_DIR = RSUContract.TypeEntry.CONTENT_URI;

    public void testUriMatcher() {
        UriMatcher testMatcher = RSUProvider.buildUriMatcher();

        assertEquals("Error: The CONTAINER URI was matched incorrectly.",
                            testMatcher.match(TEST_CONTAINER_DIR), RSUProvider.CONTAINER);
        assertEquals("Error: The CONTAINER WITH TYPE URI was matched incorrectly.",
                            testMatcher.match(TEST_CONTAINER_WITH_TYPE_DIR), RSUProvider.CONTAINER_WITH_TYPE);

        assertEquals("Error: The LOCATION URI was matched incorrectly.",
                            testMatcher.match(TEST_LOCATION_DIR), RSUProvider.LOCATION);
        assertEquals("Error: The TYPE URI was matched incorrectly.",
                            testMatcher.match(TEST_TYPE_DIR), RSUProvider.TYPE);
    }
}
