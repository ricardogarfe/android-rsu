package com.ricardogarfe.rsu.app;

import android.content.Context;

public class Utility {

    private static final String DUMMY_LAT_LONG = "39471791/-382460";

    public static final String CONTAINER = "container";
    public static final String BATTERIES = "pilas";
    public static final String OIL = "aceite";
    public static final String CLOTHES = "ropa";
    public static final String CARDBOARD = "carton";
    public static final String GLASS = "vidrio";
    public static final String BOTTLING = "envases";
    public static final String WASTE = "residuos";

    public static String getPreferredLocation(Context context) {
        return DUMMY_LAT_LONG ;
    }
}
