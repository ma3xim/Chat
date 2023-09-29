package com.javarush.task.task30.task3008.client;

import com.javarush.task.task30.task3008.ConsoleHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BotClient extends Client {

    public class BotSocketThread extends SocketThread {
        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            sendTextMessage("Привет чатику. Я бот. Понимаю команды: дата, день, месяц, год, время, час, минуты, секунды.");
            super.clientMainLoop();
        }

        @Override
        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
            if (!message.contains(":")){
                return;
            }
            String[] messageAndName = message.split(": ");
            Date date = new Date();
            SimpleDateFormat simpleDateFormat;
            switch (messageAndName[1].toLowerCase(Locale.ROOT).trim()) {
                case "дата":
                    simpleDateFormat = new SimpleDateFormat("d.MM.yyyy");
                    sendTextMessage("Информация для " + messageAndName[0] + ": " + simpleDateFormat.format(date));
                    break;
                case "день":
                    simpleDateFormat = new SimpleDateFormat("d");
                    sendTextMessage("Информация для " + messageAndName[0] + ": " + simpleDateFormat.format(date));
                    break;
                case "месяц":
                    simpleDateFormat = new SimpleDateFormat("MMMM");
                    sendTextMessage("Информация для " + messageAndName[0] + ": " + simpleDateFormat.format(date));
                    break;
                case "год":
                    simpleDateFormat = new SimpleDateFormat("yyyy");
                    sendTextMessage("Информация для " + messageAndName[0] + ": " + simpleDateFormat.format(date));
                    break;
                case "время":
                    simpleDateFormat = new SimpleDateFormat("H:mm:ss");
                    sendTextMessage("Информация для " + messageAndName[0] + ": " + simpleDateFormat.format(date));
                    break;
                case "час":
                    simpleDateFormat = new SimpleDateFormat("H");
                    sendTextMessage("Информация для " + messageAndName[0] + ": " + simpleDateFormat.format(date));
                    break;
                case "минуты":
                    simpleDateFormat = new SimpleDateFormat("m");
                    sendTextMessage("Информация для " + messageAndName[0] + ": " + simpleDateFormat.format(date));
                    break;
                case "секунды":
                    simpleDateFormat = new SimpleDateFormat("s");
                    sendTextMessage("Информация для " + messageAndName[0] + ": " + simpleDateFormat.format(date));
                    break;
            }

        }
    }

    @Override
    protected SocketThread getSocketThread() {
        return new BotSocketThread();
    }

    @Override
    protected boolean shouldSendTextFromConsole() {
        return false;
    }

    @Override
    protected String getUserName() {
        return "date_bot_" + (int) (Math.random() * 100);
    }

    public static void main(String[] args) {
        BotClient botClient = new BotClient();
        botClient.run();
    }
}
