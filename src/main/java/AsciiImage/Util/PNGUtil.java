package main.java.AsciiImage.Util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.InflaterInputStream;

public final class PNGUtil {

    /**
     * Converts a signed byte to an unsigned byte
     * @param a byte to convert
     * @return an unsigned byte (int)
     */
    public static int toUInt8(byte a) {
        return a & 0xff;
    }

    /**
     * Converts 2 signed bytes to a 16 bit unsigned int
     * @param a MSB
     * @param b LSB
     * @return a 16 bit unsigned int
     */
    public static int toUInt16(byte a, byte b) {
        return toUInt8(a) << 8 | toUInt8(b);
    }

    /**
     * Converts 3 signed bytes to a 24 bit unsigned int
     * @param a MSB
     * @param b 2nd byte
     * @param c LSB
     * @return a 24 bit unsigned int
     */
    public static int toUInt24(byte a, byte b, byte c) {
        return toUInt16(a, b) << 8 | toUInt8(c);
    }

    /**
     * Converts 4 signed bytes to a 32 bit unsigned int
     * @param a MSB
     * @param b 2nd byte
     * @param c 3rd byte
     * @param d LSB
     * @return a 32 bit unsigned int
     */
    public static int toUInt32(byte a, byte b, byte c, byte d) { // Tested, seems to work
        return toUInt24(a, b, c) << 8 | toUInt8(d);
    }

    public static <T extends Number> byte[] listToByteArray(List<T> list) {
        byte[] arr = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i).byteValue();
        }
        return arr;
    }

    public static byte[] decompress(byte[] data) {
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        InflaterInputStream inflate = new InflaterInputStream(stream);
        byte[] stuff = {};
        if (data.length > 0) {
            try {
                stuff = inflate.readAllBytes();
                inflate.close();
            } catch (IOException e) {e.printStackTrace();}
        }
        return stuff;
    }
    
    // * https://www.w3.org/TR/2003/REC-PNG-20031110/#D-CRCAppendix

    private static long crcTable[] = new long[256]; // Table of CRCs of all 8-bit messages.
    private static boolean madeTable = false;

    public static void makeCRCTable() { // Make the table for a fast CRC. 
        if (madeTable) return;
        long c;
        int n, k;
        for (n = 0; n < 256; n++) {
            c = (long) n;
            for (k = 0; k < 8; k++) {
                if ((c & 1) == 1)
                c = 0xedb88320L ^ (c >> 1);
                else
                c = c >> 1;
            }
            crcTable[n] = c;
        }
        madeTable = true;
    }

    /* Update a running CRC with the bytes buf[0..len-1]--the CRC
      should be initialized to all 1's, and the transmitted value
      is the 1's complement of the final running CRC (see the
      crc() routine below). */
    private static long updateCRC(long crc, byte[] buf, int len) {
        long c = crc;
        int n;
        for (n = 0; n < len; n++) {
            c = crcTable[(int) ((c ^ buf[n]) & 0xff)] ^ (c >> 8);
        }
        return c;
    }

    // Return the CRC of the bytes buf
    public static long crc(byte[] buf) {
        return updateCRC(0xffffffffL, buf, buf.length) ^ 0xffffffffL;
    }

}
