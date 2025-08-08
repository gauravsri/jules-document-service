# Implementation Plan

This document outlines the implementation plan for building a secure, full-stack, multi-tenant Document Management Service (DMS).

## 1. Implementation Phases

### Phase 1: Core Infrastructure & Multi-Tenancy (Sprint 1-2)
*   **Backend Tasks:**
    *   Initialize Spring Boot project, configure profiles, set up H2/Minio.
    *   Implement the `SearchService` interface and the `ZincSearchService` implementation for local development.
    *   Implement `TenantFilter` and Hibernate filters.
    *   Create initial schema.
*   **Frontend Tasks:**
    *   Initialize React/TypeScript project, set up structure and libraries.
    *   Create the main application layout and a mock login/tenant switcher.
*   **Deliverables:** A runnable backend with multi-tenancy support, a basic frontend shell, and a `docker-compose.yml` that starts a ZincSearch service for the local environment.

### Phase 2: Document & Case Management (Sprint 3-4)
*   **Backend Tasks:**
    *   Develop REST endpoints for document and case management.
    *   On document upload, use Apache Tika to extract text content and call the `SearchService` to index it in ZincSearch.
*   **Frontend Tasks:**
    *   Create a dedicated API service using Axios.
    *   Build React components for uploading documents, creating cases, and adding tags.
    *   Develop a view to display documents within a case.
*   **Deliverables:** A functional API and UI for uploading and managing documents, with content being indexed automatically.

### Phase 3: Search & Retrieval (Sprint 5)
*   **Backend Tasks:**
    *   Enhance the search endpoint to accept content search queries. It will call the `SearchService` to get matching document IDs, then fetch the full metadata for those IDs from the primary database.
*   **Frontend Tasks:**
    *   Build a search bar and filter components for both metadata and content-based searches.
    *   Create a view to display combined search results.
    *   Implement logic for document downloads.
*   **Deliverables:** A UI where users can search for documents by filename, tags, or the text inside them.

### Phase 4: Retention Policies & Security (Sprint 6-7)
*   **Backend Tasks:**
    *   Implement the retention policy service and scheduled job. When a document is deleted, ensure its corresponding entry is also removed from the search index via the `SearchService`.
    *   Implement JWT-based authentication and authorization endpoints.
*   **Frontend Tasks:**
    *   Implement the full authentication flow.
    *   Connect the tenant switcher to the API.
    *   Implement role-based rendering.
*   **Deliverables:** A secure application with a complete login flow and automated data retention across all data stores.

### Phase 5: Testing & Deployment (Sprint 8)
*   **Backend Tasks:** Write unit and integration tests. Implement the `ElasticsearchService` and configure it for the `prod` profile.
*   **Frontend Tasks:** Write component and integration tests.
*   **Deployment Tasks:**
    *   Update `Dockerfile` for the backend.
    *   Create a `Dockerfile` for the frontend.
    *   Update `docker-compose.yml` to orchestrate the local environment.
    *   Create production deployment scripts (e.g., Kubernetes manifests) that connect to a managed Elasticsearch service.
*   **Deliverables:** A fully tested and deployable full-stack application.

## 2. Local Development Environment
*   **Requirements:** Java (JDK 17+), Node.js (v18+), Maven/Gradle, Docker.
*   **Setup:** `docker-compose.yml` will orchestrate the backend services (database, object storage, ZincSearch). The React dev server will run separately.

## 3. Production Considerations
*   **Deployment:** The frontend will be served by Nginx, which will also reverse proxy to the backend.
*   **CORS:** The backend will need a CORS policy for the frontend domain.
*   **Search Engine Management:** For production, you will need to provision, configure, and monitor a managed Elasticsearch cluster (e.g., on AWS, GCP, or Elastic Cloud). This includes planning for scaling, backup, and recovery.

## 4. Risks and Mitigation
*   **Backend Risks:** Data security, performance (mitigations remain the same).
*   **Frontend Risks:** State management complexity, responsive UI (mitigations remain the same).
*   **New Risk: Content Indexing and Extraction**
    *   **Risk:** Text extraction can fail for corrupted or unsupported document formats. The search index could become out of sync with the primary database if an operation fails midway.
    *   **Mitigation:** Implement robust error handling and logging for the Tika extraction process. Use transactional logic or compensating transactions (e.g., a cleanup job) to ensure the search index and database remain consistent. This applies to both ZincSearch and Elasticsearch.
