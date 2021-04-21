package interfaces;

import java.io.IOException;
import models.ServerMessage.Message;

public interface Sender {
    public void send(Message message) throws IOException;
}
