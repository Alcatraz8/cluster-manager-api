package portifolio.conteiner_analyzer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import portifolio.conteiner_analyzer.DTO.admin.AdminUserRequestDTO;
import portifolio.conteiner_analyzer.DTO.request.UserRequestDTO;
import portifolio.conteiner_analyzer.DTO.response.LoginResponseDTO;
import portifolio.conteiner_analyzer.DTO.response.UserResponseDTO;
import portifolio.conteiner_analyzer.entities.Customer;
import portifolio.conteiner_analyzer.entities.Role;
import portifolio.conteiner_analyzer.entities.User;
import portifolio.conteiner_analyzer.exception.InvalidAuthenticationException;
import portifolio.conteiner_analyzer.exception.ResourceAlreadyExistsException;
import portifolio.conteiner_analyzer.exception.ResourceNotFoundException;
import portifolio.conteiner_analyzer.repository.CustomerRepository;
import portifolio.conteiner_analyzer.repository.UserRepository;
import portifolio.conteiner_analyzer.security.TokenService;
import portifolio.conteiner_analyzer.service.authentication.AuthenticatedUserService;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticatedUserService authenticatedUserService;

    @Autowired
    private TokenService tokenService;

    public UserResponseDTO signup(UserRequestDTO dto) {

        if (repository.existsByLogin(dto.login())) {
            throw new ResourceAlreadyExistsException("User name already exists");
        }

        User user = new User();
        user.setLogin(dto.login());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(Role.CLIENT);

        User savedUser = repository.save(user);

        return toResponseDTO(savedUser);
    }

    public LoginResponseDTO signIn(UserRequestDTO dto) {

        UsernamePasswordAuthenticationToken credentials =
                new UsernamePasswordAuthenticationToken(
                        dto.login(),
                        dto.password()
                );

        Authentication authentication =
                authenticationManager.authenticate(credentials);

        User user = (User) authentication.getPrincipal();

        String token = tokenService.generateToken(user);

        return new LoginResponseDTO(token);
    }

    public UserResponseDTO UpdateUser(Long id, AdminUserRequestDTO dto) {

        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (repository.existsByLogin(dto.login())
                && !user.getLogin().equals(dto.login())) {
            throw new ResourceAlreadyExistsException("User name already exists");
        }

        user.setLogin(dto.login());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(dto.role());

        User updatedUser = repository.save(user);

        return toResponseDTO(updatedUser);
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {

        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (repository.existsByLogin(dto.login())
                && !user.getLogin().equals(dto.login())) {
            throw new ResourceAlreadyExistsException("User name already exists");
        }

        user.setLogin(dto.login());
        user.setPassword(passwordEncoder.encode(dto.password()));

        User updatedUser = repository.save(user);

        return toResponseDTO(updatedUser);
    }

    public void deleteUser(Long id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Customer customer = user.getCustomer();

        if (customer != null) {
            customer.setUser(null);
            user.setCustomer(null);

            customerRepository.delete(customer);
        }

        repository.delete(user);
    }

    public List<UserResponseDTO> findAll() {

        return repository.findAll()
                .stream()
                .map(this::toResponseDTO).toList();
    }

    public UserResponseDTO findById(Long id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toResponseDTO(user);
    }


    private UserResponseDTO toResponseDTO(User user){

        return new UserResponseDTO(
                user.getId(),
                user.getLogin(),
                user.getRole());

    }

}


