import { Box, Typography } from '@mui/material';
import { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { searchDocuments } from '../services/api';

// A simple type for the document data we expect
interface Document {
    id: number;
    name: string;
    contentType: string;
}

export default function SearchResultsPage() {
    const [searchParams] = useSearchParams();
    const query = searchParams.get('q');
    const [results, setResults] = useState<Document[]>([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (query) {
            setLoading(true);
            searchDocuments(query)
                .then(data => setResults(data))
                .catch(err => console.error("Search failed:", err))
                .finally(() => setLoading(false));
        }
    }, [query]);

    return (
        <Box>
            <Typography variant="h4">Search Results</Typography>
            <Typography variant="subtitle1" gutterBottom>
                Showing results for: <strong>{query}</strong>
            </Typography>
            {loading && <p>Loading...</p>}
            <ul>
                {results.map(doc => (
                    <li key={doc.id}>{doc.name} ({doc.contentType})</li>
                ))}
            </ul>
        </Box>
    );
}
