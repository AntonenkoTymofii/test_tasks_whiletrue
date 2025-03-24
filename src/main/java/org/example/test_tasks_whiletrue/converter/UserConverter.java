package org.example.test_tasks_whiletrue.converter;

import org.example.test_tasks_whiletrue.dto.UserDTO;
import org.example.test_tasks_whiletrue.model.UserModel;

public class UserConverter {

    public static UserDTO toDTO(UserModel user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        return dto;
    }

    public static UserModel toEntity(UserDTO dto) {
        UserModel user = new UserModel();
        user.setId(dto.getId());
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        return user;
    }
}
