package com.atletry.mapper;

import com.atletry.dto.response.UserResponse;
import com.atletry.dto.response.UserSportResponse;
import com.atletry.entity.Role;
import com.atletry.entity.User;
import com.atletry.entity.UserSport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "profileComplete", source = "profileComplete")
    @Mapping(target = "roles",  source = "roles")
    @Mapping(target = "sports", ignore = true)   // populated by service
    UserResponse toResponse(User user);

    @Mapping(target = "sportId",               source = "sport.id")
    @Mapping(target = "sportName",             source = "sport.name")
    @Mapping(target = "iconUrl",               source = "sport.iconUrl")
    @Mapping(target = "skillLevelLabel",       expression = "java(us.getSkillLevel().getLabel())")
    @Mapping(target = "skillLevelDescription", expression = "java(us.getSkillLevel().getDescription())")
    UserSportResponse toUserSportResponse(UserSport us);

    List<UserSportResponse> toUserSportResponseList(List<UserSport> list);

    default List<String> rolesToStringList(Set<Role> roles) {
        return roles.stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toList());
    }
}
