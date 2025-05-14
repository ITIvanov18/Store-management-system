<h1 align="center">🛒 Store Management System</h1>
<br>

<p align = "center">
  <img alt="Languages" src="https://img.shields.io/github/languages/count/ITIvanov18/Store-management-system?style=for-the-badge" /> 
  <img alt="GitHub license" src="https://img.shields.io/github/license/ITIvanov18/Store-management-system?style=for-the-badge">
  <img alt="Repo Size" src="https://img.shields.io/github/repo-size/ITIvanov18/Store-management-system?style=for-the-badge" /> 
  <img alt="Last Commit" src="https://img.shields.io/github/last-commit/ITIvanov18/Store-management-system?style=for-the-badge" />
</p>

---

<div align="center">

## 📋 Table of Contents
  
<ul style="list-style:none; padding:0; text-align:center;">
  <li><a href="#description">Description</a></li>
  <li><a href="#key-features">Key Features</a></li>
  <li><a href="#tech-stack">Tech Stack</a></li>
  <li><a href="#deployment">Deployment</a></li>
</ul>

<br>

## 💻 Description <a name="description"></a>

<p align = "center">A Java-based web application for managing the inventory and sales flow of a retail store. <br>
Build with Spring Boot and Thymeleaf; backed by a Microsoft SQL Server database via <br>
Spring Data JPA (Hibernate & Jakarta EE) for seamless object-relational mapping, and Gradle for build and testing.<br>
The system supports expiry-aware pricing, real-time inventory updates, cashier workflows, receipt generation, and store statistics.
</p>

<br>

## ⚙️ Tech Stack <a name="tech-stack"></a>

<p align="center">
  <a href="https://www.jetbrains.com/idea/"><img src="https://img.icons8.com/color/96/intellij-idea.png" alt="IntelliJ Idea" width="60px"/></a>
  <a href="https://www.oracle.com/java/"><img src="https://img.icons8.com/color/96/java-coffee-cup-logo--v1.png" alt="Java 17" width="64px"/></a>
  <a href="https://spring.io/projects/spring-boot"><img src="https://img.icons8.com/color/96/spring-logo.png" alt="Spring Boot 3" width="55px"/></a> &nbsp
  <a href="https://hibernate.org/"><img src="https://icon.icepanel.io/Technology/svg/Hibernate.svg" alt="Hibernate / JPA" width="53px"/></a> &nbsp
  <a href="https://www.thymeleaf.org/"><img src="https://www.thymeleaf.org/images/thymeleaf.png" alt="Thymeleaf" width="55px"/></a> &nbsp
  <a href="https://getbootstrap.com/"><img src="https://getbootstrap.com/docs/5.0/assets/brand/bootstrap-logo.svg" alt="Bootstrap 5" width="67px"/></a> &nbsp
  <a href="https://gradle.org/"><img src="https://icon.icepanel.io/Technology/svg/Gradle.svg" alt="Gradle" width="64px"/></a>
  <a href="https://docs.microsoft.com/sql/ssms/"><img src="https://img.icons8.com/color/96/microsoft-sql-server.png" alt="SQL Server" width="60px"/></a>
  <a href="https://junit.org/junit5/"><img src="https://junit.org/junit5/assets/img/junit5-logo.png" alt="JUnit 5" width="55px"/></a>
</p>

<br>

## 📷 Project Demo


![project_demo](https://github.com/user-attachments/assets/2ec4efd7-008d-4164-b571-42a6c5d6f9a1)

<br>

## 🏷️ Key Features <a name="key-features"></a>

#### Product Management
- Each product has an ID, name, purchase price, category (FOOD / NON_FOOD), expiry date, and stock level  
- Sale price is calculated with configurable markup percentages per category, plus dynamic discounting when expiry is imminent  
- Expired products are automatically excluded from sale  

#### Cashier & Cash Register Management
- Cashiers have an ID, name, and monthly salary  
- Each cash register can host one cashier at a time; cashiers may swap registers  
- Sales are validated against customer funds; insufficient stock triggers a custom exception detailing shortages  

#### Receipt Processing
- Generates receipts with sequential numbers, cashier info, timestamp, itemized list (unit price × quantity), and total amount  
- Maintains running totals of all receipts issued and total turnover  
- Serializes each receipt to a separate text file (`receipt-{number}.txt`)

#### Store Analytics
- Tracks cumulative expenses (salaries + delivery costs) and revenues from sales  
- Computes net profit  

#### Persistence & Migrations
- Uses SQL Server (SSMS) via Spring Data JPA (Hibernate)  
- Database schema versioning with Flyway  

#### Testing & Quality
- Unit tests with JUnit 5 for business logic and exception flows  

<br>

## 📥 How to deploy <a name="deployment"></a>
<h3> <B>Clone the repository:</B> </h3>

Paste this line of code in **your terminal**:
<pre>git clone https://github.com/ITIvanov18/Store-management-system.git</pre>

</div>

---
