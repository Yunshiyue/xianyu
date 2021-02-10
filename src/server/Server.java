package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
    static final int PORT = 9999;

    public static void main(String[] args) throws IOException {
        try (ServerSocket s = new ServerSocket(PORT)) {
            System.out.println("Server Started");
            while (true) {
                Socket socket = s.accept();
                try {
                    new ServerOneJabber(socket);
                } catch (IOException e) {
                    System.out.println("12");
                }
            }
        }
    }
}
