# Frontend Architecture

## Overview

The ReelNet frontend is built using React with TypeScript, following modern best practices and a component-based architecture.

## Technology Stack

- **Core Framework**: React 18+
- **State Management**: Redux Toolkit
- **Routing**: React Router
- **API Communication**: Axios, React Query
- **Real-time**: Socket.io-client
- **UI Components**: Material-UI
- **Video Processing**: FFmpeg.wasm
- **Testing**: Jest, React Testing Library
- **Build Tool**: Vite

## Project Structure

```
src/
├── assets/           # Static assets
├── components/       # Reusable components
│   ├── common/      # Shared components
│   ├── layout/      # Layout components
│   └── features/    # Feature-specific components
├── hooks/           # Custom React hooks
├── pages/           # Page components
├── services/        # API services
├── store/           # Redux store configuration
├── styles/          # Global styles
├── types/           # TypeScript definitions
└── utils/           # Utility functions
```

## Key Components

### Authentication

```typescript
// src/components/auth/LoginForm.tsx
interface LoginFormProps {
  onSubmit: (credentials: LoginCredentials) => void;
}

const LoginForm: React.FC<LoginFormProps> = ({ onSubmit }) => {
  // Form implementation
};
```

### Project Management

```typescript
// src/components/projects/ProjectList.tsx
interface Project {
  id: string;
  name: string;
  description: string;
  thumbnail: string;
}

const ProjectList: React.FC = () => {
  // Project list implementation
};
```

### Video Editor

```typescript
// src/components/editor/VideoEditor.tsx
interface VideoEditorProps {
  projectId: string;
  initialTimeline: Timeline;
}

const VideoEditor: React.FC<VideoEditorProps> = ({ projectId, initialTimeline }) => {
  // Video editor implementation
};
```

## State Management

### Redux Store Structure

```typescript
interface RootState {
  auth: {
    user: User | null;
    token: string | null;
    loading: boolean;
  };
  projects: {
    items: Project[];
    currentProject: Project | null;
    loading: boolean;
  };
  editor: {
    timeline: Timeline;
    selectedClip: Clip | null;
    playing: boolean;
  };
}
```

### Example Slice

```typescript
// src/store/slices/projectSlice.ts
const projectSlice = createSlice({
  name: 'projects',
  initialState,
  reducers: {
    setProjects: (state, action) => {
      state.items = action.payload;
    },
    // Other reducers
  },
});
```

## API Integration

### API Service

```typescript
// src/services/api.ts
const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

export const projectService = {
  getProjects: () => api.get('/projects'),
  createProject: (data: CreateProjectDTO) => api.post('/projects', data),
  // Other API methods
};
```

## Real-time Features

### WebSocket Integration

```typescript
// src/services/websocket.ts
class WebSocketService {
  private socket: Socket;

  constructor() {
    this.socket = io(WEBSOCKET_URL, {
      auth: { token: getAuthToken() },
    });
  }

  // WebSocket methods
}
```

## Routing Configuration

```typescript
// src/App.tsx
const App: React.FC = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/projects" element={<ProjectsPage />} />
        <Route path="/editor/:projectId" element={<EditorPage />} />
        {/* Other routes */}
      </Routes>
    </Router>
  );
};
```

## Styling Approach

### Theme Configuration

```typescript
// src/styles/theme.ts
const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#dc004e',
    },
  },
  // Other theme configurations
});
```

## Performance Optimizations

1. **Code Splitting**
   - Route-based splitting
   - Component lazy loading
   - Dynamic imports

2. **Caching Strategy**
   - API response caching
   - Asset caching
   - State persistence

3. **Rendering Optimizations**
   - Virtualized lists
   - Memoization
   - Debouncing/Throttling

## Error Handling

```typescript
// src/components/common/ErrorBoundary.tsx
class ErrorBoundary extends React.Component<Props, State> {
  static getDerivedStateFromError(error: Error) {
    return { hasError: true, error };
  }

  // Error boundary implementation
}
```

## Testing Strategy

### Component Testing

```typescript
// src/components/ProjectList.test.tsx
describe('ProjectList', () => {
  it('renders projects correctly', () => {
    render(<ProjectList projects={mockProjects} />);
    // Test assertions
  });
});
```

## Build and Deployment

```typescript
// vite.config.ts
export default defineConfig({
  plugins: [react()],
  build: {
    outDir: 'dist',
    sourcemap: true,
  },
  // Other configurations
});
```

For more detailed information about specific features, refer to:
- [Component Documentation](../features/)
- [API Integration](../api/)
- [Performance Guidelines](../technical/performance.md) 