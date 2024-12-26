# Real-time Collaboration API Documentation

## Overview

The Real-time Collaboration API enables multiple users to work together on video projects simultaneously using WebSocket connections.

## WebSocket Connection

### Base URL

```
wss://api.reelnet.com/api/v1/collaboration
```

### Connection Parameters

```
?token=<jwt_token>&projectId=<project_id>
```

## Authentication

All WebSocket connections require authentication using JWT token as a query parameter or in the connection headers:

```
Authorization: Bearer <token>
```

## Message Format

All messages follow this standard format:

```json
{
  "type": "string",
  "payload": {},
  "timestamp": "string",
  "userId": "string"
}
```

## Events

### Connection Events

#### Join Project

```json
// Client -> Server
{
  "type": "join_project",
  "payload": {
    "projectId": "string",
    "userId": "string"
  }
}

// Server -> Client (Broadcast)
{
  "type": "user_joined",
  "payload": {
    "userId": "string",
    "username": "string",
    "role": "string",
    "timestamp": "string"
  }
}
```

#### Leave Project

```json
// Client -> Server
{
  "type": "leave_project",
  "payload": {
    "projectId": "string",
    "userId": "string"
  }
}

// Server -> Client (Broadcast)
{
  "type": "user_left",
  "payload": {
    "userId": "string",
    "timestamp": "string"
  }
}
```

### Timeline Events

#### Timeline Update

```json
// Client -> Server
{
  "type": "timeline_update",
  "payload": {
    "projectId": "string",
    "timeline": {
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
      ],
      "version": "string"
    }
  }
}

// Server -> Client (Broadcast)
{
  "type": "timeline_updated",
  "payload": {
    "timeline": {},
    "userId": "string",
    "timestamp": "string",
    "version": "string"
  }
}
```

#### Clip Operations

```json
// Add Clip
{
  "type": "add_clip",
  "payload": {
    "trackId": "string",
    "clip": {
      "id": "string",
      "assetId": "string",
      "startTime": 0,
      "duration": 0
    }
  }
}

// Move Clip
{
  "type": "move_clip",
  "payload": {
    "clipId": "string",
    "trackId": "string",
    "startTime": 0
  }
}

// Delete Clip
{
  "type": "delete_clip",
  "payload": {
    "clipId": "string",
    "trackId": "string"
  }
}
```

### Asset Events

#### Asset Upload Started

```json
// Server -> Client (Broadcast)
{
  "type": "asset_upload_started",
  "payload": {
    "assetId": "string",
    "userId": "string",
    "metadata": {
      "filename": "string",
      "size": 0,
      "type": "string"
    }
  }
}
```

#### Asset Upload Complete

```json
// Server -> Client (Broadcast)
{
  "type": "asset_upload_complete",
  "payload": {
    "assetId": "string",
    "url": "string",
    "metadata": {
      "duration": 0,
      "resolution": "string",
      "codec": "string"
    }
  }
}
```

### Cursor Events

#### Cursor Update

```json
// Client -> Server
{
  "type": "cursor_update",
  "payload": {
    "position": {
      "x": 0,
      "y": 0
    },
    "timestamp": "string"
  }
}

// Server -> Client (Broadcast)
{
  "type": "cursor_updated",
  "payload": {
    "userId": "string",
    "position": {
      "x": 0,
      "y": 0
    }
  }
}
```

### Chat Events

#### Send Message

```json
// Client -> Server
{
  "type": "chat_message",
  "payload": {
    "content": "string",
    "timestamp": "string"
  }
}

// Server -> Client (Broadcast)
{
  "type": "chat_message_received",
  "payload": {
    "userId": "string",
    "username": "string",
    "content": "string",
    "timestamp": "string"
  }
}
```

## Error Handling

### Error Message Format

```json
{
  "type": "error",
  "payload": {
    "code": "string",
    "message": "string",
    "details": {}
  }
}
```

### Common Error Codes

- `CONNECTION_ERROR`: WebSocket connection error
- `AUTHENTICATION_ERROR`: Invalid or expired token
- `PERMISSION_ERROR`: Insufficient permissions
- `VERSION_CONFLICT`: Timeline version conflict
- `INVALID_MESSAGE`: Invalid message format

## Client Implementation

### JavaScript/TypeScript Example

```typescript
class CollaborationClient {
  private ws: WebSocket;
  private projectId: string;
  private token: string;

  constructor(projectId: string, token: string) {
    this.projectId = projectId;
    this.token = token;
    this.connect();
  }

  private connect() {
    this.ws = new WebSocket(
      `wss://api.reelnet.com/api/v1/collaboration?token=${this.token}&projectId=${this.projectId}`
    );

    this.ws.onopen = () => {
      this.sendMessage({
        type: 'join_project',
        payload: {
          projectId: this.projectId,
          userId: getCurrentUserId()
        }
      });
    };

    this.ws.onmessage = (event) => {
      const message = JSON.parse(event.data);
      this.handleMessage(message);
    };

    this.ws.onerror = (error) => {
      console.error('WebSocket error:', error);
    };

    this.ws.onclose = () => {
      // Implement reconnection logic
      setTimeout(() => this.connect(), 5000);
    };
  }

  private handleMessage(message: any) {
    switch (message.type) {
      case 'timeline_updated':
        this.handleTimelineUpdate(message.payload);
        break;
      case 'user_joined':
        this.handleUserJoined(message.payload);
        break;
      case 'cursor_updated':
        this.handleCursorUpdate(message.payload);
        break;
      // Handle other message types
    }
  }

  public updateTimeline(timeline: any) {
    this.sendMessage({
      type: 'timeline_update',
      payload: {
        projectId: this.projectId,
        timeline
      }
    });
  }

  private sendMessage(message: any) {
    if (this.ws.readyState === WebSocket.OPEN) {
      this.ws.send(JSON.stringify(message));
    }
  }
}
```

## Best Practices

1. **Connection Management**
   - Implement reconnection logic with exponential backoff
   - Handle connection errors gracefully
   - Monitor connection health with heartbeats

2. **State Synchronization**
   - Implement Operational Transformation or CRDT for conflict resolution
   - Maintain version history for timeline changes
   - Handle version conflicts appropriately

3. **Performance Optimization**
   - Batch cursor updates
   - Implement rate limiting for message sending
   - Use compression for large messages

4. **Error Handling**
   - Implement proper error recovery
   - Log errors for debugging
   - Provide meaningful error messages to users

## Rate Limiting

- Cursor updates: 10 updates per second
- Chat messages: 10 messages per minute
- Timeline updates: 60 updates per minute

For more information about collaboration features, refer to:
- [Real-time Collaboration Features](../features/real-time-collaboration/)
- [Project Management](../features/project-management/) 