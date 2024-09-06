package com.homebuildmanager.models.Order;

public enum OrderStatus {
    NEW,             // Nowy
    ACCEPTED,        // Zaakceptowany
    IN_PROGRESS,     // W trakcie realizacji
    READY_FOR_REVIEW, // Do zatwierdzenia
    NEEDS_REWORK,    // Do poprawy
    APPROVED,        // Zatwierdzony
    ARCHIVED,        // Zarchiwizowany
    REJECTED         // Odrzucony
}
