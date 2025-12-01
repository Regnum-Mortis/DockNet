package com.example.docknet.util;

/**
 * Small utility class providing distance calculations used across the app.
 * Kept as a separate file so the original 6-argument distance function is
 * available for future reuse without cluttering parser code.
 */
@SuppressWarnings("unused")
public final class DistanceUtils {
    private DistanceUtils() { /* utility */ }

    /**
     * Calculate Euclidean distance between two 3D points: (x1,y1,z1) and (x2,y2,z2).
     * Returns 0.0 when points are identical.
     */
    public static double calculateDistance(double x1, double y1, double z1,
                                           double x2, double y2, double z2) {
        if (x1 == x2 && y1 == y2 && z1 == z2) return 0.0;
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dz = z2 - z1;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
