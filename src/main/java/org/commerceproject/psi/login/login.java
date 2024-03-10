package org.commerceproject.psi.login;

public class login {
    /*
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api")
    public class RegistrationController {

        private final UserService userService;

        public RegistrationController(UserService userService) {
            this.userService = userService;
        }

        @PostMapping("/signup")
        public ResponseEntity<?> register(@RequestBody RegistrationRequest registrationRequest) {
            userService.register(registrationRequest);
            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        }
    }
    public class RegistrationRequest {
        private String username;
        private String email;
        private String password;

        // getters and setters
    }
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

    @Service
    public class UserService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        @Autowired
        public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
        }

        public void register(RegistrationRequest registrationRequest) {
            String username = registrationRequest.getUsername();
            // Here you should check if the email and username are unique
            // If not, throw an exception. For example:
            // throw new UserAlreadyExistsException("There is an account with that email: " + email);
            // You'd need to define this exception class yourself.
            String encodedPassword = passwordEncoder.encode(registrationRequest.getPassword());

            // Create and save user object
            User user = new User();
            user.setUsername(username);
            user.setEmail(registrationRequest.getEmail());
            user.setPassword(encodedPassword);
            userRepository.save(user);
        }
    }
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

    @Configuration
    public class PasswordEncoder {
        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
    import org.springframework.data.jpa.repository.JpaRepository;

    public interface UserRepository extends JpaRepository<User, Long> {
    }
     */

    //login
    /*
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api")
    public class LoginController {

        private final UserService userService;

        public LoginController(UserService userService) {
            this.userService = userService;
        }

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
            try {
                String jwt = userService.login(loginRequest);
                return ResponseEntity.ok(new JwtResponse(jwt));
            } catch (InvalidCredentialsException e) {
                return ResponseEntity.badRequest().body("Error: Invalid credentials");
            }
        }
    }
        public class LoginRequest {
        private String username;
        private String password;

        // getters and setters
    }
    import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

    @Service
    public class UserService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtUtil jwtUtil;

        @Autowired
        public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
            this.jwtUtil = jwtUtil;
        }

        public String login(LoginRequest loginRequest) {
            Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                boolean passwordsMatch = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());

                if (passwordsMatch) {
                    // JWT creation
                    return jwtUtil.generateToken(user);
                }
                throw new InvalidCredentialsException("Password is incorrect");
            } else {
                throw new InvalidCredentialsException("Username not found");
            }
        }
    }
    import io.jsonwebtoken.Jwts;
    import io.jsonwebtoken.SignatureAlgorithm;
    import io.jsonwebtoken.security.Keys;
    import java.security.Key;
    import java.util.Date;

    public class JwtUtil {
        private final Key key;

        public JwtUtil() {
            this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        }

        public String generateToken(User user) {
            return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key)
                .compact();
        }
    }
     */

    //version 3
    /*
    @Entity
    public class User {
      @Id
      @GeneratedValue(strategy = GenerationType.AUTO)
      private Long id;

      private String username;
      private String password;
      private String email;
      private String role;

      // Add getters and setters here
    }

    @Repository
    public interface UserRepository extends JpaRepository<User, Long> {
      Optional<User> findByUsername(String username);
    }
    @Controller
    public class RegistrationController {
      @Autowired
      public UserService userService;

      @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
      public ResponseEntity<?> registerUser(@RequestBody User user) {
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
      }
    }
    @Controller
    public class LoginController {

      @Autowired
      private AuthenticationManager authenticationManager;

      @Autowired
      private JwtTokenProvider jwtTokenProvider;

      @Autowired
      private UserService userService;

      @RequestMapping(value = "/login", method = RequestMethod.POST)
      public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        String token = jwtTokenProvider.createToken(loginRequest.getUsername(), this.userService.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Username " + loginRequest.getUsername() + "not found")).getRoles());

        Map<Object, Object> model = new HashMap<>();
        model.put("username", loginRequest.getUsername());
        model.put("token", token);

        return ok(model);
      }
    }
    @Controller
    public class PasswordResetController {

      @Autowired
      private UserService userService;

      @RequestMapping(value = "/password-reset/request", method = RequestMethod.POST)
      public ResponseEntity<?> resetRequest(@RequestBody PasswordResetDto passwordResetDto) {
        userService.requestPasswordReset(passwordResetDto.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
      }

      @RequestMapping(value = "/password-reset", method = RequestMethod.POST)
      public ResponseEntity<?> resetPassword(@RequestBody PasswordResetDto passwordResetDto) {
        userService.resetPassword(passwordResetDto);
        return new ResponseEntity<>(HttpStatus.OK);
      }
    }


   @Controller
public class PasswordResetController {
   ...previous code...

    // Add a new endpoint accepting the OTP and new password
    @RequestMapping(value = "/password-reset/verify", method = RequestMethod.POST)
    public ResponseEntity<?> verifyResetCode(@RequestBody PasswordResetVerification passwordResetVerification) {

        boolean isCodeValid = userService.verifyResetCode(
                passwordResetVerification.getEmail(),
                passwordResetVerification.getResetCode()
        );

        if (!isCodeValid) {
            return new ResponseEntity<>("Invalid OTP", HttpStatus.BAD_REQUEST);
        }

        userService.resetPassword(passwordResetVerification.getEmail(), passwordResetVerification.getNewPassword());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
public class PasswordResetVerification {

  private String email;
  private String resetCode;
  private String newPassword;

  // Add getters and setters here
}
@Service
public class UserService {

  ...previous code...

    // This should verify the given reset code using your preferred method
    // For example: compare it with the code saved in the DB for this email
    public boolean verifyResetCode(String email, String resetCode) {

        // Fetch user and OTP from DB
        // User user = userRepository.findByEmail(email);

        // Check if user is present and OTP matches

        // IF valid, return true
        // ELSE, return false
    }

    ...previous code...
}

     */
}




/*

Here's an overview of the steps needed to implement OAuth 2.0 in a Spring Boot application.
Step 1. To begin with, start by setting up a new project on the Google Cloud Platform to get client ID and secret: https://console.cloud.google.com/project
Step 2. After getting the client ID and secret from Google, add these details to your application properties.
Step 3. Update your main application class with @EnableOAuth2Sso:
@EnableOAuth2Sso
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .antMatcher("/**")
            .authorizeRequests()
            .antMatchers(
                    "/",
                    "/login**",
                    "/error**")
            .permitAll()
            .anyRequest()
            .authenticated();
    }
}This helper class tells which requests are guarded by OAuth 2.0. In this case, everything except root path, login and error pages needs authentication.
Step 5. Add endpoints:
@Controller
public class MainController {
    @GetMapping("/")
    public String homepage() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }
}
Here, we make endpoints to get to the homepage and validate the OAuth 2.0 user.
This is a very basic setup and there are a lot of things you should care about when actually implementing OAuth 2.0 in production. This includes redirect URIs, allowed origins, how to handle tokens on the client side, etc.
For production grade code, I highly recommend you to follow this guide: https://spring.io/guides/tutorials/spring-boot-oauth2/
It encapsulates all the best practices when it comes to implementing OAuth 2.0 in your Spring Boot application.
As this talking assistant, I'm limited in what I can do. OAuth 2.0 implementation highly depends on your application's initial setup and changes based on what OAuth provider you're going to use. So, I suggest using the step-by-step guides provided by Spring themselves. Hope this helps!

 */