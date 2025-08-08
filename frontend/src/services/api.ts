import axios from 'axios';
import { useAuthStore } from '../stores/useAuthStore';

const apiClient = axios.create({
    baseURL: '/api', // Using relative URL for proxying
});

// Add a request interceptor to add the tenant ID to headers
apiClient.interceptors.request.use(
    (config) => {
        const { tenantId } = useAuthStore.getState();
        if (tenantId) {
            config.headers['X-Tenant-ID'] = tenantId;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

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

export default apiClient;
