package com.example.demo.dto.userDto;

import com.example.demo.Enum.Gender;
import com.example.demo.dto.RoleDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserViewDto {
    private int id;
    private String name;
    private String username;
    private String email;
    private String birth;
    private String phone;
    private String code;
    private RoleDto roleDto;
    private Gender gender;
}
