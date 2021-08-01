package app;

import command.CommandListener;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.discordjson.json.ApplicationInfoData;
import reactor.core.publisher.Mono;
import support.AddRandomReaction;
import support.Commands;
import support.VoiceSupport;

import static support.Commands.isAuthor;

public class Main {

    public static void main(String[] args) {
        GatewayDiscordClient client = DiscordClient.create(System.getenv("token"))
                .login()
                .block();

        Mono<Long> ownerId = client.rest().getApplicationInfo()
                .map(ApplicationInfoData::owner)
                .map(user -> Snowflake.asLong(user.id()))
                .cache();

        CommandListener listener = CommandListener.createWithPrefix("!!")
                .filter(req -> isAuthor(ownerId, req))
                .on("echo", Commands::echo)
                .on("exit", (req, res) -> req.getClient().logout())
                .on("status", Commands::status)
                .on("changeAvatar", Commands::changeAvatar)
                .on("react", new AddRandomReaction())
                .on("userinfo", Commands::userInfo);

        Mono.when(client.on(listener), VoiceSupport.create(client).eventHandlers()).block();
    }
}