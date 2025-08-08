import { AppBar, Box, Button, Toolbar, Typography } from '@mui/material';
import { Outlet } from 'react-router-dom';
import RoleBasedGuard from './RoleBasedGuard';
import SearchBar from './SearchBar';
import TenantSwitcher from './TenantSwitcher';

export default function Layout() {
  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            DMS
          </Typography>
          <SearchBar />
          <Box sx={{ mx: 1 }} />
          <RoleBasedGuard allowedRoles={['ADMIN']}>
            <Button color="inherit">Admin Section</Button>
          </RoleBasedGuard>
          <TenantSwitcher />
        </Toolbar>
      </AppBar>
      <Box component="main" sx={{ p: 3 }}>
        <Outlet />
      </Box>
    </Box>
  );
}
