# Use the API

First, get the token from Keycloak:

1. **Obtain Access Token:**
   - Use a tool like Postman or Thunder Client to make the request.
   - Set the request type to `POST`.
   - Set the URL to `http://localhost:9090/realms/resume-builder/protocol/openid-connect/token`.
   - In the body, use `x-www-form-urlencoded` format and include the following parameters:
     - `client_id`: `rb`
     - `username`: `test@example.com`
     - `password`: `password`
     - `grant_type`: `password`

2. **Response:**
   - The response will contain an access token. It will look something like this:
     ```json
     {
       "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
       "expires_in": 300,
       "refresh_expires_in": 1800,
       "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
       "token_type": "Bearer",
       "not-before-policy": 0,
       "session_state": "d3e8b9e2-3e8b-4e8b-8e8b-8e8b8e8b8e8b",
       "scope": "email profile"
     }
     ```

3. **Use the Token to Access the API:**
   - Include the access token in the `Authorization` header of your API requests.
   - Example header: `Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...`

4. **Example API Request:**
   - Make a GET request to your API endpoint (e.g., `http://localhost:8080/api/v1/resumes/1`).
   - Include the `Authorization` header with the access token.

5. **Example API Response:**
   - The API will respond with the requested data if the token is valid:
    ```json
    {
        "id": 1,
        "title": "Software Engineer",
        "firstName": "John",
        "lastName": "Doe",
        "picture": null,
        "email": null,
        "phone": null,
        "address": null,
        "website": null,
        "linkedIn": null,
        "github": null,
        "instagram": null,
        "facebook": null,
        "createdDate": "2024-11-20T15:21:42.460682",
        "lastModifiedDate": "2024-11-20T15:24:59.783256"
    }
    ```