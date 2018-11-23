

import java.net.Socket;

/**
 *
 * @author Mutaz Barika
 */

public class ClientMessInfo {
  Socket sc;
  String senderName;
  String message;
	public ClientMessInfo(Socket s, String senderName, String message) {
		super();
		this.sc = s;
		//this.name = name;
		this.message = message;
        this.senderName=senderName;
	}

    public String getMessage() {
        return message;
    }

    public String getSenderName() {
        return senderName;
    }

    public Socket getSc() {
        return sc;
    }

}
