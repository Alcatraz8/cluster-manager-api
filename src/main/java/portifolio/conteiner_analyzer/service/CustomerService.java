package portifolio.conteiner_analyzer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portifolio.conteiner_analyzer.entities.Customer;
import portifolio.conteiner_analyzer.entities.User;
import portifolio.conteiner_analyzer.repository.CustomerRepository;
import portifolio.conteiner_analyzer.repository.UserRepository;

@Service
public class CustomerService {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public CustomerRepository repository;

    public Customer createCustomer(Customer customer) {

        User user = userRepository.findById(customer.getUser().getId()).orElseThrow(() ->
                new RuntimeException("User not found"));
        if (user.getCustomer() != null) {
            throw new RuntimeException("user already have a customer");
        }

        return repository.save(customer);
    }
}
