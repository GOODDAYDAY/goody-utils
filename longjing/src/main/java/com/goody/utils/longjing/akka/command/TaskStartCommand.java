package com.goody.utils.longjing.akka.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.goody.utils.longjing.akka.base.TaskCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * start task command, @JsonCreator for config akka.actor.serialization-bindings
 *
 * @author Goody
 * @version 1.0, 2022/8/31 16:34
 * @since 1.0.0
 */
@Data
@AllArgsConstructor(onConstructor_ = @JsonCreator)
@NoArgsConstructor
@Builder
public final class TaskStartCommand implements TaskCommand {
    private String value;
}
