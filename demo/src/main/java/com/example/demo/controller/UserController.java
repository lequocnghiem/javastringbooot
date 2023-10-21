package com.example.demo.controller;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.productrepons.UserRepository;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.User;
@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/users")
public class UserController {

	 @Autowired
	    private UserRepository userRepository;
	
	  @PostMapping("/add")
	    public ResponseEntity<User> addUser(
	            @RequestParam("name") String name,
	            @RequestParam("email") String email,
	            
	            @RequestParam("phone") String  phone,
	            @RequestParam("username") String username,
	            @RequestParam("password") String password,
	            @RequestParam("address") String  address ,
	            @RequestPart("imageFile") MultipartFile imageFile,
	            @RequestParam("role") String  role ,
	            @RequestParam("status") String status
	            ) {
	        try {
	            // Xử lý tệp hình ảnh và lưu vào thư mục
	            String imageDirectory = "C:\\Users\\nghie\\Desktop\\LUUHINHANHJAVASTRINGBOOT\\";
	            String imageName = imageDirectory + UUID.randomUUID() + "." + imageFile.getOriginalFilename();
	            Files.write(Paths.get(imageName), imageFile.getBytes());

	            // Tạo mới sản phẩm và set các trường thông tin
	            User User = new User();
	            User.setName(name);
	            User.setEmail(email);
	            User.setPhone(phone);
	            User.setUsername(username);
	            User.setPassword(password);
				User.setAddress(address);
	            User.setImage(imageName);
	            User.setRole(role);
	            User.setStatus(status);

	            // Lưu sản phẩm vào cơ sở dữ liệu
	            final User savedUser = userRepository.save(User);

	            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	    }
	  
	  

	    @GetMapping
	    public List<User> getAllUsers() {
	        return userRepository.findAll();
	    }

	 
	
}
