package com.ricardogarfe.rsu.app.data;

import android.net.Uri;
import android.test.AndroidTestCase;

import java.util.List;

public class TestRSUContract extends AndroidTestCase {

    // intentionally includes a slash to make sure Uri is getting quoted correctly
    private static final String TEST_TYPE = "pilas";
    private static final long TEST_LAT = 222222 ;
    private static final long TEST_LONG = -333333;


    public void testBuildContainerWithType() {
        Uri containerWithTypeUri = RSUContract.ContainerEntry.buildContainerWithType(TEST_TYPE);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildWeatherLocation in " +
                              "ContainerContract.",
                             containerWithTypeUri);
        assertEquals("Error: Container type not properly appended to the end of the Uri",
                            TEST_TYPE, containerWithTypeUri.getLastPathSegment());
        assertEquals("Error: Container type Uri doesn't match our expected result",
                            containerWithTypeUri.toString(),
                            "content://com.ricardogarfe.rsu.app/container/pilas");
    }

    public void testBuildContainerWithTypeAndLocation() {
        Uri containerWithTypeAndLocationUri = RSUContract.ContainerEntry.buildContainerWithTypeAndLocation(TEST_TYPE, TEST_LAT, TEST_LONG);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildContainerWithTypeAndLocation in " +
                              "ContainerContract.",
                             containerWithTypeAndLocationUri);
        List<String> pathSegments = containerWithTypeAndLocationUri.getPathSegments();
        assertEquals("Error: Container type not properly appended to the end of the Uri",
                            TEST_TYPE, pathSegments.get(1));
        assertEquals("Error: Container Lat not properly appended to the end of the Uri",
                            Long.toString(TEST_LAT), pathSegments.get(2));
        assertEquals("Error: Container Long not properly appended to the end of the Uri",
                            Long.toString(TEST_LONG), pathSegments.get(3));
        assertEquals("Error: Container type location Uri doesn't match our expected result",
                            containerWithTypeAndLocationUri.toString(),
                            "content://com.ricardogarfe.rsu.app/container/pilas/222222/-333333");
    }
}
