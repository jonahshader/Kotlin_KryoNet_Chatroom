package jonahshader

import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server

fun main() {
    println("Server or client: ")
    loop@ while(true) {
        when (readLine()?.toLowerCase()) {
            "server" -> {
                runServer()
                break@loop
            }
            "client" -> {
                runClient()
                break@loop
            }
            else -> { }
        }
    }
}

fun runServer() {
    val server = Server()
    server.start();

    val port = promptFor("Enter port: ").toInt();

    server.bind(port, port)

    server.addListener(object : Listener() {
        override fun received(connection: Connection?, `object`: Any?) {
            if (`object` is String) {
                println(`object`)
                for (con in server.connections)
                    con?.sendTCP(`object`)
            }
        }
    })

    while (true) {
        val line = readLine()
        if (line != null) {
            for (con in server.connections)
                con?.sendTCP("Server: $line")
            println("Server: $line")
        }
    }
}

fun runClient() {
    val client = Client()
    client.start()

    val username = promptFor("Enter username: ")
    val ip = promptFor("Enter IP without port: ")
    val port = promptFor("Enter port: ").toInt()

    client.connect(5000, ip, port, port)

    client.sendTCP("User connected")

    client.addListener(object : Listener() {
        override fun received(connection: Connection?, `object`: Any?) {
            if (`object` is String) {
                println(`object` as String?)
            }
        }
    })

    while (true) {
        val line = readLine()
        if (line != null) {
            client.sendTCP("$username: $line")
        }
    }
}

fun promptFor(prompt: String) : String {
    var validInput = false
    var out = ""
    while (!validInput) {
        println(prompt)
        val line = readLine()
        if (line != null) {
            out = line
            validInput = true
        }
    }
    return out
}
