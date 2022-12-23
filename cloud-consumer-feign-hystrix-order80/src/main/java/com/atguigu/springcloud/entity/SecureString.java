package com.atguigu.springcloud.entity;

import java.security.SecureRandom;
import java.util.Arrays;

public class SecureString implements CharSequence {
    private final int[] chars;
    private final int[] pad;

    public SecureString(final CharSequence original) {
        this(0, original.length(), original);
    }

    public SecureString(final int start, final int end, final CharSequence original) {
        final int length = end - start;
        pad = new int[length];
        chars = new int[length];
        scramble(start, length, original);
    }

    @Override
    public char charAt(final int i) {
        return (char) (pad[i] ^ chars[i]);
    }

    @Override
    public int length() {
        return chars.length;
    }

    @Override
    public CharSequence subSequence(final int start, final int end) {
        return new SecureString(start, end, this);
    }

    /**
     * Convert array back to String but not using toString(). See toString() docs
     * below.
     */
    public String asString() {
        final char[] value = new char[chars.length];
        for (int i = 0; i < value.length; i++) {
            value[i] = charAt(i);
        }
        return new String(value);
    }

    /**
     * Manually clear the underlying array holding the characters
     */
    public void clear() {
        Arrays.fill(chars, '0');
        Arrays.fill(pad, 0);
    }

    /**
     * Protect against using this class in log statements.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Secure:XXXXX";
    }

    /**
     * Called by garbage collector.
     * <p>
     * {@inheritDoc}
     */
    @Override
    protected void finalize() throws Throwable {
        clear();
        super.finalize();
    }

    /**
     * Randomly pad the characters to not store the real character in memory.
     *
     * @param start start of the {@code CharSequence}
     * @param length length of the {@code CharSequence}
     * @param characters the {@code CharSequence} to scramble
     */
    private void scramble(final int start, final int length, final CharSequence
            characters) {
        final SecureRandom random = new SecureRandom();
        for (int i = start; i < length; i++) {
            final char charAt = characters.charAt(i);
            pad[i] = random.nextInt();
            chars[i] = pad[i] ^ charAt;
            //1 xor 1 = 0
            //0 xor 0 = 0
            //1 xor 0 = 1

            //1 and 0 = 0
            //1 or 0 = 1
        }
    }

    /*
-XX:+UseG1GC -XX:+PrintGCDetails -Xms1024m -Xmx1024m -XX:MinHeapFreeRatio=30 -XX:MaxHeapFreeRatio=60 -XX:GCTimeRatio=12 -XX:MaxGCPauseMillis=150 -XX:NewRatio=3
-XX:+UseGCLogFileRotation
-XX:NumberOfGCLogFiles=10
-XX:GCLogFileSize=50M
-Xloggc:/home/user/log/gc.log
*/


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecureString that = (SecureString) o;
        if (that.chars.length != chars.length || that.pad.length != pad.length) return false;
        int i = chars.length;
        while (--i >= 0) {
            if ((chars[i] ^ pad[i]) != (that.chars[i] ^ that.pad[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final char[] value = new char[chars.length];
        int h, i = value.length;
        while (--i >= 0) {
            value[i] = charAt(i);
        }
        return (h = Arrays.hashCode(value)) ^ (h >>> 16);
    }


}