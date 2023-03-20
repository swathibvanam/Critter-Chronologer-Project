package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    private PetService petService;

    @Autowired
    private UserService userService;

    private PetDTO convertPetToPetDTO(Pet pet){
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        petDTO.setOwnerId(pet.getCustomer().getId());
        return petDTO;
    }
    private Pet convertPetDTOToPet(PetDTO petDTO){
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        return pet;
    }

    @PostMapping
     public PetDTO savePet(@RequestBody PetDTO petDTO) {

        Customer customer = userService.getCustomerById(petDTO.getOwnerId());
        Pet savedPet = petService.savePetDb(convertPetDTOToPet(petDTO), customer);
        userService.updateCustomerPetList(customer, savedPet);
        PetDTO savedPetDTO= convertPetToPetDTO(savedPet);
        return savedPetDTO;
     }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return convertPetToPetDTO(petService.getPetById(petId));
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<PetDTO> modifiedPetList = new ArrayList<>();
        for(Pet p : petService.getAllPets()){
            modifiedPetList.add(convertPetToPetDTO(p));
        }
        return modifiedPetList;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<PetDTO> modifiedPetListByOwner = new ArrayList<>();
        for(Pet p : petService.getAllPetsByOwner(ownerId)){
            modifiedPetListByOwner.add(convertPetToPetDTO(p));
        }
        return modifiedPetListByOwner;

    }
}
