service("mongo:latest") {
    name = "mongodb"
    tty = true

    env["DB_USER"] = "test"

    volumes {
        +"/data"
        +""
    }

    events {
        onStart {
            println("Starting container...")
        }
        onRestart {

        }
    }
}

volume("data") {

}
