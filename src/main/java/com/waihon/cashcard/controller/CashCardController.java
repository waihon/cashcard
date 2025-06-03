package com.waihon.cashcard.controller;

import com.waihon.cashcard.entity.CashCard;
import com.waihon.cashcard.repository.CashCardRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private ResponseEntity<CashCard> findById(@PathVariable Long requestedId) {
        Optional<CashCard> cashCardOptional = cashCardRepository.findById(requestedId);
        if (cashCardOptional.isPresent()) {
            return ResponseEntity.ok(cashCardOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
