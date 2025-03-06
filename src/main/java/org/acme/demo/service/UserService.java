package org.acme.demo.service;

import jakarta.annotation.PostConstruct;
import org.acme.demo.entity.AppUser;
import org.acme.demo.entity.Role;
import org.acme.demo.entity.RoleName;
import org.acme.demo.repository.RoleRepository;
import org.acme.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements  UserDetailsService {

    @Value("${DEFAULT_ADMIN_USERNAME}")
    private String defaultAdminUsername;

    @Value("${DEFAULT_ADMIN_EMAIL}")
    private String defaultAdminEmail;

    @Value("${DEFAULT_ADMIN_PASSWORD}")
    private String defaultAdminPassword;

    // Correct Constructor Declaration


   private final UserRepository userRepository;
   private final RoleRepository roleRepository;
   private final PasswordEncoder passwordEncoder;

   
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

   public AppUser registerUser(String username, String email, String password, String provider, String providerId){
        Optional<AppUser> existingUser = userRepository.findByEmail(email);

       if (existingUser.isPresent()) {
           AppUser user = existingUser.get();
           if (provider != null && providerId != null) {
               if (provider.equals(user.getProvider()) && providerId.equals(user.getProviderId())) {
                   return user;
               } else {
                   throw new IllegalArgumentException("Email is already registered with a different method");
               }
           } else {
               throw new IllegalArgumentException("Email is already registered");
           }
       }

       if (userRepository.findByUsername(username).isPresent()){
           throw new IllegalArgumentException("Username already taken");
       }

       AppUser newUser = new AppUser();

       if (username == null || username.isEmpty() || username.isBlank()){
           username = generateDefaultUsername(email);
       }

       newUser.setUsername(username);
       newUser.setEmail(email);

       if(provider != null && providerId != null){
           newUser.setProvider(provider);
           newUser.setProviderId(providerId);
           newUser.setPassword(null);

       } else{
           newUser.setPassword(passwordEncoder.encode(password));
       }

       //Assign Default Role
       Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
               .orElseThrow(() -> new RuntimeException("Role not found"));
       newUser.setRoles(Set.of(userRole)); // Set the default role for the user

       return userRepository.save(newUser);
   }

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
       AppUser appUser = findUserByUsername(username);
       if(appUser == null){
           throw new UsernameNotFoundException("User not found with username: " + username);
       }

       Set<GrantedAuthority> authorities = appUser.getRoles().stream()
               .map(role -> new SimpleGrantedAuthority(role.getName().name()))
               .collect(Collectors.toSet());

       return User.builder()
               .username(appUser.getUsername())
               .password(appUser.getPassword())
               .authorities(authorities)
               .build();
   }

   public AppUser findUserByEmail(String email){
       return userRepository.findByEmail(email)
               .orElse(null);
   }
    public AppUser findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }

    public AppUser findUserByProviderAndProviderId(String provider, String providerId){
       return userRepository.findByProviderAndProviderId(provider, providerId).orElse(null);
    }

    public String generateDefaultUsername(String email){
        if (email != null && email.contains("@")) {
            return email.split("@")[0];
        }
        return "user" + System.currentTimeMillis();
    }

    public AppUser changeUsername(String currentUsername, String newUsername){
       AppUser user = findUserByUsername(currentUsername);
       if (user == null){
           throw new RuntimeException("User not found");
       }
       if (userRepository.findByUsername(newUsername).isPresent()){
           throw new RuntimeException("Username already taken");
       }

       user.setUsername(newUsername);
       return userRepository.save(user);
    }

    public AppUser changeEmail(String currentUsername, String newEmail){
        AppUser user = findUserByUsername(currentUsername);
        if (user == null){
            throw new RuntimeException("User not found");
        }

        //Cant change email for oAuth users
        if (user.getProvider() != null){
            throw  new RuntimeException("You can not change this email");
        }

        if (userRepository.findByEmail(newEmail).isPresent()){
            throw new RuntimeException("Email is already registered");
        }
        user.setEmail(newEmail);
        return userRepository.save(user);
    }

    public AppUser changePassword(String currentUsername, String currentPassword, String newPassword){
        AppUser user = findUserByUsername(currentUsername);

        if (user == null){
            throw new RuntimeException("User not found");
        }

        //Cant change email for oAuth users
        if (user.getProvider() != null){
            throw  new RuntimeException("You can not change this password");
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())){
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);


    }


    //Generating default admin profile
    @PostConstruct
    public void initializeDefaultAdmin() {
        Optional<AppUser> adminUser = userRepository.findByUsername(defaultAdminUsername);
        if (adminUser.isEmpty()) {
            AppUser admin = new AppUser();
            admin.setUsername(defaultAdminUsername);
            admin.setPassword(passwordEncoder.encode(defaultAdminPassword));
            admin.setEmail(defaultAdminEmail);

            Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));
            admin.setRoles(Set.of(adminRole));

            userRepository.save(admin);
            System.out.println("Admin user created: " + defaultAdminUsername);
        }
    }

}
