package com.github.jjbcoding.sqlmap.util.internals;

import com.github.jjbcoding.sqlmap.exceptions.BuilderException;

import java.util.Map;

/**
 * Helps with tasks related to locating classes.
 */
public class MapHelper {
    // ----- STATIC

    /**
     * Combines two maps.
     * @param target            The target map
     * @param source            The source map
     * @param errorMessage      The error string
     * @param <T>               The second type parameter of the maps
     */
    public static <T> void combineMaps(Map<Class<?>,T> target, Map<Class<?>,T> source, String errorMessage) {
        for (Map.Entry<Class<?>,T> entry : source.entrySet())
            if (target.put(entry.getKey(), entry.getValue()) != null)
                throw new BuilderException(errorMessage);
    }
}
