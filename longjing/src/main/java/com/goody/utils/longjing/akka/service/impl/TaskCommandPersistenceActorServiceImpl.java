package com.goody.utils.longjing.akka.service.impl;

import com.goody.utils.longjing.akka.base.TaskEvent;
import com.goody.utils.longjing.akka.command.TaskStartCommand;
import com.goody.utils.longjing.akka.command.TaskStopCommand;
import com.goody.utils.longjing.akka.command.TaskWorkCommand;
import com.goody.utils.longjing.akka.event.TaskCloseEvent;
import com.goody.utils.longjing.akka.event.TaskStartEvent;
import com.goody.utils.longjing.akka.event.TaskWorkEvent;
import com.goody.utils.longjing.akka.service.ITaskActorService;
import com.goody.utils.longjing.akka.service.ITaskCommandPersistenceActorService;
import com.goody.utils.longjing.akka.state.TaskCloseState;
import com.goody.utils.longjing.akka.state.TaskInitState;
import com.goody.utils.longjing.akka.state.TaskWorkingState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * {@link ITaskActorService} impl
 *
 * @author Goody
 * @version 1.0, 2022/9/1 9:34
 * @since 1.0.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TaskCommandPersistenceActorServiceImpl implements ITaskCommandPersistenceActorService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TaskEvent> handleCommand(TaskStartCommand command, TaskCloseState state) {
        System.out.printf("#TaskStartEvent handle command %s state %s%n", command, state);
        return Collections.singletonList(new TaskStartEvent(command.getValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TaskEvent> handleCommand(TaskWorkCommand command, TaskInitState state) {
        System.out.printf("#TaskWorkCommand handle command %s state %s%n", command, state);
        return Collections.singletonList(new TaskWorkEvent(command.getValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TaskEvent> handleCommand(TaskStopCommand command, TaskWorkingState state) {
        System.out.printf("#TaskStopCommand handle command %s state %s%n", command, state);
        return Collections.singletonList(new TaskCloseEvent(command.getValue()));
    }
}
