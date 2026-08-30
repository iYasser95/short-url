# Short URL

A simple URL shortening service built to explore backend architecture.

The service generates short URLs for long-form URLs and redirects users to the original destination when the generated URL is accessed.

## Quick Start

### Clone the repository

```bash
git clone https://github.com/iYasser95/short-url.git
cd shorturl
```

### Create environment file

```bash
cp example.env .env
```

### Start the application

```bash
docker compose up --build
```

### Open the application

```bash
http://localhost:5173
```

## Features

- Generate 6-character Base62 short URLs.
- Redirect short URLs to their original destination.
- Prevent duplicate short URLs.
- Postgres persistence.
- Redis caching for faster redirections.
- Lazy deletion for expired URLs when accessed.
- Scheduled cleanup for expired URLs.

## Tech Stack

### Backend

- Spring Boot
- Postgres
- Redis
- Maven

### Web

- React
- TypeScript

### Infrastructure

- Docker
- Docker Compose

## Architecture

### Flowchart

```mermaid
flowchart LR
    A[User] --> B[Web Application]
    B --> C[Backend]
    C --> D[(Redis)]
    C --> E[(Postgres)]
```

#### Sequence - Resolve Short URL

```mermaid
sequenceDiagram
    User ->>+ Backend: GET /{code}
    Backend ->> Redis: Get URL from cache
    Redis -->> Backend: Return response

    alt URL exists in cache
        Backend -->> User: Redirect to original URL
    else URL not found in cache
        Backend ->> Database: Get Short URL
        Database -->> Backend: Return response

        alt URL not found
            Backend -->> User: 404 Not Found
        else URL expired
            Backend ->> Database: Delete expired URL
            Backend -->> User: 404 Not Found
        else URL is valid
            Backend ->> Redis: Save URL in cache
            Backend -->> User: Redirect to original URL
        end
    end
```

#### Sequence - Create Short URL

```mermaid
sequenceDiagram
    User ->>+ Web: Request Short URL
    Web ->>+ Backend: POST /short-urls
    Backend ->> Backend: Get URL from request body
    Backend ->> Database: Check if URL already exists
    Database -->> Backend: Return response

    alt URL exists
        Backend ->> Backend: Check URL expiry

        alt URL not expired
            Backend ->> Redis: Store in cache with remaining TTL
            Redis -->> Backend: Successful response
            Backend -->> Web: Return Short URL
            Web -->> User: Show Short URL
        else URL expired
            Backend ->> Database: Delete expired URL
            Database -->> Backend: Successful response
            Backend ->> Backend: Continue with URL creation
        end
    end

    opt URL does not exist or expired URL was deleted
        Backend ->> Backend: Generate 6-character Base62 code
        Backend ->> Database: Check if code exists
        Database -->> Backend: Return response

        loop While code exists
            Backend ->> Backend: Generate new Base62 code
            Backend ->> Database: Check if code exists
            Database -->> Backend: Return response
        end

        Backend ->> Database: Store URL + Base62 code + expiration timestamp
        Database -->> Backend: Successful response
        Backend ->> Redis: Store in cache with TTL
        Redis -->> Backend: Successful response
        Backend -->> Web: Return Short URL
        Web -->> User: Show Short URL
    end
```

#### Sequence - Scheduler

```mermaid
sequenceDiagram
    Scheduler ->> Database: Delete expired URLs
    Database -->> Scheduler: Return deleted count
```

## Architecture Journey

If you want to follow the architecture evolution of this project, including the design decisions and changes made across each version, you can view the full project journey on my website:

[yasserjaffer.com](https://yasserjaffer.com)

## License

This project is licensed under the MIT License.
