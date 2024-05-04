package com.driver.services.impl;

import com.driver.model.Admin;
import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository1;

    @Autowired
    ServiceProviderRepository serviceProviderRepository1;

    @Autowired
    CountryRepository countryRepository1;

    @Override
    public Admin register(String username, String password) {

        Admin admin=new Admin();
        admin.setUsername(username);
        admin.setPassword(password);

        adminRepository1.save(admin);
        return admin;
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName) {
          Admin admin=adminRepository1.findById(adminId).get();


          ServiceProvider serviceProvider=new ServiceProvider();
          serviceProvider.setName(providerName);
          serviceProvider.setAdmin(admin);


         List<ServiceProvider> serviceProviderList=admin.getServiceProviders();
         serviceProviderList.add(serviceProvider);
         admin.setServiceProviders(serviceProviderList);


          adminRepository1.save(admin);

          return admin;
    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception{

        ServiceProvider serviceProvider=serviceProviderRepository1.findById(serviceProviderId).get();

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

        Country country=new Country();
        country.setCountryName(countryName1);
        country.setUser(null);
        country.setCode(countryCode);
        country.setServiceProvider(serviceProvider);

        serviceProvider.getCountryList().add(country);

        serviceProvider=serviceProviderRepository1.save(serviceProvider);
//        countryRepository1.save(country);
        return serviceProvider;

    }
}
