package com.turbo.util;

import org.apache.commons.lang3.ObjectUtils;

import java.util.function.BiFunction;

public abstract class CommonUtils {

    /**
     * Invoke mergeFunction if both elements not null
     * Or return first not null
     * @param first
     * @param second
     * @param mergeFunc
     * @param <T>
     * @return
     */
    public static <T> T mergeObjects(T first, T second, BiFunction<T,T,T> mergeFunc) {
        if(ObjectUtils.allNotNull(first, second)) {
            return mergeFunc.apply(first, second);
        }
        return ObjectUtils.firstNonNull(first, second);
    }
}
