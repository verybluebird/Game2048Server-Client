package controller;

import model.Player;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController {
    Player player_1 = new Player("Host");
    Player player_2 = new Player("Guest");

    public ServerController() {
    }


    static class MyThread extends Thread {
        DataInputStream in;
        public String entry;

        MyThread(Socket clientSocket) throws IOException {
            in = new DataInputStream(clientSocket.getInputStream());
            System.out.println("Thread created");
        }

        @Override
        public void run() {
            entry = read(in);
        }

        void CloseMyThread() throws IOException {
            in.close();
            entry = null;
            System.out.println("in stream is closed");
        }


        public String read(DataInputStream in) {

            try {
                entry = in.readUTF();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return entry;
        }
    }

    public void begin_server() {
        Socket clientSocket; //сокет для общения
        ServerSocket server; // серверсокет


        try {
            server = new ServerSocket(7777);
            System.out.println("Server is running!");
            System.out.println("Waiting fo another player...");
            clientSocket = server.accept();
            MyThread myThread = new MyThread(clientSocket);

            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            System.out.println("DataOutputStream  created");
//            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
//            System.out.println("DataInputStream created");

            out.writeUTF("Connection...");

            while (!clientSocket.isClosed()) {


                while (!player_1.controller.getView().isGameEnded && !player_2.controller.getView().isGameEnded) {
                    System.out.println("Server reading from channel");

// сервер ждёт в канале чтения (inputstream) получения данных клиента
                    myThread.run();

                    String entry = myThread.entry;


// после получения данных считывает их
                    System.out.println("READ from client message - " + entry);

// и выводит в консоль
                    System.out.println("Server is trying to write to channel");

// инициализация проверки условия продолжения работы с клиентом по этому сокету по кодовому слову       - quit
                    if (entry.equalsIgnoreCase("quit")) {
                        System.out.println("Client initialize connections suicide ...");
                        out.writeUTF("Server reply - " + entry + " - OK");
                        out.flush();

                        break;
                    }

// если условие окончания работы не верно - продолжаем работу - отправляем эхо-ответ  обратно клиенту
                    out.writeUTF("Server reply - " + entry + " - OK");
                    System.out.println("Server wrote message to client.");

// освобождаем буфер сетевых сообщений (по умолчанию сообщение не сразу отправляется в сеть, а сначала накапливается в специальном буфере сообщений, размер которого определяется конкретными настройками в системе, а метод  - flush() отправляет сообщение не дожидаясь наполнения буфера согласно настройкам системы
                    out.flush();
//                    player_1.controller.getView().run();
//                    player_2.controller.getView().run();
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

            myThread.CloseMyThread();
            out.close();
            System.out.println("out stream is closed");
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
             DataOutputStream oos = new DataOutputStream(socket.getOutputStream());) {

//                  DataInputStream ois = new DataInputStream(socket.getInputStream());)
            MyThread myThread = new MyThread(socket);

            System.out.println("Client connected to socket.");
            System.out.println();
            System.out.println("Client writing channel = oos & reading channel = ois initialized.");

// проверяем живой ли канал и работаем если живой
            System.out.println("Write 'start' to start the game");
            while (!socket.isOutputShutdown()) {
                while (!player_1.controller.getView().isGameEnded && !player_2.controller.getView().isGameEnded) {
                    myThread.run();

                    String entry = myThread.entry;
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
                            if (myThread.in.read() > -1) {
                                System.out.println("reading...");
                                String in = myThread.in.readUTF();
                                System.out.println(in);
                            }

// после предварительных приготовлений выходим из цикла записи чтения
                            break;
                        }

// если условие разъединения не достигнуто продолжаем работу
                        System.out.println("Client sent message & start waiting for data from server...");


// проверяем, что нам ответит сервер на сообщение(за предоставленное ему время в паузе он должен был успеть ответить)
                        if (myThread.in.read() > -1) {

// если успел забираем ответ из канала сервера в сокете и сохраняем её в ois переменную,  печатаем на свою клиентскую консоль
                            System.out.println("reading...");
                            String in = myThread.in.readUTF();
                            System.out.println(in);
                        }
                    }
                }
            }
// на выходе из цикла общения закрываем свои ресурсы
            myThread.CloseMyThread();
            oos.close();
            System.out.println("out stream is closed");
            socket.close();
            System.out.println("Closing connections & channels - DONE.");


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

