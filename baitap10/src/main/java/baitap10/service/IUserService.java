package baitap10.service;

import baitap10.entity.User; 

public interface IUserService {
    User findByUsernameAndPassword(String username, String password);
}