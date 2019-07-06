package by.pvt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    private static Logger log = Logger.getLogger("by.pvt.Client");

    public static void main(String[] args) {


        try (Socket socket = new Socket("localhost", 3036)) {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            ExecutorService service = Executors.newCachedThreadPool();//нить чтения сообщений с сервера
            service.submit(() -> {
                String str;
                try {
                    while (true) {
                        str = dataInputStream.readUTF(); // ждем сообщения с сервера
                        if ("END".equals(str)) {
                            break;
                        }
                        System.out.println(str); // пишем сообщение с сервера на консоль
                    }
                } catch (IOException e) {
                    log.log(Level.SEVERE, e.getMessage(), e);
                } finally {
                    try {
                        dataInputStream.close();
                        dataOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            service.shutdown();

            System.out.println("Напишите свое имя: ");
            Scanner scannerName = new Scanner(System.in, "Cp866");
            String name = scannerName.nextLine();
            Date time = new Date();
            SimpleDateFormat dt1 = new SimpleDateFormat("HH:mm:ss");
            String dtime = dt1.format(time);
            dataOutputStream.writeUTF("\t(" + dtime + ") к чату присоединился  " + name);
            dataOutputStream.flush();

            Scanner scanner = new Scanner(System.in, "Cp866");  // нить отправки сообщений из консоли на сервер
            String command;
            while (true) {
                command = scanner.nextLine();
                if ("END".equals(command)) {
                    dataOutputStream.writeUTF(command);
                    break;
                }
                time = new Date();
                dtime = dt1.format(time);
                dataOutputStream.writeUTF("\t(" + dtime + ") " + name + " говорит:  " + command);
                dataOutputStream.flush();
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }

    }
}
