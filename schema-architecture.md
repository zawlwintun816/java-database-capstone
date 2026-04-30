# Architecture Design – Smart Clinic Management System

## Section 1: Architecture Summary

This Spring Boot application follows a three-tier architecture consisting of the presentation layer, application layer, and data layer. The presentation layer includes Thymeleaf-based web pages used for Admin and Doctor dashboards, as well as REST API clients for modules like appointments and patient records. This design allows the system to support both server-rendered views and scalable API-based communication.

The application layer is built using Spring Boot and includes both MVC and REST controllers. These controllers handle incoming requests and pass them to a centralized service layer, where business logic and validation are performed. The service layer ensures proper separation of concerns and communicates with repository classes for data access.

The data layer consists of two databases: MySQL and MongoDB. MySQL is used for structured relational data such as patients, doctors, appointments, and admin information, using JPA entities. MongoDB is used for flexible, document-based data like prescriptions. This dual-database approach allows efficient handling of both structured and unstructured data.

---

## Section 2: Numbered Flow of Data and Control

1. Users access the system through Thymeleaf web pages (Admin/Doctor dashboards) or REST API clients (appointments, patient modules).
2. The request is routed to the appropriate controller (MVC controller for web pages or REST controller for APIs).
3. The controller processes the request and forwards it to the service layer.
4. The service layer applies business logic, validation, and workflows.
5. The service layer calls the repository layer to interact with the database.
6. The repository communicates with MySQL or MongoDB and retrieves or stores data.
7. The data is mapped into models and returned to the controller, which sends back either an HTML view or a JSON response to the user.