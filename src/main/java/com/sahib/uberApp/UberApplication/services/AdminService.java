package com.sahib.uberApp.UberApplication.services;

public interface AdminService {

    void grantAdmin(Long userId);

    void removeAdmin(Long userId);

    void transferOwnership(Long newOwnerId);


}
