import { lazy, Suspense } from "react";
import { createBrowserRouter } from "react-router-dom";
import { ROUTES } from "./constants";
import { ProtectedRoute } from "./components/ProtectedRoute";
import LoadingFallbackPage from "@/shared/pages/LoadingFallbackPage";
import BlankLayout from "./layout/BlankLayout";
import HomePage from "@/shared/pages/HomePage";
import { MainLayout } from "./layout/MainLayout";
// Lazy load components
const WorkspacePage = lazy(() => import("@/shared/pages/WorkspacePage"));
const CommunityPage = lazy(() => import("@/shared/pages/CommunityPage"));

// Lazy load components

const LoadingFallback = () => <LoadingFallbackPage />;

export const router = createBrowserRouter([
  {
    element: <ProtectedRoute />,
    children: [
      {
        element: <BlankLayout />,
        children: [
          {
            path: ROUTES.HOME,
            element: (
              <Suspense fallback={<LoadingFallback />}>
                <HomePage />
              </Suspense>
            ),
          },
        ],
      },
      {
        element: <MainLayout />,
        children: [
          {
            path: ROUTES.VIDEO_EDITOR.WORKSPACE,
            element: (
              <Suspense fallback={<LoadingFallback />}>
                <WorkspacePage />
              </Suspense>
            ),
          },
          {
            path: ROUTES.COMMUNITY.COMMUNITY,
            element: (
              <Suspense fallback={<LoadingFallback />}>
                <CommunityPage />
              </Suspense>
            ),
          },
        ],
      },

    ],
  },
]);
