package com.weareadaptive.auction.user;

import com.weareadaptive.auction.exception.BusinessException;
import com.weareadaptive.auction.model.Entity;
import com.weareadaptive.auction.organisation.Organisation;

import java.util.Objects;

import static com.weareadaptive.auction.utils.StringUtil.isNullOrEmpty;

public class User implements Entity {
    private final int id;
    private final String username;
    private String password;
    private final UserRole userRole;
    private String firstName;
    private String lastName;
    private Organisation organisation;
    private String phone;
    private String email;
    private AccessStatus accessStatus;

    public User(final int id, final String username, final String password, final String firstName,
                final String lastName, final Organisation organisation, final UserRole userRole) {
        if (isNullOrEmpty(username)) {
            throw new BusinessException("username cannot be null or empty");
        }
        if (isNullOrEmpty(password)) {
            throw new BusinessException("password cannot be null or empty");
        }
        if (isNullOrEmpty(firstName)) {
            throw new BusinessException("firstName cannot be null or empty");
        }
        if (isNullOrEmpty(lastName)) {
            throw new BusinessException("lastName cannot be null or empty");
        }
        if (organisation == null) {
            throw new BusinessException("organisation cannot be null");
        }
        if (userRole == null) {
            throw new BusinessException("User Role cannot be null");
        }

        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.organisation = organisation;
        this.accessStatus = AccessStatus.ALLOWED;
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return "User{ username='" + username + '\'' + '}';
    }

    public String getUsername() {
        return username;
    }

    public boolean validatePassword(final String password) {
        return this.password.equals(password);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getId() {
        return id;
    }

    public String getOrganisationName() {
        return organisation.organisationName();
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public AccessStatus getAccessStatus() {
        return accessStatus;
    }

    public void setAccessStatus(final AccessStatus status) {
        this.accessStatus = status;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return getId() == user.getId() && getUsername().equals(user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername());
    }

    public void update(final String password, final String firstName, final String lastName,
                       final Organisation organisation) {
        this.password = isNullOrEmpty(password) ? this.password : password;
        this.firstName = isNullOrEmpty(firstName) ? this.firstName : firstName;
        this.lastName = isNullOrEmpty(lastName) ? this.lastName : lastName;
        this.organisation = organisation == null ? this.organisation : organisation;
    }
}
