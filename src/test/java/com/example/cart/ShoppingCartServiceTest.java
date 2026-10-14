package com.example.cart;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ShoppingCartService - testy mockowania")
public class ShoppingCartServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private DiscountService discountService;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    @Nested
    @DisplayName("calculateTotal()")
    class calculateTotal {

        @Test
        @DisplayName("Powinien obliczyć cenę dla jednego produktu bez rabatu")
        void shouldCalculatePriceForSingleProductWithoutDiscount() {
            Product laptop = new Product("P1", "laptop", 100.00);
            when(productRepository.findById("P1")).thenReturn(Optional.of(laptop));
            when(discountService.getDiscountForCustomer("C1")).thenReturn(0.0);
            List<CartItem> items = List.of(new CartItem("P1", 2));

            double total = shoppingCartService.calculateTotal("C1", items);

            assertEquals(200.00, total, 0.01);
            verify(productRepository, times(1)).findById("P1");
            verify(discountService, times(1)).getDiscountForCustomer("C1");
            verifyNoMoreInteractions(productRepository, discountService);
        }

        @Test
        @DisplayName("Powinien zastosować 10% rabatu dla klienta VIP")
        void shouldApplyTenPercentDiscountForVipCustomer() {
            when(productRepository.findById("P1"))
                    .thenReturn(Optional.of(new Product("P1", "Laptop", 100.0)));
            when(discountService.getDiscountForCustomer("VIP1")).thenReturn(0.1);

            double total = shoppingCartService.calculateTotal("VIP1", List.of(new CartItem("P1", 2)));

            assertEquals(180.0, total, 0.01);
            verify(discountService).getDiscountForCustomer("VIP1");
            verify(productRepository).findById("P1");
        }

        @Test
        @DisplayName("Powinien obliczyć cenę dla wielu produktów")
        void shouldCalculatePriceForMultipleProducts() {
            when(productRepository.findById("P1"))
                    .thenReturn(Optional.of(new Product("P1", "Laptop", 100.0)));
            when(productRepository.findById("P2"))
                    .thenReturn(Optional.of(new Product("P2", "Mysz", 25.5)));
            when(productRepository.findById("P3"))
                    .thenReturn(Optional.of(new Product("P3", "Klawiatura", 50.0)));
            when(discountService.getDiscountForCustomer("C1")).thenReturn(0.0);

            List<CartItem> items = List.of(
                    new CartItem("P1", 1),
                    new CartItem("P2", 2),
                    new CartItem("P3", 3));

            double total = shoppingCartService.calculateTotal("C1", items);

            // 100 + 2*25.5 + 3*50 = 301
            assertEquals(301.0, total, 0.01);
            verify(productRepository).findById("P1");
            verify(productRepository).findById("P2");
            verify(productRepository).findById("P3");
            verify(discountService, times(1)).getDiscountForCustomer("C1");
            verifyNoMoreInteractions(productRepository, discountService);
        }

        @Test
        @DisplayName("Powinien rzucić wyjątek dla pustego koszyka")
        void shouldThrowExceptionForEmptyCart() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> shoppingCartService.calculateTotal("C1", Collections.emptyList()));

            assertTrue(ex.getMessage().contains("Koszyk nie może być pusty"));
            verifyNoInteractions(productRepository, discountService);
        }

        @Test
        @DisplayName("Powinien rzucić wyjątek gdy produkt nie istnieje")
        void shouldThrowExceptionWhenProductNotFound() {
            when(productRepository.findById("UNKNOWN")).thenReturn(Optional.empty());
            List<CartItem> items = List.of(new CartItem("UNKNOWN", 1));

            ProductNotFoundException ex = assertThrows(
                    ProductNotFoundException.class,
                    () -> shoppingCartService.calculateTotal("C1", items));

            assertTrue(ex.getMessage().contains("UNKNOWN"));
            verify(productRepository).findById("UNKNOWN");
            verify(discountService, never()).getDiscountForCustomer("C1");
            verifyNoInteractions(discountService);
        }
    }
}