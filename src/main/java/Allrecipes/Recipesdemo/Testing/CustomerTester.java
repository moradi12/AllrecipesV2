package Allrecipes.Recipesdemo.Testing;

import Allrecipes.Recipesdemo.Entities.User;
import Allrecipes.Recipesdemo.Exceptions.*;
import Allrecipes.Recipesdemo.Response.UserResponse;
import Allrecipes.Recipesdemo.Service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
@Order(7) // Ensure this runs after AdminTester which has @Order(6)
public class CustomerTester implements CommandLineRunner {

    private final CustomerService customerService;

    // Store User objects to access their IDs later
    private User customer1;
    private User customer2;
    private User customer3;
    private User customer4;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n!!!!!!!!!!!!Customer Tester Testing!!!!!!!!!!!!!!\n");

        try {
            addCustomers();
            printAllCustomers();
            updateCustomer();
            printAllCustomers();
            deleteUser(); // Delete Customer 3 by ID
            printAllCustomers();
            // Optionally, delete other customers as needed
        } catch (Exception e) {
            System.out.println("An unexpected error occurred during CustomerTester: " + e.getMessage());
            // Optionally, handle the exception further if needed
        }
    }

    /**
     * Adds multiple customers to the system.
     */
    private void addCustomers() {
        try {
            System.out.println("Adding new customers...");

            // Add Customer 1
            customer1 = customerService.registerUser("johnDoe", "john.doe@example.com", "password123");
            System.out.println("Added Customer 1: " + customer1);

            // Add Customer 2
            customer2 = customerService.registerUser("janeSmith", "jane.smith@example.com", "securePass456");
            System.out.println("Added Customer 2: " + customer2);

            // Add Customer 3 with unique email
            customer3 = customerService.registerUser("johnDuplicate", "john.duplicate@example.com", "anotherPass789");
            System.out.println("Added Customer 3: " + customer3);

            // Add Customer 4
            customer4 = customerService.registerUser("David", "david.david@example.com", "David1212");
            System.out.println("Added Customer 4: " + customer4);

        } catch (UsernameAlreadyTakenException | EmailAlreadyTakenException | IllegalArgumentException e) {
            System.out.println("Error adding customer: " + e.getMessage());
            // Handle specific exceptions if needed
        }
    }

    /**
     * Prints all customers in the system using DTOs.
     */
    private void printAllCustomers() {
        System.out.println("\nFetching all customers:");
        List<UserResponse> customers = customerService.getAllUserResponses();
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            customers.forEach(customer -> System.out.println("Customer: " + customer));
        }
    }

    /**
     * Updates the details of a specific customer.
     * Updates Customer 1 (johnDoe) to johnUpdated.
     */
    private void updateCustomer() {
        try {
            System.out.println("\nUpdating Customer 1 (johnDoe)...");
            // Retrieve Customer 1 by username to get the actual ID
            User retrievedCustomer1 = customerService.findByUsernameOrEmail("johnDoe")
                    .orElseThrow(() -> new UserNotFoundException("Customer 'johnDoe' not found."));
            System.out.println("Retrieved Customer 1: " + retrievedCustomer1);

            // Perform the update
            customerService.updateUser(retrievedCustomer1.getId(), "johnUpdated", "john.updated@example.com", "newPassword123");
            System.out.println("Customer 1 updated successfully.");

            // Fetch the updated customer to verify changes
            User updatedCustomer1 = customerService.findById(retrievedCustomer1.getId())
                    .orElseThrow(() -> new UserNotFoundException("Updated Customer 'johnUpdated' not found."));
            System.out.println("Updated Customer 1: " + updatedCustomer1);

        } catch (UserNotFoundException | UsernameAlreadyTakenException | EmailAlreadyTakenException | IllegalArgumentException e) {
            System.out.println("Error updating customer: " + e.getMessage());
            // Handle specific exceptions if needed
        }
    }

    /**
     * Deletes Customer 3 by ID.
     */
    private void deleteUser() {
        try {
            System.out.println("\nDeleting Customer 3 (johnDuplicate) by ID...");
            if (customer3 == null) {
                System.out.println("Customer 3 has not been added. Please ensure customers are added correctly.");
                return;
            }
            Long customer3Id = customer3.getId();
            System.out.println("Customer 3 ID: " + customer3Id);

            // Perform the deletion
            customerService.deleteUser(customer3Id);
            System.out.println("Customer 3 deleted successfully.");

        } catch (UserNotFoundException e) {
            System.out.println("Error deleting Customer 3 by ID: " + e.getMessage());
            // Handle specific exceptions if needed
        }
    }

    // Optionally, implement additional deletion methods if needed
    /*
    private void deleteCustomerByUsername() {
        try {
            System.out.println("\nDeleting Customer 2 (janeSmith) by Username...");
            // Retrieve Customer 2 by username
            User retrievedCustomer2 = customerService.findByUsernameOrEmail("janeSmith")
                    .orElseThrow(() -> new UserNotFoundException("Customer 'janeSmith' not found."));
            System.out.println("Retrieved Customer 2: " + retrievedCustomer2);

            // Perform the deletion
            customerService.deleteUser(retrievedCustomer2.getId());
            System.out.println("Customer 2 deleted successfully.");

        } catch (UserNotFoundException e) {
            System.out.println("Error deleting Customer 2 by Username: " + e.getMessage());
            // Handle specific exceptions if needed
        }
    }
    */
}
