1. Introduction
This document outlines the implementation plan for building a secure, full-stack, multi-tenant Document Management Service (DMS). The backend will be built using Spring Boot, following SOLID principles and established design patterns. The frontend will be a modern Single Page Application (SPA) built with React and TypeScript. This plan includes a powerful content search feature using ZincSearch for local development and Elasticsearch for production, ensuring a high-quality, maintainable, and user-friendly application.

2. Project Goals
Multi-Tenancy: Support multiple tenants with strict data isolation. A user can be a member of multiple tenants but can only operate within one tenant context at a time.

Intuitive Frontend: Provide a clean, responsive, and user-friendly web interface for all document management tasks.

Document Management: Allow users to upload various document types (PDF, DOC, TXT, EML, etc.), associate them with cases/batches, and add descriptive tags.

Advanced Search: Provide a robust search functionality based on both metadata (tags, filenames) and full-text content search within the documents themselves.

Security & Compliance:

Flag documents containing Material Non-Public Information (MNPI) or other confidential data.

Implement tenant-specific document retention policies.

Scalability & Maintainability: Build a scalable and maintainable solution with a clear, layered architecture guided by SOLID principles for both frontend and backend.

Developer Experience: Provide an easy-to-set-up local development environment for the backend (H2, Minio), frontend, and search index (ZincSearch).

3. Architecture
3.1. High-Level Design
The system will consist of three main components: a Spring Boot backend, a React frontend, and a search engine instance for indexing.

Backend (REST API): A monolithic Spring Boot application responsible for all business logic, data persistence, and file storage. It will also handle text extraction and indexing.

Frontend (SPA): A React/TypeScript Single Page Application that consumes the backend API.

Search Engine:

Local/Test: ZincSearch for a lightweight, easy-to-manage local setup.

Production: Elasticsearch for its robust features, scalability, and ecosystem.

3.2. Backend Architecture
API Layer (Controllers): Handles HTTP requests, validation, and delegation.

Service Layer: Contains the core business logic.

Data Access Layer (Repositories): Interacts with the primary database.

Object Storage Service: An abstraction for handling file storage (Minio/S3).

Search Indexing Service: An abstraction (SearchService interface) responsible for communicating with the search engine. We will have two implementations:

ZincSearchService: Used when the local or test Spring profile is active.

ElasticsearchService: Used when the prod Spring profile is active.

Text Extraction Service: A dedicated service using Apache Tika to extract text from documents.

3.3. Frontend Architecture
Framework: React with TypeScript.

Component Library: Shadcn/ui or MUI.

State Management: Zustand or React Context.

API Communication: Axios with an interceptor for JWT and X-Tenant-ID headers.

Routing: React Router.

3.4. Multi-Tenancy Strategy
The strategy remains the same. The tenant_id will be included in the search index (both ZincSearch and Elasticsearch) for every document to ensure search queries are also isolated by tenant.

3.5. Technology Stack
Backend:

Spring Boot 3.x, Spring Data JPA, Hibernate, Spring Security, JWT

Utility Libraries: Lombok, Apache Tika (for text extraction), Apache Commons IO/Lang

Frontend:

React 18+, TypeScript, Vite, React Router, Axios, Zustand

UI: Shadcn/ui or MUI

Search:

ZincSearch (for local/testing)

Elasticsearch (for production)

Database: H2 (local), PostgreSQL/MySQL (prod)

Object Storage: Minio (local), AWS S3 (prod)

Build/Containerization: Maven/Gradle, Docker, Docker Compose

3.6. Design Principles & Patterns
The principles remain the same, with the new SearchService interface being a prime example of the Open/Closed and Liskov Substitution principles.

4. Database Schema
The primary database schema remains unchanged.

5. Implementation Phases
Phase 1: Core Infrastructure & Multi-Tenancy (Sprint 1-2)
Backend Tasks:

Initialize Spring Boot project, configure profiles, set up H2/Minio.

Implement the SearchService interface and the ZincSearchService implementation for local development.

Implement TenantFilter and Hibernate filters.

Create initial schema.

Frontend Tasks:

Initialize React/TypeScript project, set up structure and libraries.

Create the main application layout and a mock login/tenant switcher.

Deliverables: A runnable backend with multi-tenancy support, a basic frontend shell, and a docker-compose.yml that starts a ZincSearch service for the local environment.

Phase 2: Document & Case Management (Sprint 3-4)
Backend Tasks:

Develop REST endpoints for document and case management.

On document upload, use Apache Tika to extract text content and call the SearchService to index it in ZincSearch.

Frontend Tasks:

Create a dedicated API service using Axios.

Build React components for uploading documents, creating cases, and adding tags.

Develop a view to display documents within a case.

Deliverables: A functional API and UI for uploading and managing documents, with content being indexed automatically.

Phase 3: Search & Retrieval (Sprint 5)
Backend Tasks:

Enhance the search endpoint to accept content search queries. It will call the SearchService to get matching document IDs, then fetch the full metadata for those IDs from the primary database.

Frontend Tasks:

Build a search bar and filter components for both metadata and content-based searches.

Create a view to display combined search results.

Implement logic for document downloads.

Deliverables: A UI where users can search for documents by filename, tags, or the text inside them.

Phase 4: Retention Policies & Security (Sprint 6-7)
Backend Tasks:

Implement the retention policy service and scheduled job. When a document is deleted, ensure its corresponding entry is also removed from the search index via the SearchService.

Implement JWT-based authentication and authorization endpoints.

Frontend Tasks:

Implement the full authentication flow.

Connect the tenant switcher to the API.

Implement role-based rendering.

Deliverables: A secure application with a complete login flow and automated data retention across all data stores.

Phase 5: Testing & Deployment (Sprint 8)
Backend Tasks: Write unit and integration tests. Implement the ElasticsearchService and configure it for the prod profile.

Frontend Tasks: Write component and integration tests.

Deployment Tasks:

Update Dockerfile for the backend.

Create a Dockerfile for the frontend.

Update docker-compose.yml to orchestrate the local environment.

Create production deployment scripts (e.g., Kubernetes manifests) that connect to a managed Elasticsearch service.

Deliverables: A fully tested and deployable full-stack application.

6. Local Development Environment
Requirements: Java (JDK 17+), Node.js (v18+), Maven/Gradle, Docker.

Setup: docker-compose.yml will orchestrate the backend services (database, object storage, ZincSearch). The React dev server will run separately.

7. Production Considerations
Deployment: The frontend will be served by Nginx, which will also reverse proxy to the backend.

CORS: The backend will need a CORS policy for the frontend domain.

Search Engine Management: For production, you will need to provision, configure, and monitor a managed Elasticsearch cluster (e.g., on AWS, GCP, or Elastic Cloud). This includes planning for scaling, backup, and recovery.

8. Risks and Mitigation
Backend Risks: Data security, performance (mitigations remain the same).

Frontend Risks: State management complexity, responsive UI (mitigations remain the same).

New Risk: Content Indexing and Extraction

Risk: Text extraction can fail for corrupted or unsupported document formats. The search index could become out of sync with the primary database if an operation fails midway.

Mitigation: Implement robust error handling and logging for the Tika extraction process. Use transactional logic or compensating transactions (e.g., a cleanup job) to ensure the search index and database remain consistent. This applies to both ZincSearch and Elasticsearch.
