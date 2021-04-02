# whale-warden

Whale warden is an easy-to-use Docker container orchestration software. The configuration of whale-warden is done entirely whith Kotlin Script, which opens much more possibilites than a static config file.

Additionally, it keeps your containers up to date - it does NOT do that by "watching out" for any updates in a specific interval, instead it hosts an extremely lightweight HTTP server providing custom webhooks, which you can use with your CI/CD solution (e.g. with Docker Hub).

## State

This project is still in early development!

## Usage

### On the host system

whale-warden itself is available as a Docker image, which can be run in the following way: <br>
`docker run --restart always --publish 9090:9090 bluefireoly/whale-warden:latest`

### Configuration

A `whale-warden.kts` file looks like this
```kotlin
service("itzg/minecraft-server:adopt11") {
    name = "zickzack"
    tty = true

    ports(25565 to 25565)

    mounts(
        "data" to "/data",
        "./mods" to "/mods"
    )

    restart(UNLESS_STOPPED)

    env["DB_HOST"] = "mongodb"

    env(
        "EULA" to true,
        "TYPE" to "FABRIC"
    )

    events {
        onFirstStart {
            // custom logic
        }
    }
}
```
