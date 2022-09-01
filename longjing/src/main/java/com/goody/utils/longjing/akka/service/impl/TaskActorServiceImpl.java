package com.goody.utils.longjing.akka.service.impl;

import com.goody.utils.longjing.akka.command.TaskStartCommand;
import com.goody.utils.longjing.akka.command.TaskStopCommand;
import com.goody.utils.longjing.akka.command.TaskWorkCommand;
import com.goody.utils.longjing.akka.service.ITaskActorService;
import com.goody.utils.longjing.akka.state.TaskCloseState;
import com.goody.utils.longjing.akka.state.TaskInitState;
import com.goody.utils.longjing.akka.state.TaskState;
import com.goody.utils.longjing.akka.state.TaskWorkingState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
public class TaskActorServiceImpl implements ITaskActorService {

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskState handleCommand(TaskStartCommand command, TaskCloseState state) {
        System.out.printf("#TaskStartCommand handle command %s state %s%n", command, state);
        return new TaskInitState(command.getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskState handleCommand(TaskWorkCommand command, TaskInitState state) {
        System.out.printf("#TaskWorkCommand handle command %s state %s%n", command, state);
        return new TaskWorkingState(command.getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskState handleCommand(TaskStopCommand command, TaskWorkingState state) {
        System.out.printf("#TaskStopCommand handle command %s state %s%n", command, state);
        return new TaskCloseState(command.getValue());
    }
}
