package dev.wvr.moli.runtime;

import java.util.Random;

public final class MathLib {
    private static final Random RANDOM = new Random();

    private MathLib() {}

    public static float abs(float v) {
        return Math.abs(v);
    }

    public static float sin(float v) {
        return (float) Math.sin(Math.toRadians(v));
    }

    public static float cos(float v) {
        return (float) Math.cos(Math.toRadians(v));
    }

    public static float sqrt(float v) {
        return (float) Math.sqrt(v);
    }

    public static float floor(float v) {
        return (float) Math.floor(v);
    }

    public static float ceil(float v) {
        return (float) Math.ceil(v);
    }

    public static float round(float v) {
        return Math.round(v);
    }

    public static float trunc(float v) {
        return (int) v;
    }

    public static float clamp(float v, float min, float max) {
        return Math.max(min, Math.min(max, v));
    }

    public static float lerp(float a, float b, float t) {
        return a + t * (b - a);
    }

    public static float min(float a, float b) {
        return Math.min(a, b);
    }

    public static float max(float a, float b) {
        return Math.max(a, b);
    }

    public static float pow(float a, float b) {
        return (float) Math.pow(a, b);
    }

    public static float random(float min, float max) {
        return min + RANDOM.nextFloat() * (max - min);
    }

    public static float hermite_blend(float t) {
        return 3 * t * t - 2 * t * t * t;
    }

    public static float die_roll(float num, float low, float high) {
        float sum = 0;
        for (int i = 0; i < num; i++) {
            sum += random(low, high);
        }
        return sum;
    }

    public static float die_roll_integer(float num, float low, float high) {
        int sum = 0;
        for (int i = 0; i < num; i++) {
            sum += RANDOM.nextInt((int) high - (int) low + 1) + (int) low;
        }
        return (float) sum;
    }

    public static float mod(float a, float b) {
        return a % b;
    }
}