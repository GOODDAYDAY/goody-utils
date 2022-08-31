package com.goody.utils.longjing;

import akka.actor.typed.ActorRef;
import com.goody.utils.longjing.akka.command.TaskCommand;
import com.goody.utils.longjing.akka.command.TaskStartCommand;
import com.goody.utils.longjing.akka.command.TaskStopCommand;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.TimeUnit;

@ComponentScan("com.goody.utils.longjing.akka.system")
@SpringBootApplication
public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        final ApplicationContext context = SpringApplication.run(Main.class, args);

        final ActorRef<TaskCommand> taskActor = (ActorRef<TaskCommand>) context.getBean("taskActor");
        final ActorRef<TaskCommand> taskBActor = (ActorRef<TaskCommand>) context.getBean("taskBActor");

        while (true) {
            taskActor.tell(new TaskStartCommand("taskActorAAA"));
            taskBActor.tell(new TaskStopCommand("taskBActorBBB"));
            TimeUnit.SECONDS.sleep(1);
        }
    }
}