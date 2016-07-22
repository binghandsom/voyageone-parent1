package com.voyageone.common.spring.serializer;

import org.springframework.util.Assert;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CustomServletOutputStream extends ServletOutputStream {
    private boolean isCustom;
    private final OutputStream targetStream;

    public CustomServletOutputStream(OutputStream targetStream, boolean isCustom) {
        Assert.notNull(targetStream, "Target OutputStream must not be null");
        this.targetStream = targetStream;
        this.isCustom = isCustom;
    }

    public final OutputStream getTargetStream() {
        return this.targetStream;
    }

    public void write(int b) throws IOException {
        this.targetStream.write(b);
    }

    public void flush() throws IOException {
        super.flush();
        this.targetStream.flush();
    }

    public void close() throws IOException {
        super.close();
        this.targetStream.close();
    }

    public boolean isCustom() {
        return isCustom;
    }
}
