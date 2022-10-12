package AsciiImage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.InflaterInputStream;

public class PNGUtil {

    public long computeCRC32(byte[] data) {
        final long polynomial = 0x104C11DB7L; // 32 bit crc generator polynomial (divisor)
        long crc = 0;
        crc ^= polynomial;
        System.out.println(crc);
        return crc;
    }

    public int toUInt8(byte a) {
        return a & 0xff;
    }

    public int toUInt16(byte a, byte b) {
        return toUInt8(a) << 8 | toUInt8(b);
    }

    public int toUInt24(byte a, byte b, byte c) {
        return toUInt16(a, b) << 8 | toUInt8(c);
    }

    public int toUInt32(byte a, byte b, byte c, byte d) {
        return toUInt24(a, b, c) << 8 | toUInt8(d);
    }

    public byte[] decompress(byte[] data) {
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        InflaterInputStream inflate = new InflaterInputStream(stream);
        byte[] stuff = {};
        try {
            stuff = inflate.readAllBytes();
            inflate.close();
        } catch (IOException e) {}
        return stuff;
    }
    
}
