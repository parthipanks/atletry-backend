package com.atletry.mapper;

import com.atletry.dto.response.SkillLevelResponse;
import com.atletry.dto.response.SportResponse;
import com.atletry.entity.SkillLevel;
import com.atletry.entity.Sport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SportMapper {

    @Mapping(target = "isActive",    source = "active")
    @Mapping(target = "skillLevels", source = "skillLevels")
    SportResponse toResponse(Sport sport);

    List<SportResponse> toResponseList(List<Sport> sports);

    @Mapping(target = "levelCode", expression = "java(sl.getLevelCode().name())")
    SkillLevelResponse toSkillLevelResponse(SkillLevel sl);
}
