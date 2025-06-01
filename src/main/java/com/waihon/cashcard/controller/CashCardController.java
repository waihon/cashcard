package com.waihon.cashcard.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Tells Spring that this class is a Component of type RestController and capable of
// handling HTTP requests.
@RestController
// A companion to @RestController that indicates which address requests must have
// to access this Controller.
@RequestMapping("/cashcards")
class CashCardController {

    // @GetMapping marks a method as a handler method. GET requests that matches
    // cashcards/{requestedId} will be handled by this method.
    @GetMapping("/{requestedId}")
    private ResponseEntity<String> findById() {
        return ResponseEntity.ok("{}");
    }
}
