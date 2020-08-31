package com.beia_consult_international.solomon.controller;

import com.beia_consult_international.solomon.dto.*;
import com.beia_consult_international.solomon.exception.WrongUserDetailsException;
import com.beia_consult_international.solomon.model.*;
import com.beia_consult_international.solomon.service.BeaconService;
import com.beia_consult_international.solomon.service.CampaignService;
import com.beia_consult_international.solomon.service.MallService;
import com.beia_consult_international.solomon.service.UserService;
import com.beia_consult_international.solomon.service.mapper.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final MallService mallService;
    private final BeaconService beaconService;
    private final CampaignService campaignService;
    @Value("${solomon.campaignsPicturesPath}")
    public String campaignsPath;

    public UserController(UserService userService, MallService mallService, BeaconService beaconService, CampaignService campaignService) {
        this.userService = userService;
        this.mallService = mallService;
        this.beaconService = beaconService;
        this.campaignService = campaignService;
    }

    @GetMapping("/login")
    public UserDto login(@RequestParam String username, @RequestParam String password) {
        if(!userService.validUserDetails(username, password))
            throw new WrongUserDetailsException();
        return UserMapper.mapToDto(userService.findUserByUsername(username));
    }

    @PostMapping("/signUp")
    public Boolean signUp(@RequestBody UserDto userDto, @RequestParam String password) {
        User user = UserMapper.mapToModel(userDto);
        if(userService.userExists(user)) {
            return false;
        }
        userService.save(user, password);
        return true;
    }

    @GetMapping("/getMalls")
    public List<MallDto> getMalls() {
        System.out.println("GET MALLS");
        return mallService.findAll()
                .stream()
                .map(MallMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/getBeacons")
    public List<BeaconDto> getBeacons() {
        System.out.println("GET BEACONS");
        return beaconService.findAll()
                .stream()
                .map(BeaconMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/postBeaconTime")
    public void saveBeaconTime(@RequestParam long user_id, @RequestParam long beacon_id, @RequestBody long seconds) {
        User user = userService.findById(user_id);
        Beacon beacon = beaconService.findById(beacon_id);
        Optional<UserBeaconTime> optionalUserBeaconTime = beaconService.findUserBeaconTime(user, beacon);
        if(optionalUserBeaconTime.isPresent()) {
            UserBeaconTime userBeaconTime = optionalUserBeaconTime.get();
            userBeaconTime.setSeconds(userBeaconTime.getSeconds() + seconds);
            beaconService.saveUserBeaconTime(userBeaconTime);
        }
        else {
            beaconService.saveUserBeaconTime(UserBeaconTime
                    .builder()
                    .user(user)
                    .beacon(beacon)
                    .seconds(seconds)
                    .build());
        }
    }

    @PostMapping("/computeLocation/{userId}")
    public LocationDto computeLocation(@RequestBody BeaconLocalizationData beaconLocalizationData,
                                       @PathVariable long userId) {
        System.out.println("COMPUTE LOCATION");
        Location location = beaconService.computeLocation(beaconLocalizationData);
        location.setUser(userService.findById(userId));
        beaconService.saveLocation(location);
        return LocationMapper.mapToDto(location);
    }

    @GetMapping("/getCampaigns")
    public List<CampaignDto> getCampaignsFromUser(@RequestParam(name = "companyId") long userId) throws IOException {
        User user = userService.findById(userId);
        return campaignService
                .findCampaignsByUser(user)
                .stream()
                .filter(campaign -> campaign.getEndDate().compareTo(LocalDateTime.now()) >= 0
                        && campaign.getStartDate().compareTo(LocalDateTime.now()) <= 0)
                .map(campaign -> CampaignMapper.mapToDto(campaign, campaignsPath))
                .collect(Collectors.toList());
    }

    @PostMapping("/saveCampaign")
    public void getCampaignsFromUser(@RequestBody CampaignDto campaignDto) throws IOException {
        campaignService.savePicture(campaignDto.getImage(), campaignsPath, campaignDto.getId());
        campaignService.save(CampaignMapper.mapToModel(campaignDto));
    }
}