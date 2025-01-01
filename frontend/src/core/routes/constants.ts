export const ROUTES = {
  HOME: '/',
  AUTH: {
    LOGIN: '/auth/login',
    REGISTER: '/auth/register',
    FORGOT_PASSWORD: '/auth/forgot-password',
  },
  COMMUNITY: {
    COMMUNITY: '/community',
  },
  USER_PROFILE: {
    PROFILE: '/user-profile',
  },
  
  VIDEO_EDITOR: {
    WORKSPACE: '/video-editor',
    PROJECT: '/video-editor/:projectId',
  },
} as const;

export type AppRoutes = typeof ROUTES; 