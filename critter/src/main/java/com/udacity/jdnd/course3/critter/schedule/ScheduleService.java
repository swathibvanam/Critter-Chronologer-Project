package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;

    public Schedule createASchedule(Schedule schedule, List<Pet> petsInSchedule,
                                    List<Employee> employeesInSchedule) {

        schedule.setEmployees(employeesInSchedule);
        schedule.setPets(petsInSchedule);
        Schedule newSchedule = scheduleRepository.save(schedule);
        return newSchedule;

    }

    public List<Schedule> getAllSchedulesList(){
        return scheduleRepository.findAll();
    }

    public List<Schedule> getAllSchedulesForPet(Long petId) {
        List<Schedule> allSchedulesForPet = new ArrayList<>();
        for(Schedule s: getAllSchedulesList()){
            for(Pet p: s.getPets()){
                if(p.getId() == petId) {
                    allSchedulesForPet.add(s);
                }
            }
        }
        return allSchedulesForPet;

    }
    public List<Schedule> getAllSchedulesForEmployee(Long employeeId) {
        List<Schedule> allSchedulesForEmployee = new ArrayList<>();
        for(Schedule s: getAllSchedulesList()){
            for(Employee e: s.getEmployees()){
                if(e.getId() == employeeId) {
                    allSchedulesForEmployee.add(s);
                }
            }
        }
        return allSchedulesForEmployee;

    }

    public List<Schedule> getAllSchedulesForCustomer(Long customerId) {
        List<Schedule> allSchedulesForCustomer = new ArrayList<>();
        for(Schedule s: getAllSchedulesList()){
            for(Pet p: s.getPets()){
                if(p.getCustomer().getId() == customerId) {
                    allSchedulesForCustomer.add(s);
                }
            }
        }
        return allSchedulesForCustomer;

    }
}
