package com.weareadaptive.auction.organisation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/api/v1/organisations")
public class OrganisationController {
    private final OrganisationService service;

    public OrganisationController(final OrganisationService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity<List<Organisation>> getAllOrganisations() {
        return ResponseEntity.ok().body(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organisation> getOrganisation(@PathVariable final int id) {
        return ResponseEntity.ok().body(service.get(id));
    }
}
