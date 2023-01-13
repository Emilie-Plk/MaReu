package com.emplk.mareutraining.utils;

import static org.junit.Assert.fail;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class TestUtil {

        @NonNull
        public static <T> T getValueForTesting(@NonNull final LiveData<T> liveData) {
            liveData.observeForever(t -> {
            });

            T captured = liveData.getValue();

            if (captured == null) {
                throw new AssertionError("LiveData value is null !");
            }

            return captured;
        }

    public static <T> void observeForTesting(LiveData<T> liveData, OnObservedListener<T> onObservedListener) {
        boolean[] called = {false};

        liveData.observeForever(value -> {
            called[0] = true;
            onObservedListener.onObserved(value);
        });

        if (!called[0]) {
            fail("LiveData was not called");
        }
    }

    public interface OnObservedListener<T> {
        void onObserved(T value);
    }
}
