import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Test
    void shouldConfirmOrderSuccessfully() {

        // Arrange
        ProductRepository productRepository =
                mock(ProductRepository.class);

        PaymentService paymentService =
                mock(PaymentService.class);

        OrderService orderService =
                new OrderService(
                        productRepository,
                        paymentService);

        Product product =
                new Product("P01", 100, 10);

        when(productRepository.findById("P01"))
                .thenReturn(product);

        when(paymentService.pay(200.0))
                .thenReturn(true);

        // Act
        String result =
                orderService.placeOrder("P01", 2);

        // Assert
        assertEquals("Order confirmed", result);

        verify(paymentService).pay(200.0);
    }

    @Test
    void shouldReturnProductNotFound() {

        // Arrange
        ProductRepository productRepository =
                mock(ProductRepository.class);

        PaymentService paymentService =
                mock(PaymentService.class);

        OrderService orderService =
                new OrderService(
                        productRepository,
                        paymentService);

        when(productRepository.findById("P01"))
                .thenReturn(null);

        // Act
        String result =
                orderService.placeOrder("P01", 2);

        // Assert
        assertEquals("Product not found", result);
    }

    @Test
    void shouldReturnInvalidQuantity() {

        // Arrange
        ProductRepository productRepository =
                mock(ProductRepository.class);

        PaymentService paymentService =
                mock(PaymentService.class);

        OrderService orderService =
                new OrderService(
                        productRepository,
                        paymentService);

        Product product =
                new Product("P01", 100, 10);

        when(productRepository.findById("P01"))
                .thenReturn(product);

        // Act
        String result =
                orderService.placeOrder("P01", 0);

        // Assert
        assertEquals(
                "Invalid quantity or insufficient stock",
                result);
    }

    @Test
    void shouldReturnPaymentFailed() {

        // Arrange
        ProductRepository productRepository =
                mock(ProductRepository.class);

        PaymentService paymentService =
                mock(PaymentService.class);

        OrderService orderService =
                new OrderService(
                        productRepository,
                        paymentService);

        Product product =
                new Product("P01", 100, 10);

        when(productRepository.findById("P01"))
                .thenReturn(product);

        when(paymentService.pay(200.0))
                .thenReturn(false);

        // Act
        String result =
                orderService.placeOrder("P01", 2);

        // Assert
        assertEquals("Payment failed", result);
    }
}