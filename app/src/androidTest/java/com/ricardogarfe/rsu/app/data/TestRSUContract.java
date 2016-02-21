package com.ricardogarfe.rsu.app.data;

import android.net.Uri;
import android.test.AndroidTestCase;

import java.util.List;

public class TestRSUContract extends AndroidTestCase {


    public void testBuildContainerWithType() {
        Uri containerWithTypeUri = RSUContract.ContainerEntry.buildContainerWithType(TestUtilities.TEST_TYPE);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildWeatherLocation in " +
                              "ContainerContract.",
                             containerWithTypeUri);
        assertEquals("Error: Container type not properly appended to the end of the Uri",
                            TestUtilities.TEST_TYPE, containerWithTypeUri.getLastPathSegment());
        assertEquals("Error: Container type Uri doesn't match our expected result",
                            containerWithTypeUri.toString(),
                            "content://com.ricardogarfe.rsu.app/container/pilas");
    }

    public void testBuildContainerWithTypeAndLocation() {
        Uri containerWithTypeAndLocationUri = RSUContract.ContainerEntry.buildContainerWithTypeAndLocation(TestUtilities.TEST_TYPE, TestUtilities.TEST_LAT, TestUtilities.TEST_LONG);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildContainerWithTypeAndLocation in " +
                              "ContainerContract.",
                             containerWithTypeAndLocationUri);
        List<String> pathSegments = containerWithTypeAndLocationUri.getPathSegments();
        assertEquals("Error: Container type not properly appended to the end of the Uri",
                            TestUtilities.TEST_TYPE, pathSegments.get(1));
        assertEquals("Error: Container Lat not properly appended to the end of the Uri",
                            Long.toString(TestUtilities.TEST_LAT), pathSegments.get(2));
        assertEquals("Error: Container Long not properly appended to the end of the Uri",
                            Long.toString(TestUtilities.TEST_LONG), pathSegments.get(3));
        assertEquals("Error: Container type location Uri doesn't match our expected result",
                            containerWithTypeAndLocationUri.toString(),
                            "content://com.ricardogarfe.rsu.app/container/pilas/222222/-333333");
    }
}
