package com.goody.utils.longjing.akka.actor;

import akka.persistence.typed.PersistenceId;
import akka.persistence.typed.javadsl.CommandHandler;
import akka.persistence.typed.javadsl.CommandHandlerBuilder;
import akka.persistence.typed.javadsl.Effect;
import akka.persistence.typed.javadsl.EventHandler;
import akka.persistence.typed.javadsl.EventHandlerBuilder;
import akka.persistence.typed.javadsl.EventSourcedBehavior;
import com.goody.utils.longjing.akka.base.TaskCommand;
import com.goody.utils.longjing.akka.base.TaskEvent;
import com.goody.utils.longjing.akka.base.TaskState;
import com.goody.utils.longjing.akka.command.TaskStartCommand;
import com.goody.utils.longjing.akka.command.TaskStopCommand;
import com.goody.utils.longjing.akka.command.TaskWorkCommand;
import com.goody.utils.longjing.akka.event.TaskCloseEvent;
import com.goody.utils.longjing.akka.event.TaskStartEvent;
import com.goody.utils.longjing.akka.event.TaskWorkEvent;
import com.goody.utils.longjing.akka.service.ITaskCommandPersistenceActorService;
import com.goody.utils.longjing.akka.service.ITaskEventPersistenceActorService;
import com.goody.utils.longjing.akka.state.TaskCloseState;
import com.goody.utils.longjing.akka.state.TaskInitState;
import com.goody.utils.longjing.akka.state.TaskWorkingState;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Supplier;

/**
 * taskPersistenceActor
 *
 * @author Goody
 * @version 1.0, 2022/9/5 20:28
 * @since 1.0.0
 */
public class TaskPersistenceActor extends EventSourcedBehavior<TaskCommand, TaskEvent, TaskState> {

    private final ITaskCommandPersistenceActorService commandService;
    private final ITaskEventPersistenceActorService eventService;

    public TaskPersistenceActor(PersistenceId persistenceId,
                                ITaskCommandPersistenceActorService commandService,
                                ITaskEventPersistenceActorService eventService) {
        super(persistenceId);
        this.commandService = commandService;
        this.eventService = eventService;
    }

    @Override
    public TaskState emptyState() {
        return new TaskCloseState("TaskPersistenceActor init");
    }

    @Override
    public CommandHandler<TaskCommand, TaskEvent, TaskState> commandHandler() {
        final CommandHandlerBuilder<TaskCommand, TaskEvent, TaskState> builder = newCommandHandlerBuilder();

        // CLOSE
        builder.forStateType(TaskCloseState.class)
            // task close, generate TaskStartEvent
            .onCommand(TaskStartCommand.class, (s, c) -> this.handleCommand(() -> this.commandService.handleCommand(c, s)));

        // INIT
        builder.forStateType(TaskInitState.class)
            // task init, generate TaskWorkEvent
            .onCommand(TaskWorkCommand.class, (s, c) -> this.handleCommand(() -> this.commandService.handleCommand(c, s)));

        // WORKING
        builder.forStateType(TaskWorkingState.class)
            // task working, generate TaskCloseEvent
            .onCommand(TaskStopCommand.class, (s, c) -> this.handleCommand(() -> this.commandService.handleCommand(c, s)));

        builder.forAnyState()
            // for all other
            .onAnyCommand((s, c) -> this.handleCommand(() -> this.commandService.handleCommand(c, s)));

        return builder.build();
    }

    @Override
    public EventHandler<TaskState, TaskEvent> eventHandler() {
        final EventHandlerBuilder<TaskState, TaskEvent> builder = newEventHandlerBuilder();

        // CLOSE
        builder.forStateType(TaskCloseState.class)
            // task close, state to TaskInitState
            .onEvent(TaskStartEvent.class, (s, e) -> this.handleEvent(s, () -> this.eventService.handleEvent(e, s)));

        // INIT
        builder.forStateType(TaskInitState.class)
            // task init, state to TaskWorkingState
            .onEvent(TaskWorkEvent.class, (s, e) -> this.handleEvent(s, () -> this.eventService.handleEvent(e, s)));

        // WORKING
        builder.forStateType(TaskWorkingState.class)
            // task working, state to TaskCloseState
            .onEvent(TaskCloseEvent.class, (s, e) -> this.handleEvent(s, () -> this.eventService.handleEvent(e, s)));

        builder.forAnyState()
            // for all other
            .onAnyEvent((s, e) -> this.handleEvent(s, () -> this.eventService.handleEvent(e, s)));

        return builder.build();
    }

    /**
     * command handleCommand
     *
     * @param eventSupplier the supplier invoke {@link ITaskCommandPersistenceActorService}
     * @return result
     */
    private Effect<TaskEvent, TaskState> handleCommand(Supplier<List<TaskEvent>> eventSupplier) {
        final List<TaskEvent> events = eventSupplier.get();
        if (CollectionUtils.isEmpty(events)) {
            return Effect().none();
        }
        return Effect()
            .persist(events)
            .thenNoReply();
    }

    /**
     * event handleEvent. if not changed it will print something
     *
     * @param state         oldState
     * @param stateSupplier the supplier invoke {@link ITaskEventPersistenceActorService}
     * @return result
     */
    private TaskState handleEvent(TaskState state, Supplier<TaskState> stateSupplier) {
        final TaskState newState = stateSupplier.get();
        if (state == newState) {
            System.out.println("TaskPersistenceActor#handleEvent do nothing");
            return newState;
        }
        System.out.println("TaskPersistenceActor#handleEvent to new State " + newState.toString());
        return newState;
    }
}
