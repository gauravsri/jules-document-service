import { Box, Button, TextField } from '@mui/material';
import { useState } from 'react';
import { createCase } from '../services/api';

export default function CreateCase() {
    const [name, setName] = useState('');

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        if (!name.trim()) return;
        try {
            const newCase = await createCase(name);
            alert(`Case created successfully! ID: ${newCase.id}`);
            setName('');
        } catch (error) {
            alert('Failed to create case.');
            console.error(error);
        }
    };

    return (
        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 2 }}>
            <TextField
                label="New Case Name"
                variant="outlined"
                size="small"
                value={name}
                onChange={(e) => setName(e.target.value)}
            />
            <Button type="submit" variant="contained" sx={{ ml: 1 }}>
                Create Case
            </Button>
        </Box>
    );
}
