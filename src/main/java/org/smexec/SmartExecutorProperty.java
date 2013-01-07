package org.smexec;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Generic interface to represent a property value so Hystrix can consume properties without being tied to any
 * particular backing implementation.
 * 
 * @param <T> Type of property value
 */
public interface SmartExecutorProperty<T> {

    public T get();

    /**
     * Helper methods for wrapping static values and dynamic Archaius (https://github.com/Netflix/archaius)
     * properties in the {@link SmartExecutorProperty} interface.
     */
    public static class Factory {

        public static <T> SmartExecutorProperty<T> asProperty(final T value) {
            return new SmartExecutorProperty<T>() {

                @Override
                public T get() {
                    return value;
                }

            };
        }

        /**
         * When retrieved this will return the value from the given {@link SmartExecutorProperty} or if that
         * returns null then return the <code>defaultValue</code>.
         * 
         * @param value {@link SmartExecutorProperty} of property value that can return null (meaning no
         *            value)
         * @param defaultValue value to be returned if value returns null
         * @return value or defaultValue if value returns null
         */
        public static <T> SmartExecutorProperty<T> asProperty(final SmartExecutorProperty<T> value, final T defaultValue) {
            return new SmartExecutorProperty<T>() {

                @Override
                public T get() {
                    T v = value.get();
                    if (v == null) {
                        return defaultValue;
                    } else {
                        return v;
                    }
                }

            };
        }

        /**
         * When retrieved this will iterate over the contained {@link SmartExecutorProperty} instances until a
         * non-null value is found and return that.
         * 
         * @param values
         * @return first non-null value or null if none found
         */
        public static <T> SmartExecutorProperty<T> asProperty(final SmartExecutorProperty<T>... values) {
            return new SmartExecutorProperty<T>() {

                @Override
                public T get() {
                    for (SmartExecutorProperty<T> v : values) {
                        // return the first one that doesn't return null
                        if (v.get() != null) {
                            return v.get();
                        }
                    }
                    return null;
                }

            };
        }

        public static <T> SmartExecutorProperty<T> nullProperty() {
            return new SmartExecutorProperty<T>() {

                @Override
                public T get() {
                    return null;
                }

            };
        }

    }

    public static class UnitTest {

        @Test
        public void testNested1() {
            SmartExecutorProperty<String> a = Factory.asProperty("a");
            assertEquals("a", a.get());

            SmartExecutorProperty<String> aWithDefault = Factory.asProperty(a, "b");
            assertEquals("a", aWithDefault.get());
        }

        @Test
        public void testNested2() {
            SmartExecutorProperty<String> nullValue = Factory.nullProperty();

            SmartExecutorProperty<String> withDefault = Factory.asProperty(nullValue, "b");
            assertEquals("b", withDefault.get());
        }

        @Test
        public void testNested3() {
            SmartExecutorProperty<String> nullValue = Factory.nullProperty();
            SmartExecutorProperty<String> a = Factory.asProperty(nullValue, "a");

            SmartExecutorProperty<String> withDefault = Factory.asProperty(a, "b");
            assertEquals("a", withDefault.get());
        }

        @Test
        public void testNested4() {
            SmartExecutorProperty<String> nullValue = Factory.nullProperty();
            SmartExecutorProperty<String> a = Factory.asProperty(nullValue, null);

            SmartExecutorProperty<String> withDefault = Factory.asProperty(a, "b");
            assertEquals("b", withDefault.get());
        }

        @Test
        public void testNested5() {
            SmartExecutorProperty<String> nullValue = Factory.nullProperty();
            SmartExecutorProperty<String> a = Factory.asProperty(nullValue, null);

            @SuppressWarnings("unchecked")
            SmartExecutorProperty<String> withDefault = Factory.asProperty(a, Factory.asProperty("b"));
            assertEquals("b", withDefault.get());
        }

        @Test
        public void testSeries1() {
            SmartExecutorProperty<String> nullValue = Factory.nullProperty();
            SmartExecutorProperty<String> a = Factory.asProperty(nullValue, null);

            @SuppressWarnings("unchecked")
            SmartExecutorProperty<String> withDefault = Factory.asProperty(a, nullValue, nullValue, Factory.asProperty("b"));
            assertEquals("b", withDefault.get());
        }

        @Test
        public void testSeries2() {
            SmartExecutorProperty<String> nullValue = Factory.nullProperty();
            SmartExecutorProperty<String> a = Factory.asProperty(nullValue, null);

            @SuppressWarnings("unchecked")
            SmartExecutorProperty<String> withDefault = Factory.asProperty(a, nullValue, Factory.asProperty("b"), nullValue, Factory.asProperty("c"));
            assertEquals("b", withDefault.get());
        }

    }
}
