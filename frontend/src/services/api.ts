import axios from 'axios';
import { useAuthStore } from '../stores/useAuthStore';

const apiClient = axios.create({
    baseURL: '/api', // Using relative URL for proxying
});

// Add a request interceptor to add required headers
apiClient.interceptors.request.use(
    (config) => {
        const { tenantId, token } = useAuthStore.getState();

        if (tenantId) {
            config.headers['X-Tenant-ID'] = tenantId;
        }

        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// --- Auth API ---
export const loginUser = async (username, password) => {
    const response = await apiClient.post('/auth/login', { username, password });
    return response.data; // Should contain the JWT
};


// --- Case API ---
export const createCase = async (name: string) => {
    const response = await apiClient.post('/cases', { name });
    return response.data;
};

// --- Document API ---
export const uploadDocument = async (file: File, caseId: number) => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('caseId', String(caseId));

    const response = await apiClient.post('/documents', formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    });
    return response.data;
};

// --- Search API ---
export const searchDocuments = async (query: string) => {
    const response = await apiClient.get('/documents/search', {
        params: { q: query },
    });
    return response.data;
};

export const downloadDocument = async (id: number, filename: string) => {
    const response = await apiClient.get(`/documents/${id}/download`, {
        responseType: 'blob', // Important for handling file downloads
    });

    // Create a URL for the blob
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', filename); // Use the filename from metadata
    document.body.appendChild(link);
    link.click();

    // Clean up
    link.parentNode?.removeChild(link);
    window.URL.revokeObjectURL(url);
};

export default apiClient;
