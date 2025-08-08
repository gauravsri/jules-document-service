import { FormControl, MenuItem, Select, SelectChangeEvent } from '@mui/material';
import { useAuthStore } from '../stores/useAuthStore';

// Mock tenants
const tenants = [
  { id: 'tenant-a', name: 'Tenant A' },
  { id: 'tenant-b', name: 'Tenant B' },
  { id: 'tenant-c', name: 'Tenant C' },
];

export default function TenantSwitcher() {
  const { tenantId, setTenant } = useAuthStore();

  const handleChange = (event: SelectChangeEvent<string>) => {
    setTenant(event.target.value);
  };

  return (
    <FormControl size="small">
      <Select
        value={tenantId || ''}
        onChange={handleChange}
        displayEmpty
        inputProps={{ 'aria-label': 'Without label' }}
        sx={{ color: 'white', '.MuiOutlinedInput-notchedOutline': { borderColor: 'rgba(255, 255, 255, 0.5)' } }}
      >
        <MenuItem value="" disabled>
          <em>Select Tenant</em>
        </MenuItem>
        {tenants.map((tenant) => (
          <MenuItem key={tenant.id} value={tenant.id}>
            {tenant.name}
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  );
}
