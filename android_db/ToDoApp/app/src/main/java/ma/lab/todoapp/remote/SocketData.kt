package ma.lab.todoapp.remote



data class Payload(val item: Any)

data class SocketData(val event: String, val payload: Payload)