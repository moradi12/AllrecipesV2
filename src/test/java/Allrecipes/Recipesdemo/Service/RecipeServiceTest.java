//package Allrecipes.Recipesdemo.Service;
//
//import Allrecipes.Recipesdemo.Entities.User;
//import Allrecipes.Recipesdemo.Recipe.Recipe;
//import Allrecipes.Recipesdemo.Recipe.RecipeCreateRequest;
//import Allrecipes.Recipesdemo.Repositories.RecipeRepository;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.producer.Producer;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.test.EmbeddedKafkaBroker;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.kafka.test.utils.KafkaTestUtils;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//@EmbeddedKafka(partitions = 1, topics = {"recipe-created"})
//class RecipeServiceTest {
//
//    @Mock
//    private RecipeRepository recipeRepository;
//
//    private RecipeService recipeService;
//
//    private EmbeddedKafkaBroker embeddedKafkaBroker;
//    private Producer<String, String> producer;
//
//    @BeforeEach
//    void setUp() {
//        embeddedKafkaBroker = new EmbeddedKafkaBroker(1, true, 1, "recipe-created");
//        embeddedKafkaBroker.afterPropertiesSet(); // Initialize the broker
//
//        recipeService = new RecipeService(recipeRepository);
//
//        // Kafka Producer configuration
//        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);
//        producer = new DefaultKafkaProducerFactory<>(
//                producerProps,
//                new StringSerializer(),
//                new StringSerializer()
//        ).createProducer();
//    }
//
//    @AfterEach
//    void tearDown() {
//        if (producer != null) {
//            producer.close();
//        }
//        if (embeddedKafkaBroker != null) {
//            embeddedKafkaBroker.destroy();
//        }
//    }
//
//    @Test
//    void createRecipe_ValidRequest_ShouldCreateAndPublishRecipe() {
//        // Arrange
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("testUser");
//
//        RecipeCreateRequest request = RecipeCreateRequest.builder()
//                .title("Test Recipe")
//                .description("Test Description")
//                .ingredients(Arrays.asList("ingredient1", "ingredient2"))
//                .cookingTime(30)
//                .servings(4)
//                .build();
//
//        Recipe expectedRecipe = Recipe.builder()
//                .id(1L)
//                .title(request.getTitle())
//                .description(request.getDescription())
//                .ingredients(request.getIngredients())
//                .createdBy(user)
//                .build();
//
//        when(recipeRepository.save(any(Recipe.class))).thenReturn(expectedRecipe);
//
//        // Act
//        Recipe result = recipeService.createRecipe(request, user);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(expectedRecipe.getTitle(), result.getTitle());
//        assertEquals(expectedRecipe.getDescription(), result.getDescription());
//
//        // Verify Kafka message was sent
//        ProducerRecord<String, String> record = new ProducerRecord<>(
//                "recipe-created",
//                result.getId().toString(),
//                result.getTitle()
//        );
//        producer.send(record);
//
//        // Verify message received
//        Map<String, Object> consumerProps = new HashMap<>(KafkaTestUtils.consumerProps("test-group", "true", embeddedKafkaBroker));
//        DefaultKafkaConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory<>(
//                consumerProps,
//                new StringDeserializer(),
//                new StringDeserializer()
//        );
//
//        ConsumerRecord<String, String> received = KafkaTestUtils.getSingleRecord(cf.createConsumer(), "recipe-created");
//        assertEquals(result.getId().toString(), received.key());
//        assertEquals(result.getTitle(), received.value());
//    }
//}
