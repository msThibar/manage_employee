package com.example.demo.dto.userDto;

import com.example.demo.dto.RoleDto;
import com.example.demo.Enum.Gender;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserUpdateDto {
    private String name;
    private String username;
    private String password;
    private String email;
    private String birth;
    private String phone;
    private String code;
    private Gender gender;
    private RoleDto roleDto;
}
