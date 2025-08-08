import { Box, Button, Divider, List, ListItem, ListItemText, Typography } from '@mui/material';
import { useParams } from 'react-router-dom';
import CreateCase from '../components/CreateCase';
import UploadDocument from '../components/UploadDocument';
import { downloadDocument } from '../services/api';

// Mock document data
const mockDocuments = [
    { id: 101, name: 'report-final.pdf', contentType: 'application/pdf' },
    { id: 102, name: 'meeting-notes.docx', contentType: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document' },
];

export default function CaseDetailPage() {
    const { caseId } = useParams<{ caseId: string }>();
    const numericCaseId = Number(caseId);

    const handleDownload = (id: number, filename: string) => {
        downloadDocument(id, filename).catch(err => {
            console.error("Download failed:", err);
            alert("Failed to download file.");
        });
    };

    return (
        <Box>
            <Typography variant="h4" gutterBottom>
                Case Details
            </Typography>
            <Typography variant="subtitle1" color="text.secondary">
                Currently viewing Case ID: {caseId}
            </Typography>

            <Divider sx={{ my: 2 }} />

            <Typography variant="h6">Create a New Case</Typography>
            <CreateCase />

            <Divider sx={{ my: 2 }} />

            {numericCaseId && (
                <>
                    <Typography variant="h6">Upload Document to this Case</Typography>
                    <UploadDocument caseId={numericCaseId} />
                </>
            )}

            <Divider sx={{ my: 2 }} />

            <Typography variant="h6" sx={{ mt: 4 }}>
                Documents in this Case
            </Typography>
            <List>
                {mockDocuments.map(doc => (
                    <ListItem key={doc.id} secondaryAction={
                        <Button variant="outlined" size="small" onClick={() => handleDownload(doc.id, doc.name)}>
                            Download
                        </Button>
                    }>
                        <ListItemText primary={doc.name} secondary={doc.contentType} />
                    </ListItem>
                ))}
            </List>
        </Box>
    );
}
