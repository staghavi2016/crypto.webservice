package com.resources;

import com.api.IntNumber;
import com.api.StreamDataStats;

import static org.junit.Assert.*;
import org.junit.Test;

public class PushAndRecalculateTest {

    @Test
    public void testPushAndRecalculateSTD() {

        StreamDataStats stats = new StreamDataStats();
        PushAndRecalculate PushAndRecalculateRes = new PushAndRecalculate(stats);

        int[] numbers = new int[] { 4, 7, 6, 9, 1, 10, 100, 3000, 200000, 10000000, -12, 0 };
        float[] standardD = new float[] { 0, 1.5f, 1.24f, 1.80f, 2.72f, 3.02f, 32.95f, 986.16f, 62737.59f, 2993820.2f, 2869526.8f, 2759297.8f };
        for (int i = 0; i < numbers.length; i++) {
            IntNumber  singleNum = new IntNumber();
            singleNum.setNum(numbers[i]);
            assertEquals(standardD[i], PushAndRecalculateRes.PushAndRecalculate(singleNum).getStandardD(), 0.9f);
        }

    }

    @Test
    public void testPushAndRecalculateMean() {

        StreamDataStats stats = new StreamDataStats();
        PushAndRecalculate PushAndRecalculateRes = new PushAndRecalculate(stats);

        int[] numbers = new int[] { 4, 7, 6, 9, 1, 10, 100, 3000, 200000, 10000000, -12, 0 };
        float[] means = new float[] { 4, 5.5f, 5.66f, 6.5f, 5.4f, 6.16f, 19.57f, 392.12f, 22570.77f, 1020313.6f, 927556.75f, 850260.4f };
        for (int i = 0; i < numbers.length; i++) {
            IntNumber  singleNum = new IntNumber();
            singleNum.setNum(numbers[i]);
            assertEquals(means[i], PushAndRecalculateRes.PushAndRecalculate(singleNum).getMean(), 0.09f);
        }
    }
}
