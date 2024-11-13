package com.kkk.test;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author lonelykkk
 * @email 2765314967@qq.com
 * @date 2024/11/12 18:58
 * @Version V1.0
 */
public class SocketClient {
    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1", 1024);
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
            System.out.println("请输入内容：");
            new Thread(()->{
                while (true) {
                    try {
                        Scanner scanner = new Scanner(System.in);
                        String input = scanner.nextLine();
                        printWriter.println(input);
                        printWriter.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        // 确保在 finally 块中关闭资源
                        if (printWriter != null) {
                            printWriter.close();
                        }
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
