package com.threeH.MyExhibition.tools;

public final class ByteUtil {
    private static final char[] HEX = new char[]{'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final char[] ASCII = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P'};

    private static final byte[] BYTE = new byte[]{
            0, 1, 2, 3, 4, 5, 6, 7,
            8, 9, 0, 0, 0, 0, 0, 0,
            0, 10, 11, 12, 13, 14, 15
    };

    public static String byte2hex(byte[] bs) {
        char[] cs = new char[bs.length * 2];
        for (int i = 0, n = bs.length; i < n; i++) {
            cs[i * 2] = HEX[bs[i] >>> 4 & 0x0F];
            cs[i * 2 + 1] = HEX[bs[i] & 0x0F];
        }
        return new String(cs);
    }

    public static String byte2ascii(byte[] bs) {
        char[] cs = new char[bs.length * 2];
        for (int i = 0, n = bs.length; i < n; i++) {
            cs[i * 2] = ASCII[bs[i] >>> 4 & 0x0F];
            cs[i * 2 + 1] = ASCII[bs[i] & 0x0F];
        }
        return new String(cs);
    }

    public static byte[] ascii2byte(String s) {
        try {
            char[] cs = s.toUpperCase().toCharArray();
            byte[] bs = new byte[cs.length / 2];
            for (int i = 0, n = bs.length; i < n; i++) {
                bs[i] = (byte) (((cs[i * 2] - 'A') << 4) | (cs[i * 2 + 1] - 'A'));
            }
            return bs;
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] hex2byte(String s) {
        try {
            char[] cs = s.toUpperCase().toCharArray();
            byte[] bs = new byte[cs.length / 2];
            for (int i = 0, n = bs.length; i < n; i++) {
                bs[i] = (byte) ((BYTE[cs[i * 2] - '0'] << 4) | BYTE[cs[i * 2 + 1] - '0']);
            }
            return bs;
        } catch (Exception e) {
            return null;
        }
    }

}
