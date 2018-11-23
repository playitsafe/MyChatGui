

import java.net.Socket;

/**
 *
 * @author Mutaz Barika
 */
public class ClientInfo {

    String cName;
    Socket cSocket;

    //constructor
    public ClientInfo(String cName, Socket cSocket) {
        this.cName = cName;
        this.cSocket = cSocket;
    }

    public String getcName() {
        return cName;
    }

    public Socket getcSocket() {
        return cSocket;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public void setcSocket(Socket cSocket) {
        this.cSocket = cSocket;
    }

    

}
