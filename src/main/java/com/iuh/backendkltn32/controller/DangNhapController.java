package com.iuh.backendkltn32.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.config.JwtUntils;
import com.iuh.backendkltn32.config.UserInfoUserDetails;
import com.iuh.backendkltn32.dto.JwtResponse;
import com.iuh.backendkltn32.dto.LoginRequest;
import com.iuh.backendkltn32.dto.RoleDto;
import com.iuh.backendkltn32.dto.UserDto;
import com.iuh.backendkltn32.entity.GiangVien;
import com.iuh.backendkltn32.entity.QuanLyBoMon;
import com.iuh.backendkltn32.entity.SinhVien;
import com.iuh.backendkltn32.service.GiangVienService;
import com.iuh.backendkltn32.service.QuanLyBoMonService;
import com.iuh.backendkltn32.service.SinhVienService;

@RestController
@RequestMapping("/api/xac-thuc")
public class DangNhapController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUntils jwtService;

	@Autowired
	private SinhVienService sinhVienService;

	@Autowired
	private GiangVienService giangVienService;
	
	@Autowired
	private QuanLyBoMonService quanLyBoMonService;

	@PostMapping("/dang-nhap")
	public ResponseEntity<?> dangNhap(@Validated @RequestBody LoginRequest loginRequest) throws Exception {
		String tenTaiKhoan = loginRequest.getTenTaiKhoan();
		// Xác thực từ username và password.
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(tenTaiKhoan, loginRequest.getPassword()));

		System.out.println("DangNhapController-dangNhap- " + loginRequest.getTenTaiKhoan());

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtService.generateJWTToken(authentication);

		UserInfoUserDetails userDetailsImpl = (UserInfoUserDetails) authentication.getPrincipal();

		List<String> roles = userDetailsImpl.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		RoleDto roleDto = new RoleDto(roles.get(0), roles.get(0).substring(5) + " Role");

		switch (roleDto.getRoleName()) {
		case "ROLE_GIANGVIEN": {
			
			GiangVien giangVien = giangVienService.layTheoMa(tenTaiKhoan);
			return ResponseEntity.ok(new JwtResponse(jwt,  new UserDto<GiangVien>(Arrays.asList(roleDto),  
					giangVien)));
		}
		case "ROLE_SINHVIEN" : {
			SinhVien sinhVien = sinhVienService.layTheoMa(tenTaiKhoan);
			return ResponseEntity.ok(new JwtResponse(jwt, new UserDto<SinhVien>(Arrays.asList(roleDto), 
				 sinhVien)));
		}
			
		case "ROLE_QUANLY": {
			QuanLyBoMon quanLyBoMon = quanLyBoMonService.layTheoMa(tenTaiKhoan);
			return ResponseEntity.ok(new JwtResponse(jwt, new UserDto<QuanLyBoMon>(Arrays.asList(roleDto), 
				 quanLyBoMon)));
			}
		}
		
		throw new Exception("Dont have user");
	}

//	@PostMapping("/test")
//	public ResponseEntity<?> test(@Validated @RequestBody LoginRequest loginRequest) {
//
//		// Xác thực từ username và password.
//		Authentication authentication = authenticationManager.authenticate(
//				new UsernamePasswordAuthenticationToken(loginRequest.getTenTaiKhoan(), loginRequest.getPassword()));
//
//		System.out.println("DangNhapController-dangNhap- " + loginRequest.getTenTaiKhoan());
//
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//		String jwt = jwtService.generateJWTToken(authentication);
//
//		UserInfoUserDetails userDetailsImpl = (UserInfoUserDetails) authentication.getPrincipal();
//
//		List<String> roles = userDetailsImpl.getAuthorities().stream().map(item -> item.getAuthority())
//				.collect(Collectors.toList());
//
//		return ResponseEntity.ok(new JwtResponse(jwt, "Bearer ", userDetailsImpl.getUsername(), roles));
//
//	}

}
