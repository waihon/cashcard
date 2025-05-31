package com.waihon.cashcard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CashCardJsonTest {

    // JacksonTester is a convenient wrapper to the Jackson JSON parsing library.
    // It handles serialization and deserialization of JSON objects.
    @Autowired
    private JacksonTester<CashCard> json;

    @Test
    void myFirstTest() {
        assertThat(42).isEqualTo(42);
    }

    @Test
    void cashCardSerializationTest() throws IOException {
        CashCard cashCard = new CashCard(99L, 123.45);
        var serializedCard = json.write(cashCard);
        assertThat(serializedCard).isStrictlyEqualToJson("expected.json");
        assertThat(serializedCard).hasJsonPathNumberValue("@.id");
        assertThat(serializedCard).extractingJsonPathNumberValue("@.id").isEqualTo(99);
        assertThat(serializedCard).hasJsonPathNumberValue("@.amount");
        assertThat(serializedCard).extractingJsonPathNumberValue("@.amount").isEqualTo(123.45);
    }

    @Test
    void cashCardDeserializationTest() throws IOException {
        String expected = """
                {
                    "id": 99,
                    "amount": 123.45
                }
                """;
        assertThat(json.parse(expected))
                .isEqualTo(new CashCard(1_000L, 67.89));
        assertThat(json.parseObject(expected).id()).isEqualTo(1_000);
        assertThat(json.parseObject(expected).amount()).isEqualTo(67.89);
    }

}
