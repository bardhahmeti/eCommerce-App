import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldConfirmOrderSuccessfully() {

        // STUB
        when(productRepository.findById("P01"))
                .thenReturn(new Product("P01", 100, 10));

        when(paymentService.pay(200.0))
                .thenReturn(true);

        // ACT
        String result =
                orderService.placeOrder("P01", 2);

        // ASSERT
        assertEquals("Order confirmed", result);

        // MOCK VERIFICATION
        verify(paymentService).pay(200.0);
    }
}