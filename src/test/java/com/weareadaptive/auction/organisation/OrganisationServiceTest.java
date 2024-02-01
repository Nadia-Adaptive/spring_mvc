package com.weareadaptive.auction.organisation;

import com.weareadaptive.auction.exception.NotFoundException;
import com.weareadaptive.auction.user.UserRepository;
import com.weareadaptive.auction.user.UserRole;
import com.weareadaptive.auction.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.weareadaptive.auction.TestData.ADMIN_ORGANISATION;
import static com.weareadaptive.auction.TestData.ORGANISATION1;
import static com.weareadaptive.auction.TestData.ORG_1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrganisationServiceTest {
    OrganisationService service;

    @BeforeEach
    public void initState() {
        final var repo = new OrganisationRepository();
        repo.add(ADMIN_ORGANISATION);
        repo.add(ORGANISATION1);
        service = new OrganisationService(repo);
        final var userService = new UserService(new UserRepository(), repo);
        userService.createUser("test01", "password", "first", "last", "organisation", UserRole.USER);

    }

    @Test
    @DisplayName("AddOrganisation_OrganisationDoesNotExist_ReturnsOrganisation")
    void addOrganisation() {
        final var organisation = service.addOrganisation("Organisation 48");
        assertEquals(Organisation.class, organisation.getClass());
    }

    @Test
    @DisplayName("AddOrganisation_OrganisationDoesExist_ReturnsExistingOrganisation")
    void addOrganisationThatExists() {
        assertEquals(ORGANISATION1, service.addOrganisation(ORGANISATION1.organisationName()));
    }

    @Test
    @DisplayName("GetOrganisation_OrganisationExists_ReturnsOrganisation")
    void getOrganisation() {
        final var organisation = service.get(10);
        assertEquals(ORGANISATION1, organisation);
    }

    @Test
    @DisplayName("GetOrganisation_OrganisationDoesNotExist_ReturnsOrganisation")
    void getOrganisationThatDoesNotExist() {
        assertThrows(NotFoundException.class, () -> service.get(-13));
    }

    @Test
    @DisplayName("GetAllOrganisations_ReturnsOrganisations")
    void getAllOrganisations() {
        assertEquals(3, service.getAll().size());
    }

    @Test
    @DisplayName("GetOrganisationByName_OrganisationExists_ReturnsOrganisation")
    void getOrganisationByName() {
        final var organisation = service.get(ORG_1);
        assertEquals(ORGANISATION1, organisation);
    }

    @Test
    @DisplayName("GetOrganisationByName_OrganisationDoesNotExist_Throws")
    void getOrganisationByNameThatDoesNotExist() {
        assertThrows(NotFoundException.class, () -> service.get("fail"));
    }
}
