package com.goody.utils.longjing;

import akka.actor.typed.ActorRef;
import com.goody.utils.longjing.akka.command.TaskCommand;
import com.goody.utils.longjing.akka.command.TaskStartCommand;
import com.goody.utils.longjing.akka.command.TaskStopCommand;
import com.goody.utils.longjing.akka.command.TaskWorkCommand;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@ComponentScan("com.goody.utils.longjing.akka")
@SpringBootApplication
public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        final ApplicationContext context = SpringApplication.run(Main.class, args);
        doWork(context);
    }

    private static void doWork(ApplicationContext context) throws InterruptedException {
        final ActorRef<TaskCommand> taskActor = (ActorRef<TaskCommand>) context.getBean("taskActor");
        final ActorRef<TaskCommand> taskBActor = (ActorRef<TaskCommand>) context.getBean("taskBActor");

        final Random random = new Random();
        while (true) {

            final int actorChoose = random.nextInt(2);
            final ActorRef<TaskCommand> actor = actorChoose == 0 ?
                taskActor : taskBActor;

            final int commandChoose = random.nextInt(3);
            final String value = UUID.randomUUID().toString();
            final TaskCommand command;
            if (0 == commandChoose) {
                command = new TaskStartCommand(value);
            } else if (1 == commandChoose) {
                command = new TaskWorkCommand(value);
            } else {
                command = new TaskStopCommand(value);
            }
            actor.tell(command);
            TimeUnit.SECONDS.sleep(3);
        }
    }
}