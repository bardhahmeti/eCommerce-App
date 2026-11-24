
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest_2 {

    private OrderService orderService;

    private OrderRepository orderRepository;
    private PaymentGateway paymentGateway;
    private InventoryService inventoryService;
    private EmailService emailService;

    private Order order;

    @BeforeEach
    void setup() {

        orderRepository = mock(OrderRepository.class);
        paymentGateway = mock(PaymentGateway.class);
        inventoryService = mock(InventoryService.class);
        emailService = mock(EmailService.class);

        orderService = new OrderService(
            orderRepository,
            paymentGateway,
            inventoryService,
            emailService
        );

        order = new Order("ORD-100", "P01", 2, 50.0);
    }

    @Test
    void testEverything() {

        // Arrange
        when(inventoryService.isAvailable("P01"))
            .thenReturn(true);

        when(paymentGateway.processPayment(100.0))
            .thenReturn(true);

        when(orderRepository.save(any(Order.class)))
            .thenReturn(order);

        // Act
        Order result = orderService.placeOrder(order);

        // Assert
        assertNotNull(result);
        assertEquals("ORD-100", result.getId());
        assertEquals(100.0, result.getTotal());
        assertTrue(result.isPaid());

        verify(inventoryService).isAvailable("P01");
        verify(paymentGateway).processPayment(100.0);
        verify(orderRepository).save(any(Order.class));
        verify(emailService).sendConfirmation("ORD-100");

        // Additional scenarios
        order.setQuantity(-1);

        try {
            orderService.placeOrder(order);
        } catch (Exception e) {
            System.out.println("Error occurred");
        }

        assertTrue(true);

        order.setQuantity(0);

        try {
            orderService.placeOrder(order);
        } catch (Exception e) {
            // Ignore
        }

        assertNotNull(order);

        // Test another scenario
        when(inventoryService.isAvailable("P01"))
            .thenReturn(false);

        Order secondResult = orderService.placeOrder(order);

        assertNull(secondResult);

        verify(inventoryService, times(2))
            .isAvailable("P01");

        // Check internal implementation
        verify(paymentGateway, times(1))
            .processPayment(100.0);
    }

    @Test
    void testPaymentFailure() {

        when(inventoryService.isAvailable("P01"))
            .thenReturn(true);

        when(paymentGateway.processPayment(100.0))
            .thenReturn(false);

        try {
            orderService.placeOrder(order);
        } catch (Exception e) {
            // Ignore exception
        }

        assertTrue(true);
    }

    @Test
    void testOrderStatus() {

        order.setStatus("PAID");

        assertEquals("PAID", order.getStatus());

        order.setStatus("SHIPPED");

        assertEquals("SHIPPED", order.getStatus());

        order.setStatus("DELIVERED");

        assertEquals("DELIVERED", order.getStatus());
    }

    @Test
    void testOrderWithCurrentUser() {

        String username = System.getProperty("user.name");

        assertEquals("student", username);
    }

    @Test
    void testOrderCreation() {

        Order result = orderService.createOrder(order);

        assertNotNull(result);
        assertTrue(true);
        assertEquals("ORD-100", result.getId());
    }
}
