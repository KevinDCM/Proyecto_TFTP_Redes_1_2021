package Server;

import java.io.IOException;
import java.net.SocketException;

public class main {

    public static void main(String[] args) throws SocketException, IOException, InterruptedException {

        // administrar n-clientes, con un thread que pueda atender por separado a cada usuario

        Server server = new Server();
        server.start();

//        FileReceive fR = new FileReceive(4445);
//        fR.start();

    }

}