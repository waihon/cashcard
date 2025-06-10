package com.waihon.cashcard.controller;

import com.waihon.cashcard.entity.CashCard;
import com.waihon.cashcard.repository.CashCardRepository;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

// Tells Spring that this class is a Component of type RestController and capable of
// handling HTTP requests.
@RestController
// A companion to @RestController that indicates which address requests must have
// to access this Controller.
@RequestMapping("/cashcards")
class CashCardController {

    private final CashCardRepository cashCardRepository;

    private CashCardController(CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }

    // @GetMapping marks a method as a handler method. GET requests that matches
    // cashcards/{requestedId} will be handled by this method.
    @GetMapping("/{requestedId}")
    // @PathVariable makes Spring Web aware of the requestedId supplied in the HTTP request.
    // Now it's available for us to use in our handler method.
    private ResponseEntity<CashCard> findById(@PathVariable Long requestedId, Principal principal) {
        // principal.getName() will return the username provided from Basic Auth.
        CashCard cashCard= findCashCard(requestedId, principal);
        if (cashCard != null) {
            return ResponseEntity.ok(cashCard);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    // POST expects a request "body". This contains the data submitted to the API.
    // Spring Web will deserialize the data into a CashCard for us.
    // UriComponentsBuilder is automatically injected from Spring's IoC Container.
    private ResponseEntity<Void> createCashCard(@RequestBody CashCard newCashCardRequest,
                                                UriComponentsBuilder ucb,
                                                Principal principal) {
        CashCard cashCardWithOwner = new CashCard(null, newCashCardRequest.amount(), principal.getName());
        // The save method saves a new CashCard for us, and returns the saved object
        // with a unique id provided by the database.
        CashCard savedCashCard = cashCardRepository.save(cashCardWithOwner);
        // This is constructing a URI to the newly created CashCard. This is the URI
        // that the caller can then use to GET the newly-created CashCard.
        URI locationOfNewCashCard = ucb
                .path("cashcards/{id}")
                .buildAndExpand(savedCashCard.id())
                .toUri();
        // Returns 201 CREATED with the correct Location header
        return ResponseEntity.created(locationOfNewCashCard).build();
    }

    @GetMapping()
    // Since we specified the URI parameters of page=0&size=1, pageable will contain
    // the values we need.
    private ResponseEntity<List<CashCard>> findAll(Pageable pageable, Principal principal) {
        Page<CashCard> page = cashCardRepository.findByOwner(principal.getName(),
                // PageRequest is a basic Java Bean implementation of Pageable. Things that
                // want paging and sorting implementation often support this, such has
                // some types of Spring Data Repositories.
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        // getSortOr() method provides default values for the page (0), size (20),
                        // and sort parameters.
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
                )
        );
        return ResponseEntity.ok(page.getContent());
    }

    @PutMapping("/{requestedId}")
    // Add the Principal as a method argument, provided automatically by Spring Security.
    private ResponseEntity<Void> putCashCard(@PathVariable Long requestedId, @RequestBody CashCard cashCardUpdate, Principal principal) {
        // Scope our retrieval of the CashCard to the submitted requestedId and Principal to ensure only the authenticated,
        // authorized owner may update this CashCard.
        CashCard cashCard = findCashCard(requestedId, principal);
        if (cashCard == null) {
            return ResponseEntity.notFound().build();
        }
        // Build a CashCard with updated values and save it.
        CashCard updatedCashCard = new CashCard(cashCard.id(), cashCardUpdate.amount(), principal.getName());
        cashCardRepository.save(updatedCashCard);
        return ResponseEntity.noContent().build();
    }

    private CashCard findCashCard(Long requestedId, Principal principal) {
        return cashCardRepository.findByIdAndOwner(requestedId, principal.getName());
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteCashCard(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }
}
