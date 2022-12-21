package com.example.demo.service;

import com.example.demo.Enum.Week;
import com.example.demo.dto.LoginDto;
import com.example.demo.dto.mailDto.MailSendAccountDto;
import com.example.demo.dto.RoleDto;
import com.example.demo.dto.userDto.UserViewDto;
import com.example.demo.dto.userDto.UserUpdateDto;
import com.example.demo.dto.userDto.UserViewTimesheetDto;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repositories.TimesheetRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.jwt.JwtProvider;
import com.example.demo.security.userprincal.UserDetailService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TimesheetRepository timesheetRepository;
    @Autowired
    ModelMapper mapper;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UserDetailService userDetailService;
    @Autowired
    MailService mailService;
    private static Logger logger= LoggerFactory.getLogger(UserService.class);

    public String findPasswordByUsername(String username){
        return userRepository.findPasswordByUsername(username);
    }
    public Boolean existsByCode(String code){
        return userRepository.existsByCode(code);
    }
    public Boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }
    public Boolean existsById(int id){
        return userRepository.existsById(id);
    }
    public void deleteById(int id){
        userRepository.deleteById(id);
    }
    public UserViewDto save(UserUpdateDto userUpdateDto){
        Random random= new Random();
        MailSendAccountDto content= new MailSendAccountDto();
        StringBuilder code= new StringBuilder("0000");
        do{
            code.setCharAt(0, (char)(random.nextInt(10)+'0'));
            code.setCharAt(1, (char)(random.nextInt(10)+'0'));
            code.setCharAt(2, (char)(random.nextInt(10)+'0'));
            code.setCharAt(3, (char)(random.nextInt(10)+'0'));
            if(!userRepository.existsByCode(code.toString())) break;
        }while(true);
        logger.info("code of new user: "+code);
        userUpdateDto.setCode(code.toString());
        User user= userRepository.save(mapper.map(userUpdateDto, User.class));
        UserViewDto userViewDto= mapper.map(user, UserViewDto.class);
        userViewDto.setRoleDto(mapper.map(user.getRole(), RoleDto.class));

        content.setName(userUpdateDto.getName());
        content.setUsername(userUpdateDto.getUsername());
        content.setPassword(userUpdateDto.getPassword());
        content.setCode(userUpdateDto.getCode());
        content.setMail(userUpdateDto.getEmail());
        content.setLink("http://localhost:8080/");
        mailService.SendAccount(content);

        return userViewDto;
    }
    @Transactional
    public String updateUser(UserUpdateDto userNew, int id){
        Optional<User> oldUser= userRepository.findById(id);
        User user= oldUser.get();
        if(userNew.getName()!=null) user.setName(userNew.getName());
        if(userNew.getUsername()!=null) user.setUsername(userNew.getUsername());
        if(userNew.getPassword()!=null) user.setPassword(userNew.getPassword());
        if(userNew.getEmail()!=null) user.setEmail(userNew.getEmail());
        if(userNew.getBirth()!=null) user.setBirth(userNew.getBirth());
        if(userNew.getPhone()!=null) user.setPhone(userNew.getPhone());
        if(userNew.getRoleDto()!=null) user.setRole(mapper.map(userNew.getRoleDto(), Role.class));
        if(userNew.getGender()!=null) user.setGender(userNew.getGender());
        userRepository.update(user.getName(), user.getUsername(), user.getPassword(), user.getEmail(),
                user.getBirth(), user.getPhone(), user.getRole().getId(), user.getId());
        return "done!!!";
    }

    public UserViewDto findById(int id){
        Optional<User> user= userRepository.findById(id);
        UserViewDto userDTO= mapper.map(user.get(), UserViewDto.class);
        userDTO.setRoleDto(mapper.map(user.get().getRole(), RoleDto.class));
        return userDTO;
    }
    public List<UserViewDto> findAll(){
        List<User> list= userRepository.findAll();
        List<UserViewDto> dtoList= list.stream().map(user -> {
            UserViewDto userDTO= mapper.map(user, UserViewDto.class);
            RoleDto roleDto= mapper.map(user.getRole(), RoleDto.class);
            userDTO.setRoleDto(roleDto);
            return userDTO;
        }).collect(Collectors.toList());
        return dtoList;
    }
    public boolean existsByUsernameAndPassword(LoginDto loginDto){
        return userRepository.existsByUsernameAndPassword(loginDto.getUsername(), loginDto.getPassword());
    }
    public String login(LoginDto loginDto){
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails= userDetailService.loadUserByUsername(authentication.getName());
        //UserDetails userDetails= userDetailService.loadUserByUsername(loginDto.getUsername());
        //logger.info("username: "+userDetails.getUsername());
        return jwtProvider.createToken(userDetails);
    }
    public String loginWithEmail(String email){
        Optional<User> user= userRepository.findByEmail(email);
        if(!user.isPresent()) return "email not found";
        /*authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.get().getUsername(), "admin0")
        );*/
        logger.info(user.get().getRole().getName());
        UserDetails userDetails= userDetailService.loadUserByUsername(user.get().getUsername());
        UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        logger.info("username: "+userDetails.getUsername());
        return jwtProvider.createToken(userDetails);
    }

    public ArrayList<UserViewDto> findHQL(){
        ArrayList<User> list= userRepository.findHQL();
        ArrayList<UserViewDto> listDto= (ArrayList<UserViewDto>) list.stream().map(user -> {
            UserViewDto userDto = mapper.map(user, UserViewDto.class);
            RoleDto roleDto = mapper.map(user.getRole(), RoleDto.class);
            userDto.setRoleDto(roleDto);
            return userDto;
        }).collect(Collectors.toList());
        return listDto;
    }
    public UserViewDto viewProfileByUsername(String username){
        UserViewDto userDto= mapper.map(userRepository.findByUsername(username).get(), UserViewDto.class);
        return userDto;
    }
    public List<UserViewDto> searchUsersByName(String name){
        List<User> list= userRepository.findUsersByName(name);
        return list.stream().map(user -> {
            UserViewDto userDto= mapper.map(user, UserViewDto.class);
            userDto.setRoleDto(mapper.map(user.getRole(), RoleDto.class));
            return userDto;
        }).collect(Collectors.toList());
    }
    public HashMap<String, List<UserViewTimesheetDto>> findUsersErrorOfMonth(String month){
        HashMap<String, List<UserViewTimesheetDto>> hash= new HashMap<>();
        userRepository.findUsersErrorOfMonth(month).forEach(userViewTimesheetDto -> {
            if(hash.get(userViewTimesheetDto.getName())==null)
                hash.put(userViewTimesheetDto.getName(), new ArrayList<>());
            hash.get(userViewTimesheetDto.getName()).add(userViewTimesheetDto);
        });
        return hash;
    }
    public HashMap<String, List<UserViewTimesheetDto>> findUsersErrorOfMonth(){
        String month= new SimpleDateFormat("yyyy:MM").format(new Date());
        return findUsersErrorOfMonth(month);
    }
    public HashMap<String, List<UserViewTimesheetDto>> findUsersTimesheetBetweenTwoDates(String start, String end){
        HashMap<String, List<UserViewTimesheetDto>> hash= new HashMap<>();
        userRepository.findUsersTimesheetBetweenTwoDates(start, end).forEach(userViewTimesheetDto -> {
            if(hash.get(userViewTimesheetDto.getName())==null)
                hash.put(userViewTimesheetDto.getName(), new ArrayList<>());
            hash.get(userViewTimesheetDto.getName()).add(userViewTimesheetDto);
        });
        return hash;
    }
    public HashMap<String, List<UserViewTimesheetDto>> findUsersTimesheetBetweenTwoDates(){
        SimpleDateFormat formatDate= new SimpleDateFormat("yyyy:MM:dd");
        String now= new SimpleDateFormat("E").format(new Date());
        Week day= Week.valueOf(now);
        String startWeek= formatDate.format(new Date(new Date().getTime()- (long) day.getStartWeek() *1000*60*60*24));
        String endWeek= formatDate.format(new Date(new Date().getTime()+ (long) (6 - day.getStartWeek()) *1000*60*60*24));
        return findUsersTimesheetBetweenTwoDates(startWeek, endWeek);
    }
}
