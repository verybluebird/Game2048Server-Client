package controller;

import model.Player;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController {

    public ServerController() {

    }

    public void begin_server() {
        Socket clientSocket; //сокет для общения
        ServerSocket server; // серверсокет

        Player player_1 = new Player("Host");
        Player player_2 = new Player("Guest");

        try {
            server = new ServerSocket(7777);
            System.out.println("Server is running!");
            System.out.println("Waiting fo another player...");
            clientSocket = server.accept();

            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            System.out.println("DataOutputStream  created");
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            System.out.println("DataInputStream created");

            out.writeUTF("Connection...");

            while (!clientSocket.isClosed()) {
                System.out.println("Server reading from channel");

// сервер ждёт в канале чтения (inputstream) получения данных клиента
                String entry = in.readUTF();

// после получения данных считывает их
                System.out.println("READ from client message - " + entry);

// и выводит в консоль
                System.out.println("Server try writing to channel");

// инициализация проверки условия продолжения работы с клиентом по этому сокету по кодовому слову       - quit
                if (entry.equalsIgnoreCase("quit")) {
                    System.out.println("Client initialize connections suicide ...");
                    out.writeUTF("Server reply - " + entry + " - OK");
                    out.flush();

                    break;
                }

// если условие окончания работы не верно - продолжаем работу - отправляем эхо-ответ  обратно клиенту
                out.writeUTF("Server reply - " + entry + " - OK");
                System.out.println("Server Wrote message to client.");

// освобождаем буфер сетевых сообщений (по умолчанию сообщение не сразу отправляется в сеть, а сначала накапливается в специальном буфере сообщений, размер которого определяется конкретными настройками в системе, а метод  - flush() отправляет сообщение не дожидаясь наполнения буфера согласно настройкам системы
                out.flush();
                player_1.controller.getView().run();
                player_2.controller.getView().run();


                while (!player_1.controller.getView().isGameEnded && !player_2.controller.getView().isGameEnded) {
                    if (player_1.controller.getView().isGameEnded || player_2.controller.getView().isGameEnded) {

                        if (player_1.get_player_score() > player_2.get_player_score()) {
                            player_1.controller.getView().isGameWon = true;
                            player_2.controller.getView().isGameWon = false;
                            System.out.println(player_1.get_Player_Name() + " Win!");
                            out.writeUTF(player_1.get_Player_Name() + " Win!");
                            break;
                        }
                        if (player_1.get_player_score() < player_2.get_player_score()) {
                            player_1.controller.getView().isGameWon = false;
                            player_2.controller.getView().isGameWon = true;
                            System.out.println(player_2.get_Player_Name() + " Win!");
                            out.writeUTF(player_2.get_Player_Name() + " Win!");
                            break;
                        }
                        if (player_1.get_player_score() == player_2.get_player_score()) {
                            System.out.println("Tie");
                            out.writeUTF("Tie");
                            break;
                        }
                    }
                }
            }

            in.close();
            out.close();
            System.out.println("In and out stream is closed");
            server.close();
            clientSocket.close();
            System.out.println("Closing connections & channels - DONE.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void begin_client() {

        try (Socket socket = new Socket("localhost", 7777);
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
             DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
             DataInputStream ois = new DataInputStream(socket.getInputStream());) {

            System.out.println("Client connected to socket.");
            System.out.println();
            System.out.println("Client writing channel = oos & reading channel = ois initialized.");

// проверяем живой ли канал и работаем если живой
            while (!socket.isOutputShutdown()) {
                System.out.println("Write 'start' to start the game");
// ждём консоли клиента на предмет появления в ней данных
                if (br.ready()) {

// данные появились - работаем
                    System.out.println("Client start writing in channel...");

                    String clientCommand = br.readLine();

// пишем данные с консоли в канал сокета для сервера
                    oos.writeUTF(clientCommand);
                    oos.flush();
                    System.out.println("Client sent message " + clientCommand + " to server.");

// ждём чтобы сервер успел прочесть сообщение из сокета и ответить

// проверяем условие выхода из соединения
                    if (clientCommand.equalsIgnoreCase("quit")) {

// если условие выхода достигнуто разъединяемся
                        System.out.println("Client kill connections");


// смотрим что нам ответил сервер на последок перед закрытием ресурсов
                        if (ois.read() > -1) {
                            System.out.println("reading...");
                            String in = ois.readUTF();
                            System.out.println(in);
                        }

// после предварительных приготовлений выходим из цикла записи чтения
                        break;
                    }

// если условие разъединения не достигнуто продолжаем работу
                    System.out.println("Client sent message & start waiting for data from server...");


// проверяем, что нам ответит сервер на сообщение(за предоставленное ему время в паузе он должен был успеть ответить)
                    if (ois.read() > -1) {

// если успел забираем ответ из канала сервера в сокете и сохраняем её в ois переменную,  печатаем на свою клиентскую консоль
                        System.out.println("reading...");
                        String in = ois.readUTF();
                        System.out.println(in);
                    }
                }
            }
// на выходе из цикла общения закрываем свои ресурсы
            System.out.println("Closing connections & channels on clentSide - DONE.");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

