import { Box, Divider, Typography } from '@mui/material';
import { useParams } from 'react-router-dom';
import CreateCase from '../components/CreateCase';
import UploadDocument from '../components/UploadDocument';

export default function CaseDetailPage() {
    const { caseId } = useParams<{ caseId: string }>();
    const numericCaseId = Number(caseId);

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
            {/* Placeholder for document list */}
            <p>Document list will go here. In a real app, this would be a table or list of documents fetched from the API.</p>
        </Box>
    );
}
