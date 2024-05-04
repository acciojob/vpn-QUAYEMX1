// Note: Do not write @Enumerated annotation above CountryName in this model.
package com.driver.model;

import javax.persistence.*;

@Entity
public class Country{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int CountryId;

    private CountryName countryName;
    private String code;

    @JoinColumn
    @OneToOne
    private User user;

    @JoinColumn
    @ManyToOne
    private ServiceProvider serviceProvider;

    public int getCountryId(){return CountryId;}

    public void setCountryId(int CountryId){this.CountryId=CountryId;}

    public CountryName getCountryName(){return countryName;}

    public void setCountryName(CountryName countryName){this.countryName=countryName;}

    public String getCode(){return code;}

    public void setCode(String countryCode){this.code=code;}

    public User getUser(){return user;}

    public void setUser(User user){this.user=user;}

    public ServiceProvider getServiceProvider(){return serviceProvider;}

    public void setServiceProvider(ServiceProvider serviceProvider){this.serviceProvider=serviceProvider;}

}
