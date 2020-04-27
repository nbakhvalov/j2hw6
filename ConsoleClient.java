/*
* author Nikolay Bakhvalov
*/

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ConsoleClient {
    private static final int SERVER_PORT = 8090;
    private static final String END_MESSAGE = "/end";

    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("localhost", SERVER_PORT)) {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            Thread serverThread = new Thread(() -> {
                while (true) {
                    try {
                        String str = in.readUTF();
                        if (str.equals(END_MESSAGE)) {
                            System.out.println("Closing client...");
                            System.exit(0);
                        }
                        System.out.println(str);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(0);
                    }
                }
            });
            serverThread.setDaemon(true);
            serverThread.start();

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Write a message: ");
                String message = scanner.nextLine();
                out.writeUTF(message);
                if (message.equals(END_MESSAGE)) {
                    socket.close();
                    break;
                }
            }
        }
    }
}