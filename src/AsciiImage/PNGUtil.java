package AsciiImage;

public class PNGUtil {

    public long computeCRC32(byte[] data) {
        final long polynomial = 0x104C11DB7L; // 32 bit crc generator polynomial (divisor)
        long crc = 0;
        crc ^= polynomial;
        System.out.println(crc);
        return crc;
    }

    public int combine2Bytes(byte a, byte b) {
        return (a & 0xff) << 8 | (b & 0xff);
    }
    
}
