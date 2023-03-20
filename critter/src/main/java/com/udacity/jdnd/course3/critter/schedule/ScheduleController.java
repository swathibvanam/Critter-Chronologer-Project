package com.udacity.jdnd.course3.critter.schedule;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import com.udacity.jdnd.course3.critter.user.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private UserService userService;


    @Autowired
    private PetService petService;
    private ScheduleDTO convertScheduleToScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);
        List<Long> petIDs = new ArrayList<>();
        if (schedule.getPets() != null){
            for (Pet p :schedule.getPets()) {
                petIDs.add(p.getId());
            }
            scheduleDTO.setPetIds(petIDs);
        }
        List<Long> employeeIDs = new ArrayList<>();
        if (schedule.getEmployees() != null){
            for (Employee e :schedule.getEmployees()) {
                employeeIDs.add(e.getId());
            }
            scheduleDTO.setEmployeeIds(employeeIDs);
        }
        return scheduleDTO;
    }
    private Schedule convertScheduleDTOToSchedule(ScheduleDTO scheduleDTO){
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);
        return schedule;
    }

    @PostMapping
     public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        List<Long> petList = scheduleDTO.getPetIds();
        List<Pet>  petsInSchedule = new ArrayList<>();
        List<Customer>  customersInSchedule = new ArrayList<>();
        for(Long petId : petList){
            Pet pet = petService.getPetById(petId);
            petsInSchedule.add(pet);
            customersInSchedule.add(pet.getCustomer());
        }
        List<Long> employeeList = scheduleDTO.getEmployeeIds();
        List<Employee>  employeesInSchedule = new ArrayList<>();
        for(Long employeeId : employeeList){
            employeesInSchedule.add(userService.getEmployeeById(employeeId));
        }

        Schedule schedule = scheduleService.createASchedule(convertScheduleDTOToSchedule(scheduleDTO), petsInSchedule, employeesInSchedule);

        for(Customer customer:customersInSchedule){
           userService.updateCustomerSchedule(customer, schedule);
        }

        return convertScheduleToScheduleDTO(schedule) ;

    }


    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {

        List<ScheduleDTO> modifiedScheduleList = new ArrayList<>();
        for(Schedule s : scheduleService.getAllSchedulesList()){
            modifiedScheduleList.add(convertScheduleToScheduleDTO(s));
        }
        return modifiedScheduleList;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<ScheduleDTO> modifiedScheduleListForPet = new ArrayList<>();
        for(Schedule s : scheduleService.getAllSchedulesForPet(petId)){
            modifiedScheduleListForPet.add(convertScheduleToScheduleDTO(s));
        }
        return modifiedScheduleListForPet;

    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<ScheduleDTO> modifiedScheduleListForEmployee = new ArrayList<>();
        for(Schedule s : scheduleService.getAllSchedulesForEmployee(employeeId)){
            modifiedScheduleListForEmployee.add(convertScheduleToScheduleDTO(s));
        }
        return modifiedScheduleListForEmployee;

    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<ScheduleDTO> modifiedScheduleListForCustomer = new ArrayList<>();
        for(Schedule s : scheduleService.getAllSchedulesForCustomer(customerId)){
            modifiedScheduleListForCustomer.add(convertScheduleToScheduleDTO(s));
        }
        return modifiedScheduleListForCustomer;

    }
}
