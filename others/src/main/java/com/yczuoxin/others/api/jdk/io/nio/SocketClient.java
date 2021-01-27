package com.yczuoxin.others.api.jdk.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Set;

public class SocketClient {

    public static void main(String[] args) throws IOException {
        // 获取通道
        InetSocketAddress address = new InetSocketAddress("localhost", 8080);
        SocketChannel client = SocketChannel.open(address);
        // 切换为非阻塞模式
        client.configureBlocking(false);
        // 获取选择器
        Selector selector = Selector.open();
        // 注册通道和读取服务端返回的数据
        client.register(selector, SelectionKey.OP_READ);
        // 读取一张图片到客户端
        FileChannel fileChannel = FileChannel.open(Paths.get("C:\\Users\\Administrator\\Pictures\\Saved Pictures\\timg.jpg"), StandardOpenOption.READ);

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (fileChannel.read(buffer) != -1) {
            buffer.flip();
            client.write(buffer);
            buffer.clear();
        }

        while (true) {
            int selectNum = selector.select();
            System.out.println(selectNum);

            if (selectNum > 0) {
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isReadable()) {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        int read;
                        while ((read = channel.read(byteBuffer)) != -1) {
                            byteBuffer.flip();
                            System.out.println(new String(byteBuffer.array(), 0, read));
                        }
                    }
                    iterator.remove();
                }
            }
        }
    }

}
