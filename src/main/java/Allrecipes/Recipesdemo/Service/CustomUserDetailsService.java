//package Allrecipes.Recipesdemo.Service;
//
//import Allrecipes.Recipesdemo.Entities.User;
//import Allrecipes.Recipesdemo.Repositories.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(usernameOrEmail)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        return org.springframework.security.core.userdetails.User
//                .withUsername(user.getUsername())
//                .password(user.getPassword())
//                .roles(user.getRole().name())
//                .build();
//    }
//}
