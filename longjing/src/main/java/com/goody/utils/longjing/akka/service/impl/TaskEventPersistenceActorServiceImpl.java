package com.goody.utils.longjing.akka.service.impl;

import com.goody.utils.longjing.akka.base.TaskState;
import com.goody.utils.longjing.akka.event.TaskCloseEvent;
import com.goody.utils.longjing.akka.event.TaskStartEvent;
import com.goody.utils.longjing.akka.event.TaskWorkEvent;
import com.goody.utils.longjing.akka.service.ITaskActorService;
import com.goody.utils.longjing.akka.service.ITaskEventPersistenceActorService;
import com.goody.utils.longjing.akka.state.TaskCloseState;
import com.goody.utils.longjing.akka.state.TaskInitState;
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
public class TaskEventPersistenceActorServiceImpl implements ITaskEventPersistenceActorService {

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskState handleEvent(TaskStartEvent event, TaskCloseState state) {
        System.out.printf("#TaskStartEvent handle event %s state %s%n", event, state);
        return new TaskInitState(event.getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskState handleEvent(TaskWorkEvent event, TaskInitState state) {
        System.out.printf("#TaskWorkEvent handle event %s state %s%n", event, state);
        return new TaskWorkingState(event.getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskState handleEvent(TaskCloseEvent event, TaskWorkingState state) {
        System.out.printf("#TaskStopEvent handle event %s state %s%n", event, state);
        return new TaskCloseState(event.getValue());
    }
}
