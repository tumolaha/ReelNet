# Video Processing API Documentation

## Overview

The Video Processing API provides endpoints for video upload, transcoding, editing, and rendering operations.

## Base URL

```
https://api.reelnet.com/api/v1/video
```

## Authentication

All endpoints require authentication using JWT token:

```
Authorization: Bearer <token>
```

## Endpoints

### Upload Video

Upload a video file for processing.

```http
POST /upload
Content-Type: multipart/form-data

file: <video_file>
metadata: {
  "projectId": "string",
  "name": "string",
  "description": "string"
}
```

#### Response

```json
{
  "id": "string",
  "status": "uploading",
  "metadata": {
    "filename": "string",
    "size": 0,
    "type": "string",
    "duration": 0
  },
  "uploadUrl": "string",
  "created_at": "string"
}
```

#### Status Codes
- 201: Created
- 400: Bad Request
- 401: Unauthorized
- 413: Payload Too Large

### Get Upload Status

Check the status of an uploaded video.

```http
GET /upload/{uploadId}/status
```

#### Response

```json
{
  "id": "string",
  "status": "processing",
  "progress": 0.75,
  "metadata": {
    "filename": "string",
    "size": 0,
    "duration": 0,
    "resolution": "1920x1080",
    "codec": "h264",
    "bitrate": "5000k"
  }
}
```

### Transcode Video

Request video transcoding with specific settings.

```http
POST /transcode
Content-Type: application/json

{
  "videoId": "string",
  "settings": {
    "format": "mp4",
    "codec": "h264",
    "resolution": "1920x1080",
    "bitrate": "5000k",
    "fps": 30
  },
  "output": {
    "filename": "string",
    "destination": "s3"
  }
}
```

#### Response

```json
{
  "id": "string",
  "status": "queued",
  "settings": {
    "format": "mp4",
    "codec": "h264",
    "resolution": "1920x1080",
    "bitrate": "5000k",
    "fps": 30
  },
  "progress": 0,
  "created_at": "string"
}
```

### Get Transcode Status

Check the status of a transcoding job.

```http
GET /transcode/{jobId}/status
```

#### Response

```json
{
  "id": "string",
  "status": "processing",
  "progress": 0.5,
  "output": {
    "url": "string",
    "size": 0,
    "duration": 0
  },
  "created_at": "string",
  "updated_at": "string"
}
```

### Apply Effects

Apply video effects to a clip.

```http
POST /effects
Content-Type: application/json

{
  "videoId": "string",
  "effects": [
    {
      "type": "filter",
      "name": "brightness",
      "params": {
        "value": 1.2
      }
    },
    {
      "type": "transition",
      "name": "fade",
      "params": {
        "duration": 1.5
      }
    }
  ],
  "timeRange": {
    "start": 0,
    "end": 10
  }
}
```

#### Response

```json
{
  "id": "string",
  "status": "processing",
  "effects": [],
  "progress": 0,
  "created_at": "string"
}
```

### Render Timeline

Render a project timeline.

```http
POST /render
Content-Type: application/json

{
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
    ]
  },
  "output": {
    "format": "mp4",
    "resolution": "1920x1080",
    "frameRate": 30,
    "quality": "high"
  }
}
```

#### Response

```json
{
  "id": "string",
  "status": "queued",
  "progress": 0,
  "estimated_time": 0,
  "created_at": "string"
}
```

### Get Render Status

Check the status of a render job.

```http
GET /render/{jobId}/status
```

#### Response

```json
{
  "id": "string",
  "status": "rendering",
  "progress": 0.65,
  "estimated_time": 120,
  "output": {
    "url": "string",
    "size": 0,
    "duration": 0
  },
  "created_at": "string",
  "updated_at": "string"
}
```

## WebSocket Events

### Processing Progress Updates

```json
{
  "type": "processing_progress",
  "payload": {
    "jobId": "string",
    "type": "render",
    "progress": 0.75,
    "status": "processing",
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

- `INVALID_VIDEO`: Invalid video file
- `PROCESSING_ERROR`: Video processing failed
- `UNSUPPORTED_FORMAT`: Unsupported video format
- `STORAGE_LIMIT`: Storage limit exceeded
- `RENDER_ERROR`: Timeline rendering failed

## Supported Formats

### Input Formats
- Video: MP4, MOV, AVI, MKV
- Audio: MP3, WAV, AAC
- Image: PNG, JPG, GIF

### Output Formats
- Video: MP4 (H.264, H.265)
- Audio: MP3, AAC
- Image Sequence: PNG, JPG

## Video Effects

### Available Effects

1. **Filters**
   - Brightness
   - Contrast
   - Saturation
   - Color Balance
   - Blur
   - Sharpen

2. **Transitions**
   - Fade
   - Dissolve
   - Wipe
   - Slide
   - Zoom

3. **Transforms**
   - Scale
   - Rotate
   - Position
   - Crop
   - Ken Burns

## Example Usage

### JavaScript/TypeScript

```typescript
// Upload video
const uploadVideo = async (file: File, metadata: any) => {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('metadata', JSON.stringify(metadata));

  const response = await fetch('https://api.reelnet.com/api/v1/video/upload', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
    },
    body: formData,
  });

  return response.json();
};

// Render timeline
const renderTimeline = async (projectId: string, timeline: any) => {
  const response = await fetch('https://api.reelnet.com/api/v1/video/render', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      projectId,
      timeline,
      output: {
        format: 'mp4',
        resolution: '1920x1080',
        frameRate: 30,
        quality: 'high',
      },
    }),
  });

  return response.json();
};
```

### cURL

```bash
# Upload video
curl -X POST https://api.reelnet.com/api/v1/video/upload \
  -H "Authorization: Bearer <token>" \
  -F "file=@video.mp4" \
  -F 'metadata={"projectId":"123","name":"My Video"}'

# Check render status
curl https://api.reelnet.com/api/v1/video/render/job-123/status \
  -H "Authorization: Bearer <token>"
```

## Best Practices

1. **Upload**
   - Use chunked upload for large files
   - Implement resume capability
   - Validate file format before upload
   - Show upload progress to users

2. **Processing**
   - Monitor job status regularly
   - Implement webhook notifications
   - Handle failures gracefully
   - Cache processed results

3. **Rendering**
   - Optimize timeline before rendering
   - Use appropriate quality settings
   - Implement render queue management
   - Provide progress updates

## Rate Limiting

- Upload: 10 concurrent uploads per user
- Transcode: 5 concurrent jobs per user
- Render: 2 concurrent renders per user

For more information about video processing features, refer to:
- [Video Editing Features](../features/video-editing/)
- [Project Management](../features/project-management/) 