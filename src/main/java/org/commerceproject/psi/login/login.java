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




/*
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String password;

    // getters and setters
}@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String name);
}@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    // code for saving user etc.
}@Component
public class JwtUtil {

    private String SECRET_KEY = "secret";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}@RestController
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        User user = userService.findByUsername(authenticationRequest.getUsername());

        if(user != null && authenticationRequest.getPassword().equals(user.getPassword())) {
            final String jwt = jwtTokenUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        } else {
            throw new BadCredentialsException("Incorrect username or password");
        }
    }
}@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authService;

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public String authenticateUser(String username, String password) throws Exception {
        User user = findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            return authService.generateToken(user);
        } else {
            throw new FailedAuthenticationException("Incorrect Username or Password");
        }
    }

}@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authService;

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public String authenticateUser(String username, String password) throws Exception {
        User user = findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            return authService.generateToken(user);
        } else {
            throw new FailedAuthenticationException("Incorrect Username or Password");
        }
    }

}@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthTokenDTO> authenticateUser(@RequestBody LoginDTO loginData) {
        try {
            String token = userService.authenticateUser(loginData.getUsername(), loginData.getPassword());

            AuthTokenDTO response = new AuthTokenDTO();
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (FailedAuthenticationException ex) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}public class LoginDTO {
    private String username;
    private String password;

    // getters and setters
}

public class AuthTokenDTO {
    private String token;

    // getters and setters
}@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers("/authenticate").permitAll()
                .anyRequest().authenticated()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
 */