import { RouterProvider } from 'react-router-dom';
import { router } from './core/routes/config';
import { Auth0Provider } from '@auth0/auth0-react';

const AUTH0_DOMAIN = import.meta.env.VITE_AUTH0_DOMAIN || '';
const AUTH0_CLIENT_ID = import.meta.env.VITE_AUTH0_CLIENT_ID || '';
const AUTH0_CALLBACK_URL = import.meta.env.VITE_AUTH0_CALLBACK_URL || 'http://localhost:3000';

function App() {
  return (
    <Auth0Provider
      domain={AUTH0_DOMAIN}
      clientId={AUTH0_CLIENT_ID}
      authorizationParams={{
        redirect_uri: AUTH0_CALLBACK_URL,
      }}
    >
      <RouterProvider router={router} />
    </Auth0Provider>
  );
}

export default App;
