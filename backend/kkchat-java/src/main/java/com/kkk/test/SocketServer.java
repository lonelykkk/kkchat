package com.kkk.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author lonelykkk
 * @email 2765314967@qq.com
 * @date 2024/11/12 17:39
 * @Version V1.0
 */
public class SocketServer {
    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(1024);
            System.out.println("服务已启动，等待客户端连接");
            Socket socket = server.accept();
            String ip = socket.getInetAddress().getHostAddress();
            System.out.println("有客户端连接ip: " + ip + ",端口：" + socket.getPort());

            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String readData = bufferedReader.readLine();
            System.out.println("收到客户端的消息->" + readData);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
