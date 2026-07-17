package portifolio.conteiner_analyzer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import portifolio.conteiner_analyzer.DTO.request.UserRequestDTO;
import portifolio.conteiner_analyzer.DTO.response.UserResponseDTO;
import portifolio.conteiner_analyzer.entities.Customer;
import portifolio.conteiner_analyzer.entities.User;
import portifolio.conteiner_analyzer.repository.CustomerRepository;
import portifolio.conteiner_analyzer.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO signup(UserRequestDTO dto) {
        if (repository.existsByLogin(dto.login())) {
            throw new RuntimeException("User name already exists");
        }

        User user = new User();
        user.setLogin(dto.login());
        user.setPassword(passwordEncoder.encode(dto.password()));

        User savedUser = repository.save(user);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getLogin()
        );
    }

    public UserResponseDTO signIn(UserRequestDTO dto) {
        User user = repository.findByLogin(dto.login())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new RuntimeException("Invalid login or password");
        }

        return new UserResponseDTO(
                user.getId(),
                user.getLogin()
        );
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {

        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (repository.existsByLogin(dto.login())
                && !user.getLogin().equals(dto.login())) {
            throw new RuntimeException("User name already exists");
        }

        user.setLogin(dto.login());
        user.setPassword(passwordEncoder.encode(dto.password()));

        repository.save(user);

        return new UserResponseDTO(
                user.getId(),
                user.getLogin()
        );
    }

    public void deleteUser(Long id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

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
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getLogin())).toList();
    }

    public UserResponseDTO findById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserResponseDTO(
                user.getId(),
                user.getLogin());
    }

}


