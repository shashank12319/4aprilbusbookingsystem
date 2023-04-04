package com.wittybrains.busbookingsystem.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wittybrains.busbookingsystem.dto.PassengerDTO;
import com.wittybrains.busbookingsystem.model.Passenger;
import com.wittybrains.busbookingsystem.service.PassengerService;

@RestController
@RequestMapping("/passengers")
public class PassengerController {

    private final Logger logger = LoggerFactory.getLogger(PassengerController.class);

    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @PostMapping
    public ResponseEntity<Passenger> createPassenger(@RequestBody PassengerDTO passengerDTO) {
        Passenger passenger = passengerService.createPassenger(passengerDTO);
        logger.info("Created passenger with ID: {}", passenger.getPassengerId());
        return ResponseEntity.status(HttpStatus.CREATED).body(passenger);
    }
    
    @GetMapping("/{passengerId}")
    public ResponseEntity<Passenger> getPassengerById(@PathVariable Long passengerId) {
        Optional<Passenger> optionalPassenger = passengerService.getPassengerById(passengerId);
        if (optionalPassenger.isPresent()) {
            Passenger passenger = optionalPassenger.get();
            logger.info("Retrieved passenger with ID: {}", passengerId);
            return ResponseEntity.ok(passenger);
        } else {
            logger.warn("Failed to retrieve passenger with ID: {}", passengerId);
            return ResponseEntity.notFound().build();
        }
    }



    
    @PutMapping("/{passengerId}")
    public ResponseEntity<Passenger> updatePassenger(@PathVariable Long passengerId, @RequestBody PassengerDTO passengerDTO) {
        passengerService.updatePassenger(passengerId, passengerDTO);
        logger.info("Updated passenger with ID: {}", passengerId);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{passengerId}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long passengerId) {
        Optional<Passenger> optionalPassenger = passengerService.getPassengerById(passengerId);
        if (optionalPassenger.isPresent()) {
            passengerService.deletePassenger(passengerId);
            logger.info("Deleted passenger with ID: {}", passengerId);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Failed to delete passenger with ID: {}", passengerId);
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Passenger>> getPassengers() {
        List<Passenger> passengers = passengerService.getAllPassengers();
        return ResponseEntity.ok(passengers);
    }

}

    
    
