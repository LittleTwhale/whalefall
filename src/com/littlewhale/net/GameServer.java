package com.littlewhale.net;

import java.io.*;
import java.net.*;

// 服务器端
public class GameServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    // 启动主机服务，监听端口
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept(); // 等待连接
        // 先输出流再输入流
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());
    }

    // 发送状态
    public void sendFullState(GameState state) throws IOException {
        out.reset();
        out.writeObject(state);
        out.flush();
    }

    // 接收状态
    public WhaleState receiveState() throws IOException, ClassNotFoundException {
        return (WhaleState) in.readObject();
    }
    public GameState receiveFullState() throws IOException, ClassNotFoundException {
        return (GameState) in.readObject();
    }

    // 关闭连接
    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }
}

