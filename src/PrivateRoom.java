

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;

/**
 *
 * @author Mutaz Barika
 */

public class PrivateRoom{

    String creatorName;
    int passcode;
     int maxMem;
    LinkedList<Socket> memeberSockets;
    LinkedList<ClientMessInfo> privateMessagQueue;

public PrivateRoom(String creatorName,int passcode, Socket creator, int maxmem)
{
    this.creatorName=creatorName;
    memeberSockets= new LinkedList<Socket>();
    //clientSockets.add(creator);
    this.passcode=passcode;
    this.maxMem=maxmem;
    privateMessagQueue=new LinkedList<ClientMessInfo>();
    memeberSockets.add(creator);
}

    public synchronized String getCreatorName() {
        return creatorName;
    }

    public synchronized int getPasscode() {
        return passcode;
    }

    public synchronized LinkedList<Socket> getMemeberSockets() {
        return memeberSockets;
    }
    
    public synchronized void addMemeberSockets(Socket memeber) {
        memeberSockets.add(memeber);
    }

    public synchronized  void removeMemeberSockets(Socket memeber) {
        memeberSockets.remove(memeber);
    }

    public synchronized  void addPrivateMessag(ClientMessInfo msg) {
        privateMessagQueue.add(msg);
    }

    public synchronized  ClientMessInfo removePrivateMessag() {
        return privateMessagQueue.removeFirst();
    }

    public synchronized  int getPrivateRoomLimt() {
        return maxMem;
    }
}

