# Bhukkad  
### Real-Time Campus Cafe Management System for Android

A dual-sided Android application engineered to manage end-to-end campus cafe operations through real-time synchronization, role-based routing, and integrated digital payments.

Built using native Android (Java/XML), Firebase, and Razorpay.

---

## Why This Exists

Most college food ordering systems are fragmented, manual, or operationally inefficient:

- Orders are handled verbally or through paper slips
- No real-time kitchen visibility
- No centralized menu management
- No structured role separation between staff and customers
- No transaction traceability

Bhukkad was built as a complete operational workflow system for a campus cafe environment.

It is not just a food ordering UI.  
It is a synchronized operational platform.

---

## Design Goals

- **Real-Time Synchronization** — Orders and menu updates reflect instantly across devices
- **Role-Based Access Control** — Staff and customer flows remain isolated
- **Persistent Session Routing** — Users bypass login when authenticated
- **Transactional Integrity** — Orders exist before payment confirmation
- **Low-Latency UI Updates** — Firebase listeners drive live updates
- **Scalable Menu CMS** — Staff can dynamically manage inventory and visibility

---

## System Architecture

### Execution Flow

1. User authenticates using Firebase Authentication
2. App queries Firestore `users` collection
3. Role-based router redirects:
   - `customer` → Customer UI
   - `staff` → Staff Dashboard
4. Customer places order
5. Draft order stored with `payment_pending`
6. Razorpay transaction initiated
7. Successful callback upgrades order state to `Pending`
8. Staff dashboard updates in real time through Firestore listeners

---

## Architectural Characteristics

- Native Android application (Java/XML)
- Firebase-backed real-time NoSQL architecture
- MVVM-based dashboard logic
- Singleton-powered cart persistence
- Observer-driven UI synchronization
- RecyclerView lifecycle-safe state management

---

## Core Features

- Firebase Authentication
- Real-time Firestore synchronization
- Staff/Admin dashboard
- Dynamic menu management
- Cart management system
- Razorpay payment integration
- Order lifecycle tracking
- Real-time kitchen workflow updates
- Persistent login sessions
- Live KPI dashboard aggregation

---

## Tech Stack

- **Language:** Java
- **Frontend:** Native Android XML
- **Architecture:** MVVM
- **Backend:** Firebase
- **Database:** Firestore NoSQL
- **Storage:** Firebase Cloud Storage
- **Authentication:** Firebase Auth
- **Payment Gateway:** Razorpay SDK
- **Image Loading:** Glide

---

## Database Architecture

### users Collection

Handles identity and routing logic.

```json
{
  "uid": "Firebase UID",
  "name": "String",
  "email": "String",
  "student_id": "String",
  "role": "customer | staff"
}
```

---

### menu_items Collection

Dynamic menu catalog and CMS source.

```json
{
  "name": "String",
  "description": "String",
  "price": "Integer",
  "category": "String",
  "imageUrl": "String",
  "isAvailable": "Boolean",
  "isBestseller": "Boolean",
  "isVegetarian": "Boolean"
}
```

---

### orders Collection

Tracks transactional and kitchen workflow state.

```json
{
  "orderId": "Firestore Document ID",
  "userId": "String",
  "status": "payment_pending | Pending | Preparing | Ready | Completed",
  "pickupTime": "String",
  "totalPrice": "Double",
  "timestamp": "Unix Epoch",
  "razorpay_payment_id": "String"
}
```

---

## Core Engineering Implementations

### Authentication & Role Routing

The application uses Firebase Authentication combined with Firestore RBAC logic.

There are no separate staff login portals.

After authentication:
- `staff` users route to the staff dashboard
- `customer` users route to the customer interface

Splash screen session checks bypass login entirely when valid tokens exist.

---

### Global Cart State (Singleton Pattern)

`CartManager.java` maintains persistent in-memory cart state across activity transitions using a Singleton architecture.

This avoids:
- oversized Intent payloads
- duplicated state
- inconsistent cart synchronization

---

### Real-Time Dashboard Analytics

`DashboardViewModel` aggregates daily metrics using timestamp-filtered Firestore listeners.

Benefits:
- live KPI updates
- rotation-safe persistence
- reduced redundant queries
- minimized UI blocking

---

### RecyclerView Lifecycle Safety

The menu management system prevents false toggle events caused by Android view recycling.

`OnCheckedChangeListener` instances are detached before programmatic updates and reattached afterward.

This guarantees:
- deterministic availability toggles
- clean synchronization with Firestore
- prevention of ghost writes

---

### Pending-First Transaction Pipeline

Orders are written to Firestore BEFORE Razorpay payment confirmation.

Flow:
1. Draft order created (`payment_pending`)
2. Razorpay transaction initiated
3. Success callback appends payment ID
4. Order promoted to active kitchen queue

This guarantees transactional traceability even if payment flow is interrupted.

---

## Project Structure

```text
Bhukkad/
├── app/
│   ├── src/main/java/com/example/bhukkad/
│   ├── src/main/res/
│   ├── google-services.json
│   └── build.gradle.kts
├── gradle/
├── build.gradle.kts
└── settings.gradle.kts
```

---

## Installation

### Prerequisites

- Android Studio
- Firebase Project
- Razorpay Developer Account
- Android SDK

---

## Firebase Setup

Create a Firebase project and enable:

- Firebase Authentication
- Firestore Database
- Firebase Storage

Register the Android app using your application ID.

Place:

```text
google-services.json
```

inside:

```text
app/
```

---

## Creating a Staff Account

1. Register normally inside the app
2. Open Firestore Console
3. Locate the user document
4. Change:

```json
"role": "customer"
```

to:

```json
"role": "staff"
```

The next login routes the user into the Staff Dashboard automatically.

---

## Razorpay Configuration

Open:

```text
PaymentActivity.java
```

Replace:

```java
rzp_test_YOUR_KEY_HERE
```

with your Razorpay test key.

---

## Firestore Rules (Static Archive Mode)

This project is configured to operate as a long-term static showcase system.

Recommended production archive rules:

```js
rules_version = '2';

service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read: if true;
      allow write: if false;
    }
  }
}
```

This preserves the application's final state permanently while preventing all external modifications.

---

## Performance Characteristics

Designed for:

- small-to-medium campus deployments
- real-time synchronization
- low-latency menu updates
- transactional traceability
- Android-native execution

Not designed for:

- distributed backend scaling
- public API exposure
- multi-tenant SaaS infrastructure

---

## Limitations

- Firebase client-side architecture
- No dedicated backend server
- No server-side payment verification
- No Cloud Functions integration
- No offline synchronization layer

---

## What This Project Demonstrates

- Real-time mobile system architecture
- Firebase NoSQL data modeling
- Android lifecycle management
- Role-Based Access Control (RBAC)
- MVVM implementation
- Payment gateway integration
- State persistence patterns
- RecyclerView lifecycle safety
- Real-time synchronization design

---

## Project Status

- Feature complete
- Stable for demonstration and academic use
- No active feature development planned

This project serves as a completed Android systems design and real-time application engineering showcase.
