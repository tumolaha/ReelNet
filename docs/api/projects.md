# Project Management API Documentation

## Overview

The Project Management API provides endpoints for creating, managing, and collaborating on video projects.

## Base URL

```
https://api.reelnet.com/api/v1/projects
```

## Authentication

All endpoints require authentication using JWT token:

```
Authorization: Bearer <token>
```

## Endpoints

### List Projects

Retrieve a list of projects accessible to the authenticated user.

```http
GET /projects
Authorization: Bearer <token>
```

#### Query Parameters

- `page` (integer, default: 1): Page number
- `limit` (integer, default: 20): Items per page
- `sort` (string): Sort field (created_at, updated_at, name)
- `order` (string): Sort order (asc, desc)
- `filter` (string): Filter by status (active, archived, shared)

#### Response

```json
{
  "data": [
    {
      "id": "string",
      "name": "string",
      "description": "string",
      "thumbnail": "string",
      "status": "active",
      "owner": {
        "id": "string",
        "username": "string"
      },
      "collaborators": [
        {
          "id": "string",
          "username": "string",
          "role": "editor"
        }
      ],
      "created_at": "string",
      "updated_at": "string"
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

#### Status Codes
- 200: OK
- 401: Unauthorized
- 400: Bad Request

### Create Project

Create a new video project.

```http
POST /projects
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "string",
  "description": "string",
  "thumbnail": "string (optional)",
  "settings": {
    "resolution": "1920x1080",
    "frameRate": 30,
    "codec": "h264"
  }
}
```

#### Response

```json
{
  "id": "string",
  "name": "string",
  "description": "string",
  "thumbnail": "string",
  "settings": {
    "resolution": "1920x1080",
    "frameRate": 30,
    "codec": "h264"
  },
  "created_at": "string",
  "updated_at": "string"
}
```

#### Status Codes
- 201: Created
- 400: Bad Request
- 401: Unauthorized

### Get Project Details

Retrieve detailed information about a specific project.

```http
GET /projects/{projectId}
Authorization: Bearer <token>
```

#### Response

```json
{
  "id": "string",
  "name": "string",
  "description": "string",
  "thumbnail": "string",
  "status": "active",
  "settings": {
    "resolution": "1920x1080",
    "frameRate": 30,
    "codec": "h264"
  },
  "owner": {
    "id": "string",
    "username": "string"
  },
  "collaborators": [
    {
      "id": "string",
      "username": "string",
      "role": "editor",
      "joined_at": "string"
    }
  ],
  "assets": [
    {
      "id": "string",
      "type": "video",
      "url": "string",
      "metadata": {}
    }
  ],
  "timeline": {
    "id": "string",
    "tracks": [],
    "duration": 0
  },
  "created_at": "string",
  "updated_at": "string"
}
```

#### Status Codes
- 200: OK
- 401: Unauthorized
- 403: Forbidden
- 404: Not Found

### Update Project

Update project details.

```http
PUT /projects/{projectId}
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "string",
  "description": "string",
  "thumbnail": "string",
  "settings": {
    "resolution": "1920x1080",
    "frameRate": 30,
    "codec": "h264"
  }
}
```

#### Response

```json
{
  "id": "string",
  "name": "string",
  "description": "string",
  "thumbnail": "string",
  "settings": {
    "resolution": "1920x1080",
    "frameRate": 30,
    "codec": "h264"
  },
  "updated_at": "string"
}
```

#### Status Codes
- 200: OK
- 400: Bad Request
- 401: Unauthorized
- 403: Forbidden
- 404: Not Found

### Delete Project

Delete a project.

```http
DELETE /projects/{projectId}
Authorization: Bearer <token>
```

#### Response

```json
{
  "message": "Project successfully deleted"
}
```

#### Status Codes
- 200: OK
- 401: Unauthorized
- 403: Forbidden
- 404: Not Found

### Project Collaboration

#### Add Collaborator

```http
POST /projects/{projectId}/collaborators
Authorization: Bearer <token>
Content-Type: application/json

{
  "userId": "string",
  "role": "editor"
}
```

#### Response

```json
{
  "id": "string",
  "username": "string",
  "role": "editor",
  "joined_at": "string"
}
```

#### Remove Collaborator

```http
DELETE /projects/{projectId}/collaborators/{userId}
Authorization: Bearer <token>
```

### Project Assets

#### Upload Asset

```http
POST /projects/{projectId}/assets
Authorization: Bearer <token>
Content-Type: multipart/form-data

file: <file>
type: "video"
metadata: {}
```

#### Response

```json
{
  "id": "string",
  "type": "video",
  "url": "string",
  "metadata": {},
  "uploaded_at": "string"
}
```

#### List Assets

```http
GET /projects/{projectId}/assets
Authorization: Bearer <token>
```

### Project Timeline

#### Save Timeline

```http
PUT /projects/{projectId}/timeline
Authorization: Bearer <token>
Content-Type: application/json

{
  "tracks": [
    {
      "id": "string",
      "type": "video",
      "clips": [
        {
          "id": "string",
          "assetId": "string",
          "startTime": 0,
          "duration": 0,
          "effects": []
        }
      ]
    }
  ]
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

- `PROJECT_NOT_FOUND`: Project does not exist
- `INSUFFICIENT_PERMISSIONS`: User lacks required permissions
- `INVALID_REQUEST`: Invalid request parameters
- `STORAGE_LIMIT_EXCEEDED`: Project storage limit exceeded
- `INVALID_FILE_TYPE`: Unsupported file type

## Rate Limiting

- Standard endpoints: 100 requests per minute
- Asset upload: 10 uploads per minute
- Timeline updates: 60 updates per minute

## Example Usage

### JavaScript/TypeScript

```typescript
// Create a new project
const createProject = async (token: string, projectData: ProjectData) => {
  const response = await fetch('https://api.reelnet.com/api/v1/projects', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(projectData),
  });
  
  return response.json();
};

// Upload project asset
const uploadAsset = async (token: string, projectId: string, file: File) => {
  const formData = new FormData();
  formData.append('file', file);
  
  const response = await fetch(`https://api.reelnet.com/api/v1/projects/${projectId}/assets`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
    },
    body: formData,
  });
  
  return response.json();
};
```

### cURL

```bash
# List projects
curl https://api.reelnet.com/api/v1/projects \
  -H "Authorization: Bearer <token>"

# Create project
curl -X POST https://api.reelnet.com/api/v1/projects \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"name":"My Project","description":"Project description"}'
```

For more information about project management features, refer to:
- [Project Management Features](../features/project-management/)
- [Collaboration Features](../features/real-time-collaboration/) 