import { Navigate, Outlet, useLocation } from "react-router-dom";
import { ROUTES } from "../constants";
import { useAuth } from "../../auth/hooks/useAuth";
import LoadingFallbackPage from "@/shared/pages/LoadingFallbackPage";

export const ProtectedRoute = () => {
    const location = useLocation();
  const { isAuthenticated, isLoading } = useAuth();

  if (isLoading) {
    return <LoadingFallbackPage />;
  }

  // if (!isAuthenticated && !isLoading && location.pathname !== ROUTES.HOME) {
  //   return <Navigate to={ROUTES.HOME} replace />;
  // }
  // if (isAuthenticated && location.pathname === ROUTES.HOME) {
  //   return <Navigate to={ROUTES.VIDEO_EDITOR.WORKSPACE} replace />;
  // }

  return <Outlet />;
};
