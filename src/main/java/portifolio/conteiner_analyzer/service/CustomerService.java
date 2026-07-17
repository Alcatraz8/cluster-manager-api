package portifolio.conteiner_analyzer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portifolio.conteiner_analyzer.DTO.request.CustomerRequestDTO;
import portifolio.conteiner_analyzer.DTO.response.CustomerResponseDTO;
import portifolio.conteiner_analyzer.entities.Customer;
import portifolio.conteiner_analyzer.entities.User;
import portifolio.conteiner_analyzer.repository.CustomerRepository;
import portifolio.conteiner_analyzer.repository.UserRepository;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public CustomerRepository repository;

    public CustomerResponseDTO createCustomer(CustomerRequestDTO dto) {

        User user = userRepository.findById(dto.userId()).orElseThrow(() ->
                new RuntimeException("User not found"));
        if (user.getCustomer() != null) {
            throw new RuntimeException("user already have a customer");
        }

        Customer customer = new Customer();
        customer.setName(dto.name());
        customer.setCompany(dto.company());
        customer.setEmail(dto.email());
        customer.setUser(user);

        user.setCustomer(customer);

        Customer savedCustomer = repository.save(customer);

        return new CustomerResponseDTO(
                savedCustomer.getId(),
                savedCustomer.getName(),
                savedCustomer.getCompany(),
                savedCustomer.getEmail(),
                savedCustomer.getUser().getId()
        );

    }

    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO dto){

        Customer customer = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setName(dto.name());
        customer.setEmail(dto.email());
        customer.setCompany(dto.company());

        repository.save(customer);

        return new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getCompany(),
                customer.getEmail(),
                customer.getUser().getId()
        );

    }

    public void deleteCustomer(Long id) {

        Customer customer = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        User user = customer.getUser();

        if (user != null) {
            user.setCustomer(null);
            customer.setUser(null);
        }

        repository.delete(customer);
    }

    public List<CustomerResponseDTO> findAll(){
        return repository.findAll().stream()
                .map(customer -> new CustomerResponseDTO(
                        customer.getId(),
                        customer.getName(),
                        customer.getCompany(),
                        customer.getEmail(),
                        customer.getUser().getId()
                )).toList();
    }

    public CustomerResponseDTO findById(Long id){
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getCompany(),
                customer.getEmail(),
                customer.getUser().getId()
        );
    }
}
