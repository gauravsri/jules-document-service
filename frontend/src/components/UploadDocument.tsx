import { Box, Button } from '@mui/material';
import { useState } from 'react';
import { uploadDocument } from '../services/api';

interface UploadDocumentProps {
    caseId: number;
}

export default function UploadDocument({ caseId }: UploadDocumentProps) {
    const [selectedFile, setSelectedFile] = useState<File | null>(null);

    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        if (event.target.files) {
            setSelectedFile(event.target.files[0]);
        }
    };

    const handleUpload = async () => {
        if (!selectedFile) {
            alert('Please select a file first.');
            return;
        }
        try {
            await uploadDocument(selectedFile, caseId);
            alert('File uploaded successfully!');
            setSelectedFile(null);
        } catch (error) {
            alert('Failed to upload file.');
            console.error(error);
        }
    };

    return (
        <Box sx={{ mt: 2 }}>
            <input type="file" onChange={handleFileChange} />
            <Button variant="contained" onClick={handleUpload} disabled={!selectedFile} sx={{ ml: 1 }}>
                Upload Document
            </Button>
        </Box>
    );
}
