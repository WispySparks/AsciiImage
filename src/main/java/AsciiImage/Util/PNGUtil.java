package main.java.AsciiImage.Util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.InflaterInputStream;

public class PNGUtil {

    /**
     * Converts a signed byte to an unsigned byte
     * @param a byte to convert
     * @return an unsigned byte (int)
     */
    public int toUInt8(byte a) {
        return a & 0xff;
    }

    /**
     * Converts 2 signed bytes to a 16 bit unsigned int
     * @param a MSB
     * @param b LSB
     * @return a 16 bit unsigned int
     */
    public int toUInt16(byte a, byte b) {
        return toUInt8(a) << 8 | toUInt8(b);
    }

    /**
     * Converts 3 signed bytes to a 24 bit unsigned int
     * @param a MSB
     * @param b 2nd byte
     * @param c LSB
     * @return a 24 bit unsigned int
     */
    public int toUInt24(byte a, byte b, byte c) {
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
    public int toUInt32(byte a, byte b, byte c, byte d) { // Tested, seems to work
        return toUInt24(a, b, c) << 8 | toUInt8(d);
    }

    public byte[] decompress(byte[] data) {
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        InflaterInputStream inflate = new InflaterInputStream(stream);
        byte[] stuff = {};
        try {
            stuff = inflate.readAllBytes();
            inflate.close();
        } catch (IOException e) {e.printStackTrace();}
        return stuff;
    }

    public long computeCRC32(byte[] data) {
        final long polynomial = 0x104C11DB7L; // 32 bit crc generator polynomial (divisor)
        long crc = 0;
        crc ^= polynomial;
        System.out.println(crc);
        return crc;
    }
    
}
