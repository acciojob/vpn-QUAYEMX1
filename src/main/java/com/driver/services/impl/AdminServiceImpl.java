package com.driver.services.impl;

import com.driver.model.Admin;
import com.driver.model.ServiceProvider;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
//          serviceProvider=serviceProviderRepository1.save(serviceProvider);

          admin.getServiceProviders().add(serviceProvider);
          serviceProvider.setAdmin(admin);

          admin=adminRepository1.save(admin);

        return null;
    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception{

        ServiceProvider serviceProvider=serviceProviderRepository1.findById(serviceProviderId).get();

        if(!countryName.equalsIgnoreCase("ind") && !countryName.equalsIgnoreCase("aus") && !countryName.equalsIgnoreCase("usa")
        && !countryName.equalsIgnoreCase("chi") && !countryName.equalsIgnoreCase("jpn")){
            throw new Exception("Country not found");
        }




        return null;
    }
}