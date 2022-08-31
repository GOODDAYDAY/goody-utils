package com.goody.utils.longjing.akka.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.goody.utils.longjing.akka.command.TaskCommand;
import com.goody.utils.longjing.akka.command.TaskStartCommand;

import static com.goody.utils.longjing.akka.util.AkkaUtil.same;

/**
 * task actor
 *
 * @author Goody
 * @version 1.0, 2022/8/31 14:40
 * @since 1.0.0
 */
public class TaskActor extends AbstractBehavior<TaskCommand> {

    public TaskActor(ActorContext<TaskCommand> actorContext) {
        super(actorContext);
        System.out.println("create TaskActor");
    }

    public static Behavior<TaskCommand> create() {
        return Behaviors.setup(TaskActor::new);
    }

    @Override
    public Receive<TaskCommand> createReceive() {
        return this.newReceiveBuilder()
            .onMessage(TaskStartCommand.class, s -> same(() -> System.out.println(s)))
            .build();
    }
}
