package com.goody.utils.longjing.akka.service;

import com.goody.utils.longjing.akka.base.TaskEvent;
import com.goody.utils.longjing.akka.base.TaskState;
import com.goody.utils.longjing.akka.event.TaskCloseEvent;
import com.goody.utils.longjing.akka.event.TaskStartEvent;
import com.goody.utils.longjing.akka.event.TaskWorkEvent;
import com.goody.utils.longjing.akka.state.TaskCloseState;
import com.goody.utils.longjing.akka.state.TaskInitState;
import com.goody.utils.longjing.akka.state.TaskWorkingState;

/**
 * event service for {@link com.goody.utils.longjing.akka.actor.TaskPersistenceActor}
 *
 * @author Goody
 * @version 1.0, 2022/9/1 9:18
 * @since 1.0.0
 */
public interface ITaskEventPersistenceActorService {

    /**
     * task starting
     *
     * @param event taskStartEvent
     * @param state closeState
     * @return {@link com.goody.utils.longjing.akka.event.TaskStartEvent}
     */
    TaskState handleEvent(TaskStartEvent event, TaskCloseState state);

    /**
     * task starting
     *
     * @param event taskWorkEvent
     * @param state initState
     * @return {@link com.goody.utils.longjing.akka.event.TaskWorkEvent}
     */
    TaskState handleEvent(TaskWorkEvent event, TaskInitState state);

    /**
     * task stop
     *
     * @param event taskStopEvent
     * @param state workingState
     * @return {@link com.goody.utils.longjing.akka.event.TaskCloseEvent}
     */
    TaskState handleEvent(TaskCloseEvent event, TaskWorkingState state);

    /**
     * unhandled function
     *
     * @param event event
     * @param state state
     * @return the state input
     */
    default TaskState handleEvent(TaskEvent event, TaskState state) {
        System.out.printf("#handleEvent unhandled event %s state %s%n", event, state);
        return state;
    }
}
