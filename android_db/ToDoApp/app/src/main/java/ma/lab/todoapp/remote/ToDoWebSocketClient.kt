package ma.lab.todoapp.remote
import android.util.Log

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI


abstract class ToDoWebSocketClient(address: String): WebSocketClient(URI(address)){

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
    }

    override fun onError(ex: Exception?) {
    }
}