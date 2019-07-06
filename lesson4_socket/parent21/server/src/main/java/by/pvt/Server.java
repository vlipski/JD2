package by.pvt;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static Logger log = Logger.getLogger("by.pvt.Server");
    private static List<Socket> socketList = new ArrayList<>();

    public static void main(String[] args) {


        try {
            ServerSocket serverSocket = new ServerSocket(3036);
            ExecutorService service = Executors.newCachedThreadPool();
            do {
                Socket socket = serverSocket.accept();  //прослушиваем сокет
                socketList.add(socket);   //добавляем новый сокет в список
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                service.submit(() -> {
                    try {
                        String input;
                        while (true) {
                            input = dataInputStream.readUTF();
                            System.out.println("Server input: " + input);
                            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                            if ("END".equals(input)) {
                                dataOutputStream.writeUTF(input);
                                dataOutputStream.flush();
                                socketList.remove(socket);
                                break;
                            }
                            for (Socket socket1 : socketList) {  //отправляем сообщения всем клиентам кроме того чье сообщение
                                if (socket != socket1) {
                                    dataOutputStream = new DataOutputStream(socket1.getOutputStream());
                                    dataOutputStream.writeUTF(input);
                                    dataOutputStream.flush();
                                }
                            }
                        }

                    } catch (IOException e) {
                        log.log(Level.SEVERE, e.getMessage(), e);
                    }
                });
            } while (true);
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }

    }
}
