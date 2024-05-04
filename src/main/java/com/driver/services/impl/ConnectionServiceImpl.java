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

        List<ServiceProvider>serviceProviderList=user.getServiceProviderList();

//       if(serviceProviderList.size()>0){
//           throw new Exception("Already connected");
//       }
       if(countryName.equalsIgnoreCase(user.getOriginalCountry().getCountryName().toString())){
           return user;
       }

       boolean flg=false;
       int minId=Integer.MAX_VALUE;
       ServiceProvider serviceProvider1=null;
       Country country1=null;
       for(ServiceProvider serviceProvider:serviceProviderList){
           List<Country>countryList=serviceProvider.getCountryList();
           for(Country country:countryList){
               if(countryName.equalsIgnoreCase(country.getCountryName().toString()) && serviceProvider.getId()<minId){
                   minId=serviceProvider.getId();
                   serviceProvider1=serviceProvider;
                   country1=country;
                   flg=true;

               }
           }
       }

       if(flg==false){
           throw new Exception("Unable to connect");
       }

       user.setMaskedIp(new String(country1.getCode()+"."+serviceProvider1.getId()+"."+userId));
       user.setOriginalCountry(country1);
       user.setConnected(true);


       Connection connection=new Connection();
       connection.setUser(user);
       connection.setServiceProvider(serviceProvider1);

        user.getConnectionList().add(connection);

//        serviceProvider1.getUsers().add(user);
        serviceProvider1.getConnectionList().add(connection);

        userRepository2.save(user);
       serviceProviderRepository2.save(serviceProvider1);

       return user;

    }
    @Override
    public User disconnect(int userId) throws Exception {

        User user=userRepository2.findById(userId).get();
        if(user.getMaskedIp()==null){
            throw new Exception("Already disconnected");
        }

        user.setMaskedIp(null);
        user.setConnected(false);

        userRepository2.save(user);
        return user;
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {

        User sender = userRepository2.findById(senderId).get();
        User receiver = userRepository2.findById(receiverId).get();
        if (receiver.getMaskedIp()!=null){
            String maskedIp = receiver.getMaskedIp();
            String code = maskedIp.substring(0,3);
            code = code.toUpperCase();
            if (code.equals(sender.getOriginalCountry().getCode())) return sender;
            String countryName = "";
            CountryName[] countryNames = CountryName.values();
            for(CountryName countryName1 : countryNames){
                if (countryName1.toCode().toString().equals(code)){
                    countryName = countryName1.toString();
                }
            }
            try {
                sender = connect(senderId,countryName);
            }catch (Exception e){
                throw new Exception("Cannot establish communication");
            }
            if (!sender.getConnected()){
                throw new Exception("Cannot establish communication");
            }
            return sender;
        }
        if (sender.getOriginalCountry().equals(receiver.getOriginalCountry())){
            return sender;
        }
        String countryName = receiver.getOriginalCountry().getCountryName().toString();
        try {
            sender = connect(senderId,countryName);
        }catch (Exception e){
            if (!sender.getConnected()) throw new Exception("Cannot establish communication");
        }
        return sender;


    }
}
