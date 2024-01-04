package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.ProfileDao;
import org.yearup.models.Profile;

@RestController
@RequestMapping("/profiles")
public class ProfilesController {

    private ProfileDao profileDao;

    @Autowired
    public ProfilesController(ProfileDao profileDao) {
        this.profileDao = profileDao;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Profile> getProfileById(@PathVariable int userId) {
        try {
            Profile profile = profileDao.getProfileById(userId);
            if (profile != null) {
                return new ResponseEntity<>(profile, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Void> createProfile(@RequestBody Profile profile) {
        try {
            profileDao.saveOrUpdate(profile);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateProfile(@PathVariable int userId, @RequestBody Profile updatedProfile) {
        try {
            Profile existingProfile = profileDao.getProfileById(userId);
            if (existingProfile != null) {
                // Update existingProfile with values from updatedProfile
                existingProfile.setFirstName(updatedProfile.getFirstName());
                existingProfile.setLastName(updatedProfile.getLastName());
                // Update other fields accordingly

                profileDao.saveOrUpdate(existingProfile);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteProfile(@PathVariable int userId) {
        try {
            Profile profile = profileDao.getProfileById(userId);
            if (profile != null) {
                profileDao.delete(profile);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

