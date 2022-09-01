package com.goody.utils.longjing.akka.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * init state
 *
 * @author Goody
 * @version 1.0, 2022/9/1 9:19
 * @since 1.0.0
 */
@Data
@AllArgsConstructor(onConstructor_ = @JsonCreator)
@NoArgsConstructor
@Builder(toBuilder = true)
public class TaskInitState implements TaskState {
    private String value;
}
