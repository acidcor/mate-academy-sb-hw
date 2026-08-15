package mate.academy.hw.mapper;

import mate.academy.hw.dto.user.UserResponseDto;
import mate.academy.hw.model.User;

public interface UserMapper {
    UserResponseDto toDto(User model);

    User toModel(UserResponseDto dto);
}
