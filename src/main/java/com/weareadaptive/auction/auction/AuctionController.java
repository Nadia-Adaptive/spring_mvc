package com.weareadaptive.auction.auction;

import com.weareadaptive.auction.bid.BidDTO;
import com.weareadaptive.auction.exception.BusinessException;
import com.weareadaptive.auction.response.Response;
import com.weareadaptive.auction.response.ResponseBody;
import com.weareadaptive.auction.response.ResponseBuilder;
import com.weareadaptive.auction.response.ResponseStatus;
import com.weareadaptive.auction.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;

@RestController
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
@RequestMapping("/api/v1/auctions/")
public class AuctionController {
    AuctionService auctionService;
    Logger log = LoggerFactory.getLogger(AuctionController.class);

    AuctionController(final AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @PostMapping("/")
    ResponseEntity<Response> createAuction(@RequestBody final AuctionDTO body) {
        auctionService.createAuction(body.ownerId(), body.product(), body.minPrice(), body.quantity());
        log.info("Auction created.");
        return ResponseBuilder.created();
    }

    @GetMapping("/")
    ResponseEntity<ResponseBody> getAllAuctions() {
        log.info("All auctions requested.");
        final var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseBuilder.ok(auctionService.getAllAuctions(user.getId()));
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseBody> getAuction(@PathVariable final int id) {
        log.info("Auction with id " + id + "requested.");

        return ResponseBuilder.ok(auctionService.getAuction(id));
    }

    @GetMapping("/available")
    ResponseEntity<ResponseBody> getAllAvailableAuctions() {
        log.info("All available auctions requested.");
        final var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseBuilder.ok(auctionService.getAvailableAuctions(user.getId()));
    }

    @PutMapping("/{id}/bids")
    ResponseEntity<Response> putAuctionsMakeABid(@PathVariable final int id, @RequestBody final BidDTO body) {
        log.info("Request to bid on auction with id " + id);

        auctionService.makeABid(id, body);
        log.info("Bid placed.");
        return ResponseBuilder.ok();
    }

    @PutMapping("/{id}/close")
    ResponseEntity<Response> putCloseAuction(@PathVariable final int id) throws AccessDeniedException {
        log.info("Request to close auction with id " + id);


        final var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            auctionService.closeAuction(id, user.getId());
        } catch (final BusinessException e) {
            return ResponseBuilder.respond(HttpStatus.FORBIDDEN, ResponseStatus.FORBIDDEN);
        }
        log.info("Auction closed.");
        return ResponseBuilder.ok();
    }
}
