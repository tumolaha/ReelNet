# Authentication API Documentation

## Overview

The Authentication API provides endpoints for user registration, login, token refresh, and password management.

## Base URL

```
https://api.reelnet.com/api/v1/auth
```

## Authentication

All authenticated endpoints require a valid JWT token in the Authorization header:

```
Authorization: Bearer <token>
```

## Endpoints

### Register User

Register a new user account.

```http
POST /register
Content-Type: application/json

{
  "username": "string",
  "email": "string",
  "password": "string"
}
```

#### Response

```json
{
  "id": "string",
  "username": "string",
  "email": "string",
  "token": "string"
}
```

#### Status Codes
- 201: Created
- 400: Bad Request
- 409: Conflict (Username/Email already exists)

### Login

Authenticate a user and receive a JWT token.

```http
POST /login
Content-Type: application/json

{
  "email": "string",
  "password": "string"
}
```

#### Response

```json
{
  "token": "string",
  "refreshToken": "string",
  "user": {
    "id": "string",
    "username": "string",
    "email": "string"
  }
}
```

#### Status Codes
- 200: OK
- 401: Unauthorized
- 400: Bad Request

### Refresh Token

Obtain a new access token using a refresh token.

```http
POST /refresh
Content-Type: application/json

{
  "refreshToken": "string"
}
```

#### Response

```json
{
  "token": "string",
  "refreshToken": "string"
}
```

#### Status Codes
- 200: OK
- 401: Unauthorized
- 400: Bad Request

### Logout

Invalidate the current session.

```http
POST /logout
Authorization: Bearer <token>
```

#### Response

```json
{
  "message": "Successfully logged out"
}
```

#### Status Codes
- 200: OK
- 401: Unauthorized

### Request Password Reset

Request a password reset link.

```http
POST /password/reset-request
Content-Type: application/json

{
  "email": "string"
}
```

#### Response

```json
{
  "message": "Password reset email sent"
}
```

#### Status Codes
- 200: OK
- 404: Not Found
- 400: Bad Request

### Reset Password

Reset password using reset token.

```http
POST /password/reset
Content-Type: application/json

{
  "token": "string",
  "newPassword": "string"
}
```

#### Response

```json
{
  "message": "Password successfully reset"
}
```

#### Status Codes
- 200: OK
- 400: Bad Request
- 401: Invalid or expired token

### Change Password

Change password for authenticated user.

```http
POST /password/change
Authorization: Bearer <token>
Content-Type: application/json

{
  "currentPassword": "string",
  "newPassword": "string"
}
```

#### Response

```json
{
  "message": "Password successfully changed"
}
```

#### Status Codes
- 200: OK
- 400: Bad Request
- 401: Unauthorized

### Get Current User

Get the currently authenticated user's information.

```http
GET /me
Authorization: Bearer <token>
```

#### Response

```json
{
  "id": "string",
  "username": "string",
  "email": "string",
  "createdAt": "string",
  "updatedAt": "string"
}
```

#### Status Codes
- 200: OK
- 401: Unauthorized

## Error Responses

All endpoints return errors in the following format:

```json
{
  "error": {
    "code": "string",
    "message": "string",
    "details": {}
  }
}
```

### Common Error Codes

- `INVALID_CREDENTIALS`: Invalid username/password combination
- `TOKEN_EXPIRED`: JWT token has expired
- `INVALID_TOKEN`: Invalid JWT token
- `USER_EXISTS`: Username or email already registered
- `VALIDATION_ERROR`: Request validation failed
- `SERVER_ERROR`: Internal server error

## Rate Limiting

Authentication endpoints are rate limited to prevent abuse:

- Registration: 5 requests per hour per IP
- Login: 10 requests per minute per IP
- Password Reset: 3 requests per hour per email

## Security Considerations

1. **Password Requirements**
   - Minimum 8 characters
   - At least one uppercase letter
   - At least one lowercase letter
   - At least one number
   - At least one special character

2. **Token Management**
   - Access tokens expire after 15 minutes
   - Refresh tokens expire after 7 days
   - Tokens are invalidated on logout

3. **Security Headers**
   - CORS configuration
   - XSS protection
   - CSRF protection

## Example Usage

### JavaScript/TypeScript

```typescript
// Login Example
const login = async (email: string, password: string) => {
  const response = await fetch('https://api.reelnet.com/api/v1/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ email, password }),
  });
  
  if (!response.ok) {
    throw new Error('Login failed');
  }
  
  return response.json();
};

// Using the token
const getUser = async (token: string) => {
  const response = await fetch('https://api.reelnet.com/api/v1/auth/me', {
    headers: {
      'Authorization': `Bearer ${token}`,
    },
  });
  
  return response.json();
};
```

### cURL

```bash
# Login
curl -X POST https://api.reelnet.com/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password123"}'

# Get User Info
curl https://api.reelnet.com/api/v1/auth/me \
  -H "Authorization: Bearer <token>"
```

For more information about security and implementation details, refer to:
- [Security Documentation](../technical/security.md)
- [API Overview](./README.md) 