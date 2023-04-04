package com.wittybrains.busbookingsystem.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wittybrains.busbookingsystem.dto.UserDTO;

import com.wittybrains.busbookingsystem.service.UserService;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/users")
public class UserController {
	private static final Logger logger = Logger.getLogger(UserController.class.getName());

	
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/{userId}")
	public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
		logger.log(Level.INFO, "Fetching user with id: " + userId);
		UserDTO userDTO = userService.getUser(userId);
		if (userDTO != null) {
			return ResponseEntity.ok(userDTO);
		} else {
			return ResponseEntity.notFound().build();
		}
	}



	@PostMapping("/users")
	public ResponseEntity<Long> createUser(@RequestBody UserDTO newUserDTO) {
		logger.log(Level.INFO, "Creating user with username: " + newUserDTO.getUsername());
		Long savedUserId = userService.createUser(newUserDTO).getId();
		return ResponseEntity.ok(savedUserId);
	}

	@PutMapping("/{userId}")
	public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody UserDTO updatedUserDTO) {
		logger.log(Level.INFO, "Updating user with id: " + userId);
		UserDTO updatedUser = userService.updateUser(userId, updatedUserDTO);
		if (updatedUser != null) {
			return ResponseEntity.ok(updatedUser);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
		logger.log(Level.INFO, "Deleting user with id: " + userId);
		userService.deleteUser(userId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		logger.log(Level.INFO, "Fetching all users");
		List<UserDTO> userDTOList = userService.getAllUsers();
		if (!userDTOList.isEmpty()) {
			return ResponseEntity.ok(userDTOList);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
