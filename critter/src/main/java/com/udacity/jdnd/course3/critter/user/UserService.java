package com.udacity.jdnd.course3.critter.user;


import com.udacity.jdnd.course3.critter.pet.Pet;

import com.udacity.jdnd.course3.critter.schedule.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    public Customer saveCustomerDb(Customer customer){
       return customerRepository.save(customer);
    }
    public List<Customer> getAllCustomersList(){
        return customerRepository.findAll();
    }
    public Customer getCustomerById(Long ownerId) {
        return customerRepository.findById(ownerId).orElse(null);
    }
    public void updateCustomerPetList(Customer customer, Pet p) {
        if (customer.getPets() != null){
            List<Pet> ownerPetList = customer.getPets();
            ownerPetList.add(p);
            customer.setPets(ownerPetList);
        }

        else{
            List<Pet> newPetList = new ArrayList<>();
            newPetList.add(p);
            customer.setPets(newPetList);
        }

    }
    public Employee saveEmployeeDb(Employee employee, Set<EmployeeSkill> employeeSkills){
        employee.setSkills(employeeSkills);
        return employeeRepository.save(employee);


    }
    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElse(null);

    }

    public Employee updateEmployeeSchedule(Long employeeId, Set<DayOfWeek>availableDays){
        Employee employee = getEmployeeById(employeeId);
        employee.setDaysAvailable(availableDays);
        return employeeRepository.save(employee);

    }

    public List<Employee> getAllEmployeeList(){
        return employeeRepository.findAll();
    }
    public Set<Employee> findAvailableEmployees(Set<EmployeeSkill> requiredSkills, LocalDate date){

        Set<Employee> availableEmployees = new HashSet<>();
        List<Employee> allEmployees = getAllEmployeeList();
        for(Employee e : allEmployees){
            boolean isAvailable =true;
            for(EmployeeSkill skill : requiredSkills) {

                 if(!e.getSkills().contains(skill)) {
                     isAvailable=false;
                 }

            }
            if(isAvailable && e.getDaysAvailable().contains(date.getDayOfWeek())){
                availableEmployees.add(e);
            }
        }

        return availableEmployees;
    }

    public void updateCustomerSchedule(Customer customer, Schedule schedule){
        if (customer.getSchedules() != null){
            List<Schedule> customerScheduleList = customer.getSchedules();
            customerScheduleList.add(schedule);
            customer.setSchedules(customerScheduleList);
        }

        else{
            List<Schedule> customerNewScheduleList  = new ArrayList<>();
            customerNewScheduleList.add(schedule);
            customer.setSchedules(customerNewScheduleList);
        }
    }

}
