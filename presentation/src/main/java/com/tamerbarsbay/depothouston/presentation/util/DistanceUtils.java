package com.tamerbarsbay.depothouston.presentation.util;

public class DistanceUtils {

    public static double calculateDistanceBetweenCoordinates(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(degtoRad(lat1)) * Math.sin(degtoRad(lat2)) + Math.cos(degtoRad(lat1)) * Math.cos(degtoRad(lat2)) * Math.cos(degtoRad(theta));
        dist = Math.acos(dist);
        dist = radtoDeg(dist);
        dist = dist * 60 * 1.1515;
        return dist;
    }

    private static double degtoRad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double radtoDeg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
