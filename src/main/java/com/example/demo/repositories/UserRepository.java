package com.example.demo.repositories;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.userDto.UserViewTimesheetDto;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByCode(String code);
    Boolean existsByCode(String code);
    Boolean existsById(int id);
    Boolean existsByUsername(String username);
    Boolean existsByUsernameAndPassword(String username, String password);
    @Query("SELECT e FROM User e WHERE e.role.id=2")
    ArrayList<User> findHQL();
    @Modifying
    @Query("Update User u set u.name= ?1, u.username= ?2, u.password= ?3, u.email= ?4, u.birth= ?5, u.phone= ?6, u.role.id=?7 where u.id= ?8")
    void update(String name, String username, String password, String mail, String birth, String phone, int role_id, int id);

    @Query("Select u.password from User u where username= ?1")
    String findPasswordByUsername(String username);
    @Query("Select u from User u where u.name like %?1%")
    List<User> findUsersByName(String name);
    @Query("Select u From User u where u.email=?1")
    Optional<User> findByEmail(String email);
    @Query("Select u.id From User u ")
    List<Integer> findIds();
    @Query("Select new com.example.demo.dto.userDto.UserViewTimesheetDto(u.name, t.date, t.checkinTime, t.checkoutTime, t.status, t.error) " +
            "From User u Inner Join u.set t " +
            "Where t.date LIKE ?1% AND t.error is not null")
    List<UserViewTimesheetDto> findUsersErrorOfMonth(String month);
    @Query("Select new com.example.demo.dto.userDto.UserViewTimesheetDto(u.name, t.date, t.checkinTime, t.checkoutTime, t.status, t.error) From User u Inner Join u.set t " +
            "Where t.date BETWEEN :start AND :end AND t.checkoutTime is not null")
    List<UserViewTimesheetDto> findUsersTimesheetBetweenTwoDates(String start, String end);
}
INT1404
INT1319