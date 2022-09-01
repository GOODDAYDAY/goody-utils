package com.goody.utils.longjing.akka.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.ReceiveBuilder;
import com.goody.utils.longjing.akka.base.TaskCommand;
import com.goody.utils.longjing.akka.base.TaskState;
import com.goody.utils.longjing.akka.command.TaskStartCommand;
import com.goody.utils.longjing.akka.command.TaskStopCommand;
import com.goody.utils.longjing.akka.command.TaskWorkCommand;
import com.goody.utils.longjing.akka.service.ITaskActorService;
import com.goody.utils.longjing.akka.state.TaskCloseState;
import com.goody.utils.longjing.akka.state.TaskInitState;
import com.goody.utils.longjing.akka.state.TaskWorkingState;

import java.util.function.Supplier;

/**
 * task actor
 *
 * @author Goody
 * @version 1.0, 2022/8/31 14:40
 * @since 1.0.0
 */
public class TaskActor extends AbstractBehavior<TaskCommand> {
    private final ITaskActorService service;
    /** state of one actor */
    public TaskState state;

    public TaskActor(ActorContext<TaskCommand> actorContext, ITaskActorService service, String value) {
        super(actorContext);
        this.state = new TaskCloseState(value);
        this.service = service;
        System.out.println("create TaskActor " + this.state);
    }

    @Override
    public Receive<TaskCommand> createReceive() {
        final ReceiveBuilder<TaskCommand> builder = this.newReceiveBuilder();

        // CLOSE
        builder
            // task start, state to TaskInitState
            .onMessage(TaskStartCommand.class, this::isClose, c -> this.handleCommand(() -> service.handleCommand(c, (TaskCloseState) state)));

        // INIT
        builder
            // task working, state to TaskWorkingState
            .onMessage(TaskWorkCommand.class, this::isInit, c -> this.handleCommand(() -> service.handleCommand(c, (TaskInitState) state)));

        // WORKING
        builder
            // task start, state to TaskCloseState
            .onMessage(TaskStopCommand.class, this::isWorking, c -> this.handleCommand(() -> service.handleCommand(c, (TaskWorkingState) state)));

        builder
            // other command
            .onAnyMessage(c -> this.handleCommand(() -> service.handleCommand(c, state)));
        return builder.build();
    }

    /**
     * common handle, user only need to impl bizService
     *
     * @param stateSupplier supplier produce state
     * @return behavior[taskCommand]
     */
    private Behavior<TaskCommand> handleCommand(Supplier<TaskState> stateSupplier) {
        this.state = stateSupplier.get();
        return Behaviors.same();
    }

    private boolean isInit(Object command) {
        return this.state instanceof TaskInitState;
    }

    private boolean isWorking(Object command) {
        return this.state instanceof TaskWorkingState;
    }

    private boolean isClose(Object command) {
        return this.state instanceof TaskCloseState;
    }
}
