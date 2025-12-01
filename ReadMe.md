# Perk-Manager-Project
SYSC4806 Group 8 Project - Perk Management System - Milestone 3

## Project Overview

**Perk Manager** is a web-based application that helps users track and share discounts, benefits, and rewards linked to their memberships and credit cards.

**Users can:**
- Create a **Profile** listing their memberships and cards (Air Miles, CAA, Visa, Mastercard).
- Post **Perks** — discounts or offers tied to a product + membership (e.g., *10% off movies with a Visa card*).
- Add **expiry dates** to perks.
- **Vote** (upvote/downvote) on perks to mark them as useful or incorrect.
- **Search** for perks by product or membership.
- See **highlighted perks** that match their memberships.


---


### ✅ Implemented Features
- User registration and login with session management
- Membership management (add/remove memberships)
- Perk viewing, adding, and removing
- Search and filter functionality
- Voting system for perks (upvote/downvote)
- Personalized perk highlighting based on user memberships
- Comprehensive testing (40+ tests)
- Continuous integration with GitHub Actions
- Flyway migration to Azure SQL database
- Thymeleaf UI templates
- Dark-themed, modern interface

---


### To do for a later time/Backlog:
- Add mobile compatibility
- Clean up tests
- Add More Membership types



## 🚀 Getting Started

### Prerequisites
- Java JDK 17+
- Maven 3.6+
- Git

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/BigMozz/Perk-Manager-Project.git
   cd Perk-Manager-Project
   ```

2. **Install dependencies:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application:**
   ```
   http://localhost:8080
   ```

---



  
## Database Schema
<img width="639" height="354" alt="Screenshot 2025-11-02 at 10 38 33 PM" src="https://github.com/user-attachments/assets/768b9185-094f-4282-8de9-36129ba30b5a" />



## UML class diagram
<img width="1215" height="1909" alt="image" src="https://github.com/user-attachments/assets/238e5880-e78b-4e2b-9ac1-e6ce1dfaf38c" />

