package com.example.itemservice.util;

public class GeoUtils {
    public static double KM_IN_DEGREE_LAT = 111.0;

    public static double[] calculateBoundingBox(double lat, double lon, double radiusKm) {
        double deltaLat = radiusKm / KM_IN_DEGREE_LAT;
        double deltaLon = radiusKm / (KM_IN_DEGREE_LAT * Math.cos(Math.toRadians(lat)));

        double minLat = lat - deltaLat;
        double maxLat = lat + deltaLat;
        double minLon = lon - deltaLon;
        double maxLon = lon + deltaLon;

        return new double[]{minLat, maxLat, minLon, maxLon};
    }
}

