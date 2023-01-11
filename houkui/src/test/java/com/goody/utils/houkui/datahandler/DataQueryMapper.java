package com.goody.utils.houkui.datahandler;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * data query mapper
 *
 * @author Goody
 * @version 1.0, 2023/1/11 14:59
 * @since 1.0.1
 */
@Mapper
public interface DataQueryMapper {

    @Select("SELECT * FROM task")
    List<Object> query();
}
