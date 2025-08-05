package com.example.to_do_list.User;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(("/api/v1/user"))
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> users(){
        return userService.getUsers();
    }

//    @PostMapping
//    public void registerNewUser(@RequestBody User user) {
//        userService.registerNewUser(user);
//    }
//
//    @DeleteMapping(path = "{userId}")
//    public void deleteUser(@PathVariable Long userId) {
//        userService.deleteUser(userId);
//    }
//
//    @PutMapping(path = "{userId}")
//    public void updateUser(
//            @PathVariable Long userId,
//            @RequestParam(required = false) String username,
//            @RequestParam(required = false) String email
//    ) {
//        userService.updateUser(userId, username, email);
//    }
//
//    @PutMapping(path = "{userId}")
//    public void updateUserPassword(
//            @PathVariable Long userId,
//            @RequestParam(required = false) String password
//    ) {
//        userService.updateUserPassword(userId, password);
//    }
}
