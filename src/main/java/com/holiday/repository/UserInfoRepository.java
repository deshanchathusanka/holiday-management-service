package com.holiday.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.holiday.models.UserInfo;

/**
 * @author : dchat
 * @since : 11/11/2020, Wed
 **/
@Repository(value = "userInfoRepository")
public interface UserInfoRepository extends JpaRepository<UserInfo, Serializable> {

    public UserInfo findByEmail(String email);

    public List<UserInfo> findAllByOrderById();

    public UserInfo findById(int id);

    @Transactional
    @Modifying
    @Query(value = "update employee set active=false where id=?", nativeQuery = true)
    public void blockUser(int id);

    @Transactional
    @Modifying
    @Query(value = "update employee set active=true where id=?", nativeQuery = true)
    public void unBlockUser(int id);
}
