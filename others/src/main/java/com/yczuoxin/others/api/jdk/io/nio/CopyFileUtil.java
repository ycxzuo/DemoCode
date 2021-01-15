package com.yczuoxin.others.api.jdk.io.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class CopyFileUtil {

    public static void copyFile(String source, String destination) throws IOException {
        try (FileInputStream is = new FileInputStream(source);
             FileOutputStream os = new FileOutputStream(destination);
             FileChannel isChannel = is.getChannel();
             FileChannel osChannel = os.getChannel()) {

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while (true) {
                int offset = isChannel.read(byteBuffer);
                if (offset == -1) {
                    break;
                }
                byteBuffer.flip();
                osChannel.write(byteBuffer);
                byteBuffer.clear();
            }
        }

    }

}
