package com.mongodb.oauth2.secure;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.mongodb.oauth2.entity.Permission;
import com.mongodb.oauth2.entity.Role;
import com.mongodb.oauth2.entity.User;
import com.mongodb.oauth2.service.PermissionService;
import com.mongodb.oauth2.service.RoleService;
import com.mongodb.oauth2.service.UserService;


@Service
@Qualifier("secureUserDetailsService")
public class SecureUserDetailsService implements UserDetailsService{
	
	Logger log = LoggerFactory.getLogger(SecureUserDetailsService.class);
	
	@Autowired
	private UserService userInfoService;
	
	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private ClientDetailsService clientDetailsService;

	@Override
	public UserDetails loadUserByUsername(String username){
		log.info("Username:> {}",username);
		User user = userInfoService.findByUsername(username);
		if(null == user){
			log.info("Invalid username or password.");
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		ClientDetails clientDetails = fetchClientDetails();
		Object[] roleDetails = getUserRoleList(user.getRoles());
		@SuppressWarnings("unchecked")
		List<Role> roleList = (List<Role>) roleDetails[0];
		@SuppressWarnings("unchecked")
		List<String> roleNameList = (List<String>) roleDetails[1];
		
		List<String> commonRoleList = roleNameList.stream().filter(clientDetails.getScope()::contains).collect(Collectors.toList());
		
		if(commonRoleList.isEmpty()) {
			log.info("user not associated with client id.");
			return new SecureUserDetails(user.getUsername(),user.getPassword(),Boolean.FALSE,AuthorityUtils.NO_AUTHORITIES);
		}
		
		List<Role> roleListForPermission = roleList.stream().filter(role -> commonRoleList.contains(role.getName())).collect(Collectors.toList());
		
		return new SecureUserDetails(user.getUsername(),user.getPassword(),Boolean.TRUE,createPermissionList(roleListForPermission));
	}

	private List<GrantedAuthority> createPermissionList(List<Role> roleListForPermission) {
		log.info("roleListForPermission roles: {} ",roleListForPermission);
		List<GrantedAuthority> authoritiesList = new ArrayList<>();
		
		for(Role role : roleListForPermission) {
			
			for(String permissionId: role.getPermissions()) {
				Permission permission = permissionService.get(permissionId);
				if(permission != null) {
					authoritiesList.add(new SimpleGrantedAuthority(permission.getName()));
				}
			}
			
		}
		
		if(authoritiesList.isEmpty())
			return AuthorityUtils.NO_AUTHORITIES;
		else
			return authoritiesList;
	}
	
	private Object[] getUserRoleList(List<String> roles) {
		
		Object[] userRoleListDetails= new Object[2];
		
		List<Role> roleList = new ArrayList<>();
		List<String> roleNameList = new ArrayList<>();
		if(!roles.isEmpty()) {
			for(String roleId : roles) {
				Role role = roleService.get(roleId);
				roleList.add(role);
				roleNameList.add(role.getName());
			}
			userRoleListDetails[0] = roleList;
			userRoleListDetails[1] = roleNameList;
			return userRoleListDetails;
		}
		
		userRoleListDetails[0] = Collections.emptyList();
		userRoleListDetails[1] = Collections.emptyList();
		return userRoleListDetails;
	}
	
	private ClientDetails fetchClientDetails() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		log.info("request:> {} ",request);
		String header = request.getHeader("Authorization");
		log.info("header:> {} ",header);

		byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
		byte[] decoded = Base64.getDecoder().decode(base64Token);
		String token = new String(decoded,StandardCharsets.UTF_8);
		log.info("token:> {} ",token);
		return clientDetailsService.loadClientByClientId(token.split(":")[0]);
	}
}
