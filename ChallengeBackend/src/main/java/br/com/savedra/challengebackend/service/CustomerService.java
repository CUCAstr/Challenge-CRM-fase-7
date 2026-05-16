package br.com.savedra.challengebackend.service;

import br.com.savedra.challengebackend.model.User;
import br.com.savedra.challengebackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final UserRepository userRepository;

    public List<User> getAllCustomers() {
        return userRepository.findByRole("CLIENT");
    }

    public List<User> getCustomersBySegment(String segment) {
        return userRepository.findByRoleAndSegment("CLIENT", segment);
    }

    public Optional<User> getCustomerById(String id) {
        return userRepository.findById(id);
    }

    public User saveCustomer(User user) {
        user.setRole("CLIENT");
        return userRepository.save(user);
    }

    public void deleteCustomer(String id) {
        userRepository.deleteById(id);
    }

    public User updateCustomer(String id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow();
        user.setName(userDetails.getName());
        user.setCompany(userDetails.getCompany());
        user.setSegment(userDetails.getSegment());
        user.setScore(userDetails.getScore());
        user.setStatus(userDetails.getStatus());
        user.setTags(userDetails.getTags());
        user.setNotes(userDetails.getNotes());
        user.setPhone(userDetails.getPhone());
        user.setCategory(userDetails.getCategory());
        return userRepository.save(user);
    }
}
