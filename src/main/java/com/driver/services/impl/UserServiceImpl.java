package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{

        User user=new User();
        user.setPassword(password);
        user.setUsername(username);
        user.setConnected(false);
        user.setMaskedIp(null);

        Country country=new Country();
        if(!countryName.equalsIgnoreCase("ind") && !countryName.equalsIgnoreCase("aus") && !countryName.equalsIgnoreCase("usa")
                && !countryName.equalsIgnoreCase("chi") && !countryName.equalsIgnoreCase("jpn")){
            throw new Exception("Country not found");
        }

        CountryName countryName1=null;
        String countryCode=null;
        if(countryName.equalsIgnoreCase("ind")){
            countryName1=CountryName.IND;
            countryCode=CountryName.IND.toCode();
        }else if(countryName.equalsIgnoreCase("aus")){
            countryName1=CountryName.AUS;
            countryCode=CountryName.AUS.toCode();
        }else if(countryName.equalsIgnoreCase("usa")){
            countryName1=CountryName.USA;
            countryCode=CountryName.USA.toCode();
        }else if(countryName.equalsIgnoreCase("chi")){
            countryName1=CountryName.CHI;
            countryCode=CountryName.CHI.toCode();
        }else{
            countryName1=CountryName.JPN;
            countryCode=CountryName.JPN.toCode();
        }


        country.setCountryName(countryName1);
        country.setUser(user);
        country.setCode(countryCode);
        country.setServiceProvider(null);

        user.setOriginalCountry(country);
        user=userRepository3.save(user);

        user.setOriginalIp(new String(countryCode+"."+user.getId()));

        userRepository3.save(user);

        return user;
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {

        User user=userRepository3.findById(userId).get();
        ServiceProvider serviceProvider=serviceProviderRepository3.findById(serviceProviderId).get();

        serviceProvider.getUsers().add(user);
        user.getServiceProviderList().add(serviceProvider);

        serviceProviderRepository3.save(serviceProvider);
        return user;
    }
}
