package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PetService {
    @Autowired
    PetRepository petRepository;


    public Pet getPetById(Long petId) {
        return petRepository.findById(petId).orElse(null);

    }

    public Pet savePetDb(Pet pet, Customer customer) {
        pet.setCustomer(customer);
        Pet newPet = petRepository.save(pet);
        return newPet;
    }
    public List<Pet> getAllPets(){
        return petRepository.findAll();
    }

    public List<Pet> getAllPetsByOwner(Long ownerId) {
        List<Pet> allPetsByOwner = new ArrayList<>();
        for(Pet p: getAllPets()){
            if(p.getCustomer().getId() == ownerId){
                allPetsByOwner.add(p);
            }
        }
        return allPetsByOwner;

    }
}


