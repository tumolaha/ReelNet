# Social Features API Documentation

## Overview

The Social Features API enables user interactions, content sharing, and community features within the ReelNet platform.

## Base URL

```
https://api.reelnet.com/api/v1/social
```

## Authentication

All endpoints require authentication using JWT token:

```
Authorization: Bearer <token>
```

## Endpoints

### Profile Management

#### Get User Profile

```http
GET /profiles/{username}
```

#### Response

```json
{
  "username": "string",
  "displayName": "string",
  "bio": "string",
  "avatar": "string",
  "coverImage": "string",
  "stats": {
    "followers": 0,
    "following": 0,
    "projects": 0,
    "likes": 0
  },
  "badges": [
    {
      "id": "string",
      "name": "string",
      "icon": "string"
    }
  ],
  "social": {
    "website": "string",
    "twitter": "string",
    "instagram": "string",
    "youtube": "string"
  }
}
```

#### Update Profile

```http
PUT /profiles
Content-Type: application/json

{
  "displayName": "string",
  "bio": "string",
  "social": {
    "website": "string",
    "twitter": "string",
    "instagram": "string",
    "youtube": "string"
  }
}
```

### Following

#### Follow User

```http
POST /follow/{username}
```

#### Response

```json
{
  "status": "following",
  "follower": "string",
  "following": "string",
  "timestamp": "string"
}
```

#### Unfollow User

```http
DELETE /follow/{username}
```

#### Get Followers

```http
GET /profiles/{username}/followers
```

#### Response

```json
{
  "data": [
    {
      "username": "string",
      "displayName": "string",
      "avatar": "string",
      "followedAt": "string"
    }
  ],
  "pagination": {
    "total": 0,
    "page": 1,
    "limit": 20,
    "pages": 0
  }
}
```

### Content Sharing

#### Share Project

```http
POST /share
Content-Type: application/json

{
  "projectId": "string",
  "visibility": "public",
  "description": "string",
  "tags": ["string"],
  "allowComments": true
}
```

#### Response

```json
{
  "id": "string",
  "project": {
    "id": "string",
    "name": "string",
    "thumbnail": "string"
  },
  "author": {
    "username": "string",
    "displayName": "string"
  },
  "stats": {
    "views": 0,
    "likes": 0,
    "comments": 0,
    "shares": 0
  },
  "created_at": "string"
}
```

### Feed

#### Get User Feed

```http
GET /feed
```

#### Query Parameters

- `page` (integer, default: 1): Page number
- `limit` (integer, default: 20): Items per page
- `filter` (string): Content type filter (all, projects, updates)

#### Response

```json
{
  "data": [
    {
      "type": "project_share",
      "id": "string",
      "content": {
        "project": {},
        "description": "string",
        "tags": []
      },
      "author": {
        "username": "string",
        "displayName": "string",
        "avatar": "string"
      },
      "stats": {
        "views": 0,
        "likes": 0,
        "comments": 0
      },
      "created_at": "string"
    }
  ],
  "pagination": {
    "total": 0,
    "page": 1,
    "limit": 20,
    "pages": 0
  }
}
```

### Interactions

#### Like Content

```http
POST /like
Content-Type: application/json

{
  "contentType": "project",
  "contentId": "string"
}
```

#### Response

```json
{
  "status": "liked",
  "contentType": "project",
  "contentId": "string",
  "timestamp": "string"
}
```

#### Add Comment

```http
POST /comments
Content-Type: application/json

{
  "contentType": "project",
  "contentId": "string",
  "text": "string",
  "parentId": "string"
}
```

#### Response

```json
{
  "id": "string",
  "text": "string",
  "author": {
    "username": "string",
    "displayName": "string",
    "avatar": "string"
  },
  "created_at": "string"
}
```

### Notifications

#### Get Notifications

```http
GET /notifications
```

#### Response

```json
{
  "data": [
    {
      "id": "string",
      "type": "follow",
      "actor": {
        "username": "string",
        "displayName": "string",
        "avatar": "string"
      },
      "content": {},
      "read": false,
      "created_at": "string"
    }
  ],
  "pagination": {
    "total": 0,
    "page": 1,
    "limit": 20,
    "pages": 0
  }
}
```

#### Mark Notifications as Read

```http
POST /notifications/read
Content-Type: application/json

{
  "notificationIds": ["string"]
}
```

### Discovery

#### Search Users

```http
GET /search/users
```

#### Query Parameters

- `query` (string): Search query
- `page` (integer, default: 1): Page number
- `limit` (integer, default: 20): Items per page

#### Response

```json
{
  "data": [
    {
      "username": "string",
      "displayName": "string",
      "avatar": "string",
      "bio": "string",
      "stats": {
        "followers": 0,
        "projects": 0
      }
    }
  ],
  "pagination": {
    "total": 0,
    "page": 1,
    "limit": 20,
    "pages": 0
  }
}
```

#### Trending Projects

```http
GET /trending/projects
```

#### Query Parameters

- `period` (string): Time period (day, week, month)
- `category` (string): Project category

#### Response

```json
{
  "data": [
    {
      "id": "string",
      "name": "string",
      "thumbnail": "string",
      "author": {
        "username": "string",
        "displayName": "string"
      },
      "stats": {
        "views": 0,
        "likes": 0
      }
    }
  ]
}
```

## WebSocket Events

### Real-time Notifications

```json
{
  "type": "notification",
  "payload": {
    "id": "string",
    "type": "like",
    "actor": {
      "username": "string",
      "displayName": "string"
    },
    "content": {},
    "timestamp": "string"
  }
}
```

## Error Responses

All error responses follow the format:

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

- `USER_NOT_FOUND`: User does not exist
- `ALREADY_FOLLOWING`: Already following user
- `CONTENT_NOT_FOUND`: Content does not exist
- `INVALID_OPERATION`: Invalid social operation
- `PERMISSION_DENIED`: Insufficient permissions

## Rate Limiting

- Profile updates: 10 requests per hour
- Follow/Unfollow: 50 requests per hour
- Comments: 100 requests per hour
- Likes: 200 requests per hour

## Example Usage

### JavaScript/TypeScript

```typescript
// Follow a user
const followUser = async (username: string) => {
  const response = await fetch(`https://api.reelnet.com/api/v1/social/follow/${username}`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
    },
  });
  
  return response.json();
};

// Share a project
const shareProject = async (projectData: ProjectShareData) => {
  const response = await fetch('https://api.reelnet.com/api/v1/social/share', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(projectData),
  });
  
  return response.json();
};
```

### cURL

```bash
# Get user profile
curl https://api.reelnet.com/api/v1/social/profiles/username \
  -H "Authorization: Bearer <token>"

# Share project
curl -X POST https://api.reelnet.com/api/v1/social/share \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"projectId":"123","visibility":"public","description":"Check out my new project!"}'
```

## Best Practices

1. **Content Moderation**
   - Implement content filtering
   - Report inappropriate content
   - User blocking capabilities
   - Comment moderation

2. **Performance**
   - Cache user profiles
   - Optimize feed queries
   - Batch notifications
   - Implement pagination

3. **User Experience**
   - Real-time updates
   - Optimistic UI updates
   - Proper error handling
   - Loading states

For more information about social features, refer to:
- [Social Features Overview](../features/social-features/)
- [User Guide](../user-guide/) 