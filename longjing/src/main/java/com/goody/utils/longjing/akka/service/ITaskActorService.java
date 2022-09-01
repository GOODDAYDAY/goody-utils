package com.goody.utils.longjing.akka.service;

import com.goody.utils.longjing.akka.command.TaskCommand;
import com.goody.utils.longjing.akka.command.TaskStartCommand;
import com.goody.utils.longjing.akka.command.TaskStopCommand;
import com.goody.utils.longjing.akka.command.TaskWorkCommand;
import com.goody.utils.longjing.akka.state.TaskCloseState;
import com.goody.utils.longjing.akka.state.TaskInitState;
import com.goody.utils.longjing.akka.state.TaskState;
import com.goody.utils.longjing.akka.state.TaskWorkingState;

/**
 * service for taskActor
 *
 * @author Goody
 * @version 1.0, 2022/9/1 9:18
 * @since 1.0.0
 */
public interface ITaskActorService {

    /**
     * task starting
     *
     * @param command taskStartCommand
     * @param state   closeState
     * @return {@link com.goody.utils.longjing.akka.state.TaskInitState}
     */
    TaskState handleCommand(TaskStartCommand command, TaskCloseState state);

    /**
     * task starting
     *
     * @param command taskWorkCommand
     * @param state   initState
     * @return {@link com.goody.utils.longjing.akka.state.TaskWorkingState}
     */
    TaskState handleCommand(TaskWorkCommand command, TaskInitState state);

    /**
     * task stop
     *
     * @param command taskStopCommand
     * @param state   workingState
     * @return {@link com.goody.utils.longjing.akka.state.TaskCloseState}
     */
    TaskState handleCommand(TaskStopCommand command, TaskWorkingState state);

    /**
     * unhandled function
     *
     * @param command command
     * @param state   state
     * @return the state input
     */
    default TaskState handleCommand(TaskCommand command, TaskState state) {
//        System.out.printf("#handleCommand unhandled command %s state %s%n", command, state);
        return state;
    }
}
