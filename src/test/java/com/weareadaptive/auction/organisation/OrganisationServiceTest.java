package com.weareadaptive.auction.organisation;

import com.weareadaptive.auction.model.NotFoundException;
import com.weareadaptive.auction.user.UserRepository;
import com.weareadaptive.auction.user.UserRole;
import com.weareadaptive.auction.user.UserService;
import org.junit.jupiter.api.BeforeEach;
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
    void AddOrganisation_OrganisationDoesNotExist_ReturnsOrganisation() {
        final var organisation = service.addOrganisation("Organisation 48");
        assertEquals(Organisation.class, organisation.getClass());
    }

    @Test
    void AddOrganisation_OrganisationDoesExist_ReturnsExistingOrganisation() {
        assertEquals(ORGANISATION1, service.addOrganisation(ORGANISATION1.organisationName()));
    }

    @Test
    void GetOrganisation_OrganisationExists_ReturnsOrganisation() {
        final var organisation = service.get(10);
        assertEquals(ORGANISATION1, organisation);
    }

    @Test
    void GetOrganisation_OrganisationDoesNotExist_Throws() {
        assertThrows(NotFoundException.class, () -> service.get(-13));
    }

    @Test
    void GetAllOrganisations_ReturnsOrganisations() {
        assertEquals(3, service.getAll().size());
    }

    @Test
    void GetOrganisationByName_OrganisationExists_ReturnsOrganisation() {
        final var organisation = service.get(ORG_1);
        assertEquals(ORGANISATION1, organisation);
    }

    @Test
    void GetOrganisationByName_OrganisationDoesNotExist_Throws() {
        assertThrows(NotFoundException.class, () -> service.get("fail"));
    }
}
