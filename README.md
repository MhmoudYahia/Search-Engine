# Search-Engine
This project aims to develop a simple Crawler-based search engine demonstrating key features such as web crawling, indexing, ranking, and search functionality. The implementation is done in Java with the Spring Boot framework.
  
 ## 🧑🏼‍💻 Tech Stack 
- Java: Main programming language for the backend modules (crawler, indexer, etc.).
- Spring Boot: Framework used for building and running the backend services.
- React: JavaScript library for building the user interface and interactive components.
- MongoDB: for storing and managing the indexed data.

## Modules
### Web Crawler
The web crawler is responsible for collecting documents from the web.
Multithreaded implementation with user-controlled thread settings.
Careful handling of URL normalization and duplicate prevention.
Respect for robots.txt exclusions.
The crawler maintains state for interrupted processes. 

### Indexer
Converts downloaded HTML documents into an indexed data structure.
Persistence in secondary storage.
Optimized for fast retrieval of documents containing specific words.
Supports incremental updates with newly crawled documents.

### Query Processor
Handles search queries and performs necessary preprocessing.
Retrieves documents containing words sharing the same stem as those in the search query.

### Phrase Searching
Supports phrase searching with quotation marks.
Results with quotation marks return a subset of results without quotation marks.

### Ranker
Sorts documents based on relevance and popularity.
Calculates relevance using methods like tf-idf and aggregates scores.
Utilizes ranking algorithms for page popularity.

### Web Interface
Implements a web interface for user queries.
Displays results with snippets containing query words.
Pagination of results and a suggestion mechanism for popular query completions.

### Bonus Features
Supports AND/OR/NOT in phrase searching module (Maximum of two operations per single search).
Implementation Details

## 📱 Screenshots
![Screenshot 2023-11-27 091607](https://github.com/MhmoudYahia/Search-Engine/assets/94763036/d2c3ad27-93a2-494c-afb1-66d82e8a0b78)

![photo_2023-11-27_09-29-20](https://github.com/MhmoudYahia/Search-Engine/assets/94763036/3ef88123-dd7e-44e6-b516-deac8a1c0837)

## Project Document
![image](https://github.com/MhmoudYahia/Search-Engine/assets/94763036/9c80be16-b097-4d9a-8546-91c971f4af6a)
![image](https://github.com/MhmoudYahia/Search-Engine/assets/94763036/29bb65e9-f48b-4a7c-8ecd-2c143aff1b91)
![image](https://github.com/MhmoudYahia/Search-Engine/assets/94763036/738d99cd-f456-4902-9277-97ae7c0a7dbe)
![image](https://github.com/MhmoudYahia/Search-Engine/assets/94763036/85d780a6-55d7-496b-8333-8d1bb99a93dd)

