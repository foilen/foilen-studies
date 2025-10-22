# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.3 + React language learning application that provides vocabulary study, verb conjugation practice, and multiplication exercises. It uses MongoDB for persistence, Google Cloud Text-to-Speech for audio generation, and Spring AI (OpenAI-compatible) for content generation.

## Build and Development Commands

### Backend (Spring Boot)

Build the full application (includes compiling React UI):
```bash
./gradlew build
```

Skip the Node.js/React build during Gradle builds:
```bash
SKIP_NODEJS=1 ./gradlew build
```

Run tests:
```bash
./gradlew test
```

Run the Spring Boot application:
```bash
./gradlew bootRun
```

### Frontend (React)

The React application is located in `src/main/ui/`.

Install dependencies:
```bash
cd src/main/ui
npm install
```

Watch mode for development (auto-compiles to `build/resources/main/static`):
```bash
cd src/main/ui
npm run watch
```

Run React dev server standalone:
```bash
cd src/main/ui
npm start
```

Build production bundle:
```bash
cd src/main/ui
npm run build
```

Run React tests:
```bash
cd src/main/ui
npm test
```

Run React tests in CI mode (non-interactive):
```bash
cd src/main/ui
npm run test:ci
```

## Architecture

### Backend Layer Structure

The backend follows a three-layer architecture:

1. **Controllers** (`com.foilen.studies.controllers`)
   - REST API endpoints
   - Handle HTTP requests/responses
   - Use Spring Security Authentication for user context
   - All endpoints (except public ones) require authentication

2. **Managers** (`com.foilen.studies.managers`)
   - Business logic layer
   - Orchestrate between services and repositories
   - Examples: `WordManager`, `VerbManager`, `MultiplicationManager`, `UserManager`

3. **Data/Repositories** (`com.foilen.studies.data`)
   - MongoDB repository interfaces (Spring Data MongoDB)
   - Custom repository implementations for complex queries (e.g., `WordRepositoryImpl`)
   - Domain models: `Word`, `WordList`, `Verb`, `UserScores`, `MultiplicationScores`

4. **Services** (`com.foilen.studies.services`)
   - External integrations (Google TTS, OpenAI-compatible AI)
   - `SpeechService`: Text-to-speech audio generation
   - `AiGenerationService`: AI-powered sentence generation for vocabulary

### Data Organization

Data models are organized by feature domain:
- `data/vocabulary/`: Word, WordList, Language, SpeakText, UserScores, MultiplicationScores
- `data/verb/`: Verb, VerbLine
- `data/user/`: UserRepository

### Frontend Structure

React SPA using React Router v6 with HashRouter:
- `src/home/`: Home page
- `src/vocabulary/`: Vocabulary list management and practice
- `src/verb/`: Verb conjugation practice
- `src/multiplication/`: Multiplication exercises
- `src/layout/`: Header, Footer components
- `src/common/`: Shared utilities and components
- `service.js`: Centralized Axios-based API client

### Authentication

The application supports two authentication modes:

1. **OAuth2 (Production)**: Azure Active Directory integration
2. **Local Auth (Development)**: Simple authentication filter that auto-authenticates a user

To enable local authentication for development, set in `application.properties`:
```properties
app.auth.local.enabled: true
app.auth.local.userId: local-user
```

The `LocalAuthenticationFilter` (in `security/` package) bypasses OAuth2 when enabled.

### Configuration

Configuration is loaded from `application.properties` in the working directory (not version controlled).
Sample configuration files are in `sample_config/`.

Key configuration areas:
- MongoDB connection (Atlas or local)
- OAuth2 client credentials (Azure AD)
- Google Cloud service account for TTS
- Spring AI OpenAI-compatible endpoint
- Local authentication toggle

### Scheduled Tasks

The application includes scheduled tasks in the `scheduledtasks` package for:
- Cleaning up cached speech files
- Generating AI sentences for vocabulary words

### Upgrade Tasks

The `upgradetasks` package contains one-time migration tasks that run on application startup to update existing data.

## Testing

Tests use JUnit 5 and Spring Boot Test with embedded MongoDB (`de.flapdoodle.embed.mongo`).

Test structure mirrors main source:
- `managers/`: Business logic unit tests
- `services/`: Service layer tests
- `data/`: Custom repository implementation tests
- `fakedata/`: Test data loaders

## React Build Integration

The Gradle build is configured to automatically:
1. Download Node.js (via `com.github.node-gradle.node` plugin)
2. Run `npm install`
3. Run `npm run build`
4. Copy build output to `build/resources/main/static`

For development workflow:
- Run `npm run watch` in `src/main/ui/` to auto-compile on file changes
- Run the Spring Boot application separately
- The watch task outputs directly to `build/resources/main/static` which Spring Boot serves

## Key Dependencies

**Backend:**
- Spring Boot 3.3.3 (Java 21)
- Spring Data MongoDB
- Spring Security OAuth2 Client
- Spring AI (OpenAI integration)
- Google Cloud Text-to-Speech
- Foilen's jl-smalltools-mongodb-5-spring

**Frontend:**
- React 18.3
- React Router 6.26
- Bootstrap 5.3
- Axios
- React Toastify
