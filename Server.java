package com.javarush.task.task30.task3008;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();
    private static class Handler extends Thread {
        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException{
            while (true) {
                //ConsoleHelper.writeMessage("Здравствуйте! Назовите себя: ");
                connection.send(new Message(MessageType.NAME_REQUEST));
                Message message = connection.receive();
                if (message.getType() != MessageType.USER_NAME) {
                    ConsoleHelper.writeMessage("Получено сообщение от " + socket.getRemoteSocketAddress() + ". Тип сообщения не соответствует протоколу.");
                    continue;
                }

                String userName = message.getData();

                if (userName.isEmpty()) {
                    ConsoleHelper.writeMessage("Попытка подключения к серверу с пустым именем от " + socket.getRemoteSocketAddress());
                    continue;
                }

                if (connectionMap.containsKey(userName)) {
                    ConsoleHelper.writeMessage("Попытка подключения к серверу с уже используемым именем от " + socket.getRemoteSocketAddress());
                    continue;
                }
                connectionMap.put(userName, connection);

                connection.send(new Message(MessageType.NAME_ACCEPTED));
                return userName;
            }
        }

        private void notifyUsers(Connection connection, String userName) throws IOException{
            for (Map.Entry<String, Connection> entry: connectionMap.entrySet()) {
                if (!entry.getKey().equals(userName)){
                    connection.send(new Message(MessageType.USER_ADDED, entry.getKey()));
                }
            }
        }
    }

    public static void sendBroadcastMessage(Message message){
        for (Map.Entry<String,Connection> entry: connectionMap.entrySet()) {
            Connection connection = entry.getValue();
            try {
                connection.send(message);
            } catch (IOException e) {
                ConsoleHelper.writeMessage("Ошибка отправки сообщения");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        int port = ConsoleHelper.readInt();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            ConsoleHelper.writeMessage("Сервер запущен");
            while (true) {
                Socket socket = serverSocket.accept();
                Handler handler = new Handler(socket);
                handler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
