package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetController;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.pet.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    private CustomerDTO convertCustomerToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        List<Long> petIDs = new ArrayList<>();
        if (customer.getPets() != null){
            for (Pet p : customer.getPets()) {
                petIDs.add(p.getId());
            }
        customerDTO.setPetIds(petIDs);
        }
        return customerDTO;
    }
    private Customer convertCustomerDTOToCustomer(CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }
    private EmployeeDTO convertEmployeeToEmployeeDTO(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        employeeDTO.setSkills(employee.getSkills());
        return employeeDTO;
    }
    private Employee convertEmployeeDTOToEmployee(EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employee;
    }
    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        return convertCustomerToCustomerDTO(userService.saveCustomerDb(convertCustomerDTOToCustomer(customerDTO)));

    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<CustomerDTO> modifiedCustomerList = new ArrayList<>();
        for(Customer c : userService.getAllCustomersList()){
            modifiedCustomerList.add(convertCustomerToCustomerDTO(c));
        }
        return modifiedCustomerList;

    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Pet pet = petService.getPetById(petId);
        Customer owner = userService.getCustomerById(pet.getCustomer().getId());
        return convertCustomerToCustomerDTO(owner);

    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Set<EmployeeSkill> employeeSkills = employeeDTO.getSkills();
        Employee savedEmployee =userService.saveEmployeeDb(convertEmployeeDTOToEmployee(employeeDTO),employeeSkills);
        EmployeeDTO savedEmployeeDTO= convertEmployeeToEmployeeDTO(savedEmployee);
        return savedEmployeeDTO;
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        return convertEmployeeToEmployeeDTO(userService.getEmployeeById(employeeId));

    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        Employee employee = userService.updateEmployeeSchedule(employeeId, daysAvailable);
    }



    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
       Set<Employee> availableEmployees = userService.findAvailableEmployees(employeeDTO.getSkills(), employeeDTO.getDate());
       List<EmployeeDTO> modifiedEmployeeList = new ArrayList<>();
       for(Employee e : availableEmployees){
           modifiedEmployeeList.add(convertEmployeeToEmployeeDTO(e));
        }
        return modifiedEmployeeList;

    }

}
