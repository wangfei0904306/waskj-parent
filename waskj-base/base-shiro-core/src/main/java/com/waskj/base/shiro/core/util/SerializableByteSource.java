package com.waskj.base.shiro.core.util;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.util.ByteSource;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by poet on 2016/12/20.
 */
public class SerializableByteSource implements Serializable, ByteSource {
    private byte[] bytes;
    private String cachedHex;
    private String cachedBase64;

    public SerializableByteSource() {
    }

    public SerializableByteSource(byte[] bytes) {
        this.bytes = bytes;
    }

    public SerializableByteSource(char[] chars) {
        this.bytes = CodecSupport.toBytes(chars);
    }

    public SerializableByteSource(String string) {
        this.bytes = CodecSupport.toBytes(string);
    }

    public SerializableByteSource(ByteSource source) {
        this.bytes = source.getBytes();
    }

    public SerializableByteSource(File file) {
        this.bytes = (new SerializableByteSource.BytesHelper()).getBytes(file);
    }

    public SerializableByteSource(InputStream stream) {
        this.bytes = (new SerializableByteSource.BytesHelper()).getBytes(stream);
    }

    public static boolean isCompatible(Object o) {
        return o instanceof byte[] || o instanceof char[] || o instanceof String || o instanceof ByteSource || o instanceof File || o instanceof InputStream;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public void setCachedBase64(String cachedBase64) {
        this.cachedBase64 = cachedBase64;
    }

    public void setCachedHex(String cachedHex) {
        this.cachedHex = cachedHex;
    }

    public boolean isEmpty() {
        return this.bytes == null || this.bytes.length == 0;
    }

    public String toHex() {
        if (this.cachedHex == null) {
            this.cachedHex = Hex.encodeToString(this.getBytes());
        }

        return this.cachedHex;
    }

    public String toBase64() {
        if (this.cachedBase64 == null) {
            this.cachedBase64 = Base64.encodeToString(this.getBytes());
        }

        return this.cachedBase64;
    }

    public String toString() {
        return this.toBase64();
    }

    public int hashCode() {
        return this.bytes != null && this.bytes.length != 0 ? Arrays.hashCode(this.bytes) : 0;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof ByteSource) {
            ByteSource bs = (ByteSource) o;
            return Arrays.equals(this.getBytes(), bs.getBytes());
        } else {
            return false;
        }
    }

    private static final class BytesHelper extends CodecSupport {
        private BytesHelper() {
        }

        public byte[] getBytes(File file) {
            return this.toBytes(file);
        }

        public byte[] getBytes(InputStream stream) {
            return this.toBytes(stream);
        }
    }
}
