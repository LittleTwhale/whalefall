package com.littlewhale.net;

import java.io.*;
import java.net.*;

// 客户端
public class GameClient {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    // 连接服务器
    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    // 发送状态（只发送玩家自己的状态）
    public void sendState(WhaleState state) throws IOException {
        out.reset();
        out.writeObject(state);
        out.flush();
    }
    public void sendFullState(GameState state) throws IOException {
        out.reset();
        out.writeObject(state);
        out.flush();
    }

    // 接收状态
    public GameState receiveFullState() throws IOException, ClassNotFoundException {
        return (GameState) in.readObject();
    }

    // 断开连接
    public void disconnect() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
