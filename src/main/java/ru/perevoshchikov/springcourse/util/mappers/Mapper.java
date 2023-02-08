package ru.perevoshchikov.springcourse.util.mappers;

import java.util.Collection;

/**
 * An interface used to map respective fields
 * @author Egor Perevoshchikov
 * @param <T> the type of source instance
 * @param <R> the type of destination instance
 */
@FunctionalInterface
public interface Mapper<T, R> {

    /**
     * Implementation must implements this method to map some fields of source instance
     * to respective fields of dest instance.
     * @param dest the destination instance of mapping
     * @param source the source instance of mapping
     */
    void map(R dest, T source);

    /**
     * Implementation must implements this method to map each field of source instance
     * to respective fields of dest instance.
     * @param dest the destination instance of mapping
     * @param source the source instance of mapping
     */
    default void fullMap(R dest, T source) {
        map(dest, source);
    }

    /**
     * Implementation must implements this method to map some fields of source instance
     * to respective fields of each element of Collection.
     * @param dest the Collection of destination instances of mapping
     * @param source the source instance of  mapping
     */
    default void multipleMap(Collection<R> dest, T source) {
        dest.forEach(instance -> map(instance, source));
    }

    /**
     * Implementation must implements this method to map each field of source instance
     * to respective field of each element of Collection.
     * @param dest the Collection of destination instances of mapping
     * @param source the source instance of  mapping
     */
    default void multipleFullMap(Collection<R> dest, T source) {
        dest.forEach(instance -> fullMap(instance, source));
    }
}
