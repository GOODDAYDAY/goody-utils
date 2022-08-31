package com.goody.utils.longjing.akka.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.goody.utils.longjing.akka.command.TaskCommand;
import com.goody.utils.longjing.akka.command.TaskStopCommand;

import static com.goody.utils.longjing.akka.util.AkkaUtil.same;

/**
 * task b actor
 *
 * @author Goody
 * @version 1.0, 2022/8/31 14:40
 * @since 1.0.0
 */
public class TaskBActor extends AbstractBehavior<TaskCommand> {

    public TaskBActor(ActorContext<TaskCommand> actorContext) {
        super(actorContext);
        System.out.println("create TaskBActor");
    }

    public static Behavior<TaskCommand> create() {
        return Behaviors.setup(TaskBActor::new);
    }

    @Override
    public Receive<TaskCommand> createReceive() {
        return this.newReceiveBuilder()
            .onMessage(TaskStopCommand.class, s -> same(() -> System.out.println(s)))
            .build();
    }
}
