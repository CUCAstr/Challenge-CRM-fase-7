package br.com.savedra.challengebackend.controller;

import br.com.savedra.challengebackend.model.User;
import br.com.savedra.challengebackend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getEveryUser() {
        return ResponseEntity.ok(customerService.getEveryUser());
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllCustomers(
            @RequestParam(required = false) String segment
    ) {
        if (segment != null) {
            return ResponseEntity.ok(customerService.getCustomersBySegment(segment));
        }
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getCustomerById(@PathVariable String id) {
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createCustomer(@RequestBody User user) {
        return ResponseEntity.ok(customerService.saveCustomer(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateCustomer(@PathVariable String id, @RequestBody User user) {
        System.out.println("Receiving update request for customer ID: " + id);
        System.out.println("New Notes: " + user.getNotes());
        return ResponseEntity.ok(customerService.updateCustomer(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/timeline")
    public ResponseEntity<User> getCustomerTimeline(@PathVariable String id) {
        // For Sprint 2, Perfil 360 includes basic data, last messages, campaigns, tasks.
        // We will enrich this as we add those modules.
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
