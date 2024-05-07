package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;

    @Override
    public User connect(int userId, String countryName) throws Exception{

        User user=userRepository2.findById(userId).get();
        if(user.getMaskedIp()!=null){
            throw new Exception("Already connected");
        }
        if(countryName.equalsIgnoreCase(user.getOriginalCountry().getCountryName().toString())){
            return user;
        }


        List<ServiceProvider>serviceProviderList=user.getServiceProviderList();

//        if(serviceProviderList==null){
//            throw new Exception("Unable to connect");
//        }

        ServiceProvider serviceProvider=null;
        boolean flg=false;
        Country country=null;
        int minId=Integer.MAX_VALUE;
        for(ServiceProvider serviceProvider1:serviceProviderList){
            List<Country>countryList=serviceProvider1.getCountryList();
            for(Country country1:countryList){
                if(countryName.equalsIgnoreCase(country1.getCountryName().toString()) && minId>serviceProvider1.getId()){
                    flg=true;
                    serviceProvider=serviceProvider1;
                    country=country1;
                    minId=serviceProvider1.getId();
                }
            }
        }

        if(flg==false){
            throw new Exception("Unable to connect");
        }


        user.setConnected(true);
        // "updatedCountryCode.serviceProviderId.userId"
        user.setMaskedIp(new String(country.getCode()+"."+serviceProvider.getId()+"."+userId));

        Connection connection=new Connection();
        connection.setUser(user);
        connection.setServiceProvider(serviceProvider);
//        connection=connectionRepository2.save(connection);

        user.getConnectionList().add(connection);
        serviceProvider.getConnectionList().add(connection);

        userRepository2.save(user);
        serviceProviderRepository2.save(serviceProvider);

//        user.getConnectionList().add(connection);
//        userRepository2.save(user);

        return user;
    }

    @Override
    public User disconnect(int userId) throws Exception {

        User user=userRepository2.findById(userId).get();
        if(user.getMaskedIp()==null){
            throw new Exception("Already disconnected");
        }

        user.setConnected(false);
        user.setMaskedIp(null);

        List<Connection>connectionList=user.getConnectionList();
        for(Connection connection:connectionList){
            if(connection.getUser().equals(user)){
                connectionList.remove(connection);
            }
        }

        userRepository2.save(user);
        return user;
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {

        User Sender=userRepository2.findById(senderId).get();
        User reciever=userRepository2.findById(receiverId).get();

        Country currentCountry=reciever.getOriginalCountry();








        return null;

    }
}
