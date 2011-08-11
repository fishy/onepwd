package com.yhsif.onepwd;

import android.test.ActivityInstrumentationTestCase2;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.yhsif.onepwd.OnePwdTest \
 * com.yhsif.onepwd.tests/android.test.InstrumentationTestRunner
 */
public class OnePwdTest extends ActivityInstrumentationTestCase2<OnePwd> {

    public OnePwdTest() {
        super("com.yhsif.onepwd", OnePwd.class);
    }

}
