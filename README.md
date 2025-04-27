# Bulletin Board - Testing Strategy

This project is a **Bulletin Board System** built with **Spring Boot**. The application includes features like **message management** (add, delete, and retrieve messages), and **channels** to which messages belong. In this document, we explain the testing strategy used for this project and provide instructions for running the tests.

---

## Table of Contents

1. [Testing Strategy](#testing-strategy)
2. [Tested Methods and Justification](#tested-methods-and-justification)
3. [Running the Tests](#running-the-tests)

---

## Testing Strategy

The testing strategy for this project uses **unit tests**, **component tests**, and **integration tests** to ensure the system works correctly. The tests are designed to cover:

1. **Unit Tests**:
    - Test the logic of individual methods in service classes.
    - **Mocks** the dependencies to ensure isolation from external resources like databases.

2. **Component Tests**:
    - Test REST endpoints to ensure that controllers, services, and repositories interact properly.
    - **Mocks** services and repositories to ensure isolation from the database.

3. **Integration Tests**:
    - Test the entire flow from the controller to the database, ensuring that the full system works correctly.
    - **Uses a test database** to ensure that production data remains safe.

### Why This Strategy?
- **Unit tests** are used to test small, isolated parts of the code (e.g., service methods). We ensure that each function performs its job correctly and edge cases are handled.
- **Component tests** are important for verifying that the **controller layer** correctly interacts with the **service layer** and **repository**.
- **Integration tests** validate the entire system — from **Controller → Service → Repository → Database**, ensuring that the data is properly persisted and retrieved. 

By using these three types of tests, we ensure comprehensive test coverage at different levels of granularity.

---

## Tested Methods and Justification

### 1. **Unit Tests**

- **Methods Tested**: Methods inside the `MessageService` class:
    - `addMessage(Message message)`: Tests that a new message is added correctly.
    - `getMessagesByChannel(Long channelId)`: Verifies that messages are correctly fetched by channel.
    - `deleteMessage(Long id)`: Ensures that a message is deleted correctly.
    - `updateMessage(Long id, String content)`: Ensures that the message content is updated correctly.

- **Why Tested**: 
    - These methods represent the core business logic of the application. They interact with the **MessageRepository** to persist data.
    - The **unit tests** ensure that individual business rules (such as adding, retrieving, and updating messages) work correctly **in isolation** from external resources like databases.

---

### 2. **Component Test**

- **Methods Tested**: Controller methods in the `MessageController` class:
    - `POST /messages`: Tests creating a message.
    - `GET /messages/channel/{channelId}`: Tests retrieving messages by channel.

- **Why Tested**: 
    - These tests verify that the **controller** layer correctly handles HTTP requests and responses, ensuring proper communication between the **service** layer and the **controller**.
    - **Mocking** the **service layer** allows us to focus on the correctness of the **controller** logic without interacting with the database.

---

### 3. **Integration Test**

- **Methods Tested**: Full integration test covering the **controller → service → repository → database** flow:
    - `POST /messages`: Tests the creation of a message.
    - `GET /messages/channel/{channelId}`: Tests retrieving messages for a specific channel.

- **Why Tested**:
    - This test validates the entire system, ensuring that data can flow correctly through all layers from the controller down to the **MySQL database**.
    - The test database (isolated from production) ensures no production data is affected.

---

## Running the Tests

1. **Prerequisites**:
    - Make sure you have a **MySQL** database running and that it's configured for testing.
    - The configuration for the test database should be specified in `application-test.yml`. Here is a sample configuration:

    ```yaml
    # src/main/resources/application-test.yml
    spring:
      datasource:
        url: jdbc:mysql://localhost:3306/bulletinboard_test
        username: testuser
        password: testpass
      jpa:
        hibernate:
          ddl-auto: update
        show-sql: true
    ```

2. **Run Unit Tests**:
    - You can run the unit tests using your IDE's **JUnit** support or through Maven/Gradle.
    - **Using Maven**: Run the following command to execute the unit tests:

    ```bash
    mvn test
    ```

3. **Run Component Tests**:
    - Component tests can be run alongside unit tests since they test isolated parts of the system (controller logic).
    - **Using Maven**: Run:

    ```bash
    mvn test
    ```

4. **Run Integration Tests**:
    - The integration tests require a **test MySQL database**. Ensure that your `application-test.yml` file is correctly configured to point to a test database.
    - To run the integration tests, use the following Maven command:

    ```bash
    mvn test -Dspring.profiles.active=test
    ```

    This ensures that the **test profile** is loaded, and the **test database** configuration is used.

---

## Conclusion

In this project, we applied **unit tests**, **component tests**, and **integration tests** to ensure that each layer of the application works correctly. By isolating the test database and using **mocking** strategies where appropriate, we ensured that no production data was impacted. 

Feel free to run the tests as explained above to verify the correctness of the system!

