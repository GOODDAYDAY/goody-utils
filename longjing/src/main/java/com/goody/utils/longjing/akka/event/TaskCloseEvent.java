package com.goody.utils.longjing.akka.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.goody.utils.longjing.akka.base.TaskEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * stop task event, @JsonCreator for config akka.actor.serialization-bindings
 *
 * @author Goody
 * @version 1.0, 2022/8/31 16:34
 * @since 1.0.0
 */
@Data
@AllArgsConstructor(onConstructor_ = @JsonCreator)
@NoArgsConstructor
@Builder
public final class TaskCloseEvent implements TaskEvent {
    private String value;
}
