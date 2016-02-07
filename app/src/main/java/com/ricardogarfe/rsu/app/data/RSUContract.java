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

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the container database.
 */
public class RSUContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.ricardogarfe.rsu.app";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    public static final String PATH_CONTAINER = "container";
    public static final String PATH_LOCATION = "location";
    public static final String PATH_TYPE = "type";

    /*
        Inner class that defines the table contents of the location table
     */
    public static final class LocationEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;

        public static final String TABLE_NAME = "location";

        // Human readable location string, provided by the API.
        public static final String COLUMN_CITY_NAME = "city_name";

    }

    /* Inner class that defines the table contents of the container type table */
    public static final class TypeEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TYPE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TYPE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TYPE;

        public static final String TABLE_NAME = "type";

        public static final String COLUMN_TYPE_ID = "type_id";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_ICON = "icon";
    }

    /* Inner class that defines the table contents of the container table */
    public static final class ContainerEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONTAINER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTAINER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTAINER;

        public static final String TABLE_NAME = "container";

        public static final String COLUMN_CONTAINER_ID = "container_id";
        // Column with the foreign key into the location table.
        public static final String COLUMN_LOC_KEY = "location_id";
        // Column with the foreign key into the type table.
        public static final String COLUMN_TYPE_KEY = "type_id";
        // Distance stored as long from position to container.
        public static final String COLUMN_DISTANCE = "distance";
        // Title retrieved from API.
        public static final String COLUMN_TITLE = "title";

        // Short description and long description of the direction, as provided by API.
        public static final String COLUMN_MESSAGE = "message";

        // Lat and Long values for retrived container.
        public static final String COLUMN_LAT_DEST = "latDestiny";
        public static final String COLUMN_LONG_DEST = "longDestiny";

    }
}
