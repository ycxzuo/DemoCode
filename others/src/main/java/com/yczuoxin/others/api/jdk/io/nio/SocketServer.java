package com.yczuoxin.others.api.jdk.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Set;

public class SocketServer {

    public static void main(String[] args) throws IOException {
        // 获取 ServerSocket 类型的通道
        ServerSocketChannel server = ServerSocketChannel.open();
        // 切换为非阻塞模式
        server.configureBlocking(false);
        // 绑定端口监听事件
        SocketAddress address = new InetSocketAddress("localhost",8080);
        server.bind(address);
        // 获取选择器
        Selector selector = Selector.open();
        // 注册通道和监听的事件到选择器上
        server.register(selector, SelectionKey.OP_ACCEPT);
        // 轮询获取选择器上已经就绪的事件（selector.select() > 0 就代表有事件已经就绪）
        while (true) {
            int selectNum = selector.select();
            System.out.println(selectNum);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext())  {
                SelectionKey selectedKey = iterator.next();
                // 接收事件已经就绪
                if (selectedKey.isAcceptable()) {
                    // 连接客户端（阻塞）
                    SocketChannel client = server.accept();
                    // 切换为非阻塞模式
                    if (client == null) {
                        continue;
                    }
                    client.configureBlocking(false);
                    // 客户端开启一个读事件的监听到选择器上
                    client.register(selector, SelectionKey.OP_READ);
                    // 读事件就绪
                } else if (selectedKey.isReadable()) {
                    // 获取选择器中读就绪的通道
                    SocketChannel client = (SocketChannel) selectedKey.channel();
                    // 创建字节缓冲区用来接收文件
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    // 文件的通道，用来接收客户端传来的图片
                    FileChannel fileChannel = FileChannel.open(Paths.get("test.jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
                    // 读取客户端传输的数据
                    while (client.read(buffer) > 0) {
                        // 切换缓冲区到写类型
                        buffer.flip();
                        // 写出
                        fileChannel.write(buffer);
                        // 清空读取过的缓冲区
                        buffer.clear();
                    }
                }
             }
         }
    }
}
