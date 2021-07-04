package com.yczuoxin.others.api.jdk.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class MyServer {

    private int size = 1024;
    private ServerSocketChannel serverSocketChannel;
    private ByteBuffer byteBuffer;
    private Selector selector;
    private int remoteClientNum = 0;

    public MyServer(int port) {
        try {
            init(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init(int port) throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);

        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        byteBuffer = ByteBuffer.allocate(size);
    }

    private void listener() throws IOException {
        while (true) {
            // 表示有多少个 channel 就绪
            int n = selector.select();
            if (n == 0) {
                System.out.println("n == 0");
                continue;
            }
            // 每个 selector 对应多个 SelectionKey，每个 SelectionKey 对应一个 Channel
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 客户端处于就绪状态，则开始接收请求
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel channel = server.accept();
                    registerChannel(selector, channel, SelectionKey.OP_READ);
                    remoteClientNum++;
                    System.out.println("online client number = " + remoteClientNum);
                    write(channel, "hello client".getBytes(StandardCharsets.UTF_8));
                }
                if (key.isReadable()) {
                    read(key);
                }
                iterator.remove();
            }
        }
    }

    private void write(SocketChannel channel, byte[] bytes) throws IOException {
        byteBuffer.clear();
        byteBuffer.put(bytes);
        // 将 byteBuffer 转为写模式
        byteBuffer.flip();
        channel.write(byteBuffer);
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        int count;
        byteBuffer.clear();
        // 从通道读缓冲区
        while ((count = channel.read(byteBuffer)) > 0) {
            System.out.println(count);
            // byteBuffer 转为读模式
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                System.out.println((char) byteBuffer.get());
            }
             byteBuffer.clear();
        }
        if (count < 0) {
            channel.close();
        }
    }

    private void registerChannel(Selector selector, SocketChannel channel, int opRead) throws IOException {
        if (channel == null) {
            return;
        }
        channel.configureBlocking(false);
        channel.register(selector, opRead);
    }

    public static void main(String[] args) {
        MyServer server = new MyServer(8888);
        try {
            server.listener();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
