import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ConsoleServer {
    private static final int PORT = 8090;
    private static final String END_MESSAGE = "/end";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            Thread clientThread = new Thread(() -> {
                String str;
                while (true) {
                    try {
                        str = in.readUTF();

                        if (str.equals(END_MESSAGE)) {
                            System.exit(0);
                        }

                        System.out.println("Received: " + str);
                        out.writeUTF("Echo: " + str);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(0);
                    }
                }
            });
            clientThread.setDaemon(true);
            clientThread.start();

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();
                if (message.equals(END_MESSAGE)) {
                    out.writeUTF(END_MESSAGE);
                    System.out.println("Closing server...");
                    break;
                }
                out.writeUTF("Server: " + message);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}