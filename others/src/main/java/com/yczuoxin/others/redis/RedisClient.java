package com.yczuoxin.others.redis;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 粗略版 redis-cli
 */
public class RedisClient {

    private static OutputStream write;
    private static InputStream read;

    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("47.106.80.100", 6379)) {
            write = socket.getOutputStream();
            read = socket.getInputStream();
            Scanner scan = new Scanner(System.in);

            while (scan.hasNextLine()) {
                String str = scan.nextLine();
                if (StringUtils.isBlank(str)) {
                    continue;
                }
                String command = buildCommand(str);
                System.out.println("发送命令为：\r\n" + command);
                String result = sendCommand(command);
                System.out.println("响应命令为：" + result);
            }
            scan.close();
        }
    }

    private static String sendCommand(String command) throws IOException {
        write.write(command.getBytes());
        byte[] bytes = new byte[1024];
        read.read(bytes);
        return new String(bytes,"UTF-8");
    }

    /**
     * 构造 RESP
     * @param str
     * @return
     */
    private static String buildCommand(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        String[] strs = str.split(" ");
        StringBuilder builder = new StringBuilder();
        // 发送的命令数量
        builder.append("*").append(strs.length).append("\r\n");
        for (String str1 : strs) {
            // 发送的命令长度
            builder.append("$").append(str1.length()).append("\r\n");
            // 发送的命令
            builder.append(str1).append("\r\n");
        }
        return builder.toString();
    }
}
