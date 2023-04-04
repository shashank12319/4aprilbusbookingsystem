package com.wittybrains.busbookingsystem.controller;

import java.util.List;

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

import com.wittybrains.busbookingsystem.dto.DriverDTO;
import com.wittybrains.busbookingsystem.model.Driver;
import com.wittybrains.busbookingsystem.service.DriverService;

@RestController
@RequestMapping("/drivers")
public class DriverController {

    private final Logger logger = LoggerFactory.getLogger(DriverController.class);

    
    private DriverService driverService;
    
    public DriverController( DriverService driverService) {
         this.driverService = driverService;
    }

    @PostMapping
    public ResponseEntity<Driver> createDriver(@RequestBody DriverDTO driverDTO) {
        logger.info("Creating driver with DTO={}", driverDTO);
        Driver driver = driverService.createDriver(driverDTO);
        logger.info("Successfully created driver with id={}", driver.getDriverId());
        return ResponseEntity.status(HttpStatus.CREATED).body(driver);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Driver> getDriverById(@PathVariable Long id) {
        logger.info("Retrieving driver with id={}", id);
        Driver driver = driverService.getDriverById(id);
        logger.info("Found driver with id={}", id);
        return ResponseEntity.ok(driver);
    }

    @GetMapping
    public ResponseEntity<List<Driver>> getAllDrivers() {
        logger.info("Retrieving all drivers");
        List<Driver> drivers = driverService.getAllDrivers();
        logger.info("Found {} drivers", drivers.size());
        return ResponseEntity.ok(drivers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Driver> updateDriver(@PathVariable Long id, @RequestBody DriverDTO driverDTO) {
        logger.info("Updating driver with id={} and DTO={}", id, driverDTO);
        Driver driver = driverService.updateDriver(id, driverDTO);
        logger.info("Successfully updated driver with id={}", driver.getDriverId());
        return ResponseEntity.ok(driver);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        logger.info("Deleting driver with id={}", id);
        driverService.deleteDriver(id);
        logger.info("Successfully deleted driver with id={}", id);
        return ResponseEntity.noContent().build();
    }
}


